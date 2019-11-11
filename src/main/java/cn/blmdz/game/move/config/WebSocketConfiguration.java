package cn.blmdz.game.move.config;

import java.security.Principal;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import cn.blmdz.game.move.model.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker// 开启使用STOMP协议来传输基于代理的消息，Broker就是代理的意思
@EnableWebSocket
public class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer implements WebSocketConfigurer {

	private Jedis jedis = new Jedis("127.0.0.1", 6379);
	
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
    	config.setApplicationDestinationPrefixes("/app"); //发送消息前缀 @MessageMapping("/say")  stomp.send("/app/say"...  
        config.enableSimpleBroker("/simple"); // 订阅代理  stomp.subscribe('/simple/greetings'...   template.convertAndSend('/simple/greetings'...
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

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    }

    @EventListener
    public void onSessionConnectEvent(SessionConnectEvent event) {
        log.debug("Client with username {} SessionConnectEvent", event.getUser().getName());
    }
    @EventListener
    public void onSessionConnectedEvent(SessionConnectedEvent event) {
        log.debug("Client with username {} SessionConnectedEvent", event.getUser().getName());
    }
    @EventListener
    public void onSessionSubscribeEvent(SessionSubscribeEvent event) {
        log.debug("Client with username {} SessionSubscribeEvent", event.getUser().getName());
        UserPrincipal user = (UserPrincipal) event.getUser();
        ConstantUtil.player.put(user.getName(), user.getId());
    }
    @EventListener
    public void onSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
        log.debug("Client with username {} SessionUnsubscribeEvent", event.getUser().getName());
        ConstantUtil.player.remove(event.getUser().getName());
		jedis.zrem(ConstantUtil.REDIS_KEY, "m" + ConstantUtil.player.get(event.getUser().getName()));
    }
    @EventListener
    public void onSessionDisconnectEvent(SessionDisconnectEvent event) {
        log.debug("Client with username {} SessionDisconnectEvent", event.getUser().getName());
        ConstantUtil.player.remove(event.getUser().getName());
		jedis.zrem(ConstantUtil.REDIS_KEY, "m" + ConstantUtil.player.get(event.getUser().getName()));
    }
}