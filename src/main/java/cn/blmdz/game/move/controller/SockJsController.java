package cn.blmdz.game.move.controller;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;

import cn.blmdz.game.move.config.ConstantUtil;
import cn.blmdz.game.move.enums.MessageType;
import cn.blmdz.game.move.model.Bubble;
import cn.blmdz.game.move.model.Message;
import cn.blmdz.game.move.model.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.GeoRadiusParam;

/**
 * 控制器
 */
@Slf4j
@Controller
public class SockJsController {

	private @Autowired SimpMessagingTemplate template;
	private Jedis jedis = new Jedis("127.0.0.1", 6379);
	
	
	@MessageMapping("/say")
	public void greeting(Message message, StompHeaderAccessor accessor, UserPrincipal principal) {
		log.info("message: {}", JSON.toJSONString(message));
		log.info("accessor: {}", accessor);
		log.info("principal: {}", principal);
		String sessionId = accessor.getSessionId();
		
		MessageType type = MessageType.conversion(message.getType());
		switch (type) {
		case F_REGISTER:
			Bubble bubble = JSON.parseObject(message.getMsg(), Bubble.class);
			bubble.setId(sessionId);
			
			// 增加位置
			jedis.geoadd(ConstantUtil.REDIS_KEY,
					bubble.getX() * 0.00001,
					bubble.getY() * 0.00001,
					bubble.getId());
			// 增加玩家
			jedis.set("player:" + principal.getName(), JSON.toJSONString(bubble));
			
			// 增加映射关系
//			jedis.set("session:" + principal.getName(), sessionId);
			ConstantUtil.player.put(principal.getName(), sessionId);
			
//			ConstantUtil.send(template, sessionId, JSON.toJSONString(bubble));
			loop ();
			break;
		default:
			break;
		}
	}
	
	private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
	
	@PostConstruct
	public void init () {
		fixedThreadPool.execute(new Runnable() {
			@Override
			public void run() {
//				while (true) {
//				}
			}
		});
	}
	public void loop () {
		System.out.println(JSON.toJSONString(ConstantUtil.player));
		Set<String> players = Sets.newConcurrentHashSet(ConstantUtil.player.keySet());
		players.forEach(webId -> {
			// 并发有问题
//			fixedThreadPool.execute(new Runnable() {
//				@Override
//				public void run() {
					
					List<GeoRadiusResponse> georadiusMembers =  jedis.georadiusByMember(
							ConstantUtil.REDIS_KEY,
							ConstantUtil.player.get(webId),
							10000,
							GeoUnit.KM,
							GeoRadiusParam.geoRadiusParam().withCoord().withDist().sortAscending());
					
					List<Bubble> collect = georadiusMembers.stream().map(georadiusMember -> {
						
						String str = jedis.get("player:" + webId); // 这个应该是georadiusMember.getMember() 对应的webid
						if (StringUtils.isNotBlank(str)) {
							Bubble bubble = JSON.parseObject(str, Bubble.class);
							bubble.setX(((int) (georadiusMember.getCoordinate().getLongitude() * 100000)));
							bubble.setY(((int) (georadiusMember.getCoordinate().getLatitude() * 100000)));
							bubble.setDistance(((int)(georadiusMember.getDistance() * 100) * 1.0) / 100);
							return bubble;
						}
						return null;
					}).collect(Collectors.toList());
					
					collect = collect.stream().filter(item -> item != null && !item.getId().equals(ConstantUtil.player.get(webId))).collect(Collectors.toList());
					if (!CollectionUtils.isEmpty(collect)) {
						Message message = new Message();
						message.setType(MessageType.B_DISTANCE.value());
						message.setMsg(JSON.toJSONString(collect));
						
						ConstantUtil.send(template, ConstantUtil.player.get(webId), JSON.toJSONString(message));
						log.debug("send distance: {}, all: {}", webId, JSON.toJSONString(message));
					}
//				}
//			});
		});
	}
}