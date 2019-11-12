package cn.blmdz.game.move.config;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import cn.blmdz.game.move.model.UserPrincipal;
import lombok.extern.slf4j.Slf4j;

/**
 * socket 封装用户
 * @author yongzongyang
 * @date 2018年1月26日
 */
@Slf4j
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
            HttpSession session = serverHttpRequest.getServletRequest().getSession();
            if (session != null && session.getAttribute("user") != null) {
            	attributes.put("user", session.getAttribute("user"));
                log.debug("web session add: {}", ((UserPrincipal)session.getAttribute("user")).getName());
            } else {
            	UserPrincipal userPrincipal = new UserPrincipal(session.getId());
                session.setAttribute("user", userPrincipal);
                attributes.put("user", userPrincipal);
                
                log.debug("web session new: {}", userPrincipal.getName());
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Exception exception) {
    }
}
