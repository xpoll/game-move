package cn.blmdz.game.move.config;

import java.util.Map;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

public class ConstantUtil {

	// WebSession, SocketSession
	public static Map<String, String> player = Maps.newHashMap();
	/** 频道 */
	public static String GREETINGS = "/simple/greetings";
	public static String REDIS_KEY = "key1";
	
	
	public static <T> void send(SimpMessagingTemplate template, T t) {
		template.convertAndSend(GREETINGS, t);
	}
	
	public static <T> void send(SimpMessagingTemplate template, String sessionId, T t) {
		template.convertAndSendToUser(sessionId, GREETINGS, t, createHeaders(sessionId));
	}

	private static MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        System.out.println(JSON.toJSONString(headerAccessor.getMessageHeaders()));
        return headerAccessor.getMessageHeaders();
    }
}
