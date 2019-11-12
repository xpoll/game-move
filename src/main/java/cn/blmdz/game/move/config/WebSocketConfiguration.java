package cn.blmdz.game.move.config;

import java.security.Principal;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import cn.blmdz.game.move.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker// 开启使用STOMP协议来传输基于代理的消息，Broker就是代理的意思
@EnableWebSocket
public class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
    	config.setApplicationDestinationPrefixes("/app"); //发送消息前缀 @MessageMapping("/say")  stomp.send("/app/say"...  
        config.enableSimpleBroker("/simple", "/user"); // 订阅代理  stomp.subscribe('/simple/greetings'...   template.convertAndSend('/simple/greetings'...
        config.setUserDestinationPrefix("/user"); // 默认用户发送
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {// StompEndpointRegistry 注册STOMP协议的节点，并指定映射的URL
        StompWebSocketEndpointRegistration stompWebSocketEndpointRegistration = registry.addEndpoint("/endpoint/placard").setAllowedOrigins("*"); // 注册STOMP协议节点  new SockJS("/endpoint/placard");
        stompWebSocketEndpointRegistration.addInterceptors(new WebSocketInterceptor());
        stompWebSocketEndpointRegistration.setHandshakeHandler(new DefaultHandshakeHandler(){
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
            	return (Principal) attributes.get("user");
            }
        });
        stompWebSocketEndpointRegistration.withSockJS(); // 指定使用SockJS协议。
    }

    @EventListener
    public void onSessionConnectEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        log.debug("ConnectEvent web: {}, socket: {}", sha.getUser().getName(), sha.getSessionId());
    }
    @EventListener
    public void onSessionConnectedEvent(SessionConnectedEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        log.debug("ConnectedEvent web: {}, socket: {}", sha.getUser().getName(), sha.getSessionId());
    }
    @EventListener
    public void onSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        log.debug("SubscribeEvent web: {}, socket: {}", sha.getUser().getName(), sha.getSessionId());
    }
    @EventListener
    public void onSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        log.debug("UnsubscribeEvent web: {}, socket: {}", sha.getUser().getName(), sha.getSessionId());
        
//        ConstantUtil.player.remove(sha.getUser().getName());
//        jedis.del("player:" + sha.getUser().getName());
//        jedis.zrem(ConstantUtil.REDIS_KEY, sha.getSessionId());
    }
    @EventListener
    public void onSessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        log.debug("DisconnectEvent web: {}, socket: {}", sha.getUser().getName(), sha.getSessionId());

        ConstantUtil.player.remove(sha.getUser().getName());
        RedisUtil.del("player:" + sha.getUser().getName());
        RedisUtil.zrem(ConstantUtil.REDIS_KEY, sha.getUser().getName());
    }
}