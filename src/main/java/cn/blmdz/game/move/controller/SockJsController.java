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
import cn.blmdz.game.move.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.params.GeoRadiusParam;

/**
 * 控制器
 */
@Slf4j
@Controller
public class SockJsController {

	private @Autowired SimpMessagingTemplate template;
	
	@MessageMapping("/say")
	public void greeting(Message message, StompHeaderAccessor accessor, UserPrincipal principal) {
		log.info("message: {}", JSON.toJSONString(message));
		log.info("accessor: {}", accessor);
		log.info("principal: {}", principal);
		
		MessageType type = MessageType.conversion(message.getType());
		switch (type) {
		case F_REGISTER: {
			Bubble bubble = JSON.parseObject(message.getMsg(), Bubble.class);
			bubble.setId(principal.getName());
			
			// 增加位置
			RedisUtil.geoadd(ConstantUtil.REDIS_KEY,
					bubble.getX() * 0.00001,
					bubble.getY() * 0.00001,
					bubble.getId());
			// 增加玩家
			RedisUtil.set("player:" + principal.getName(), JSON.toJSONString(bubble));
			
			// 增加映射关系
			ConstantUtil.player.put(principal.getName(), accessor.getSessionId());
			break;
		}
		case F_MOVE: {
			Bubble bubble = JSON.parseObject(message.getMsg(), Bubble.class);
			bubble.setId(principal.getName());
			RedisUtil.zrem(ConstantUtil.REDIS_KEY, bubble.getId());
			RedisUtil.geoadd(ConstantUtil.REDIS_KEY,
					bubble.getX() * 0.00001,
					bubble.getY() * 0.00001,
					bubble.getId());
			RedisUtil.set("player:" + principal.getName(), JSON.toJSONString(bubble));
			break;
		}
		default:
			break;
		}
		loop ();
	}
	
	private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
	
	@PostConstruct
	public void init () {
		RedisUtil.del(ConstantUtil.REDIS_KEY);
//		fixedThreadPool.execute(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
//					loop ();
//					try {
//						Thread.sleep(80);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
	}
	public void loop () {
		Set<String> players = Sets.newConcurrentHashSet(ConstantUtil.player.keySet());
		players.forEach(webId -> {
			// 并发有问题
			fixedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					
					List<GeoRadiusResponse> georadiusMembers =  RedisUtil.georadiusByMember(
							ConstantUtil.REDIS_KEY,
							webId,
							500,
							GeoUnit.M,
							GeoRadiusParam.geoRadiusParam().withCoord().withDist().sortAscending());
					
					List<Bubble> collect = georadiusMembers.stream().filter(item -> !item.getMemberByString().equalsIgnoreCase(webId)).map(georadiusMember -> {
						String str = RedisUtil.get("player:" + georadiusMember.getMemberByString()); // 这个应该是georadiusMember.getMember() 对应的webid
						if (StringUtils.isNotBlank(str)) {
							Bubble bubble = JSON.parseObject(str, Bubble.class);
							bubble.setX(((int) (georadiusMember.getCoordinate().getLongitude() * 100000)));
							bubble.setY(((int) (georadiusMember.getCoordinate().getLatitude() * 100000)));
							bubble.setDistance(((int)(georadiusMember.getDistance() * 100) * 1.0) / 100);
							return bubble;
						}
						return null;
					}).collect(Collectors.toList());
					
					collect = collect.stream().filter(item -> item != null).collect(Collectors.toList());
					Message message = new Message();
					message.setType(MessageType.B_DISTANCE.value());
					message.setMsg(CollectionUtils.isEmpty(collect) ? "[]" : JSON.toJSONString(collect));
					if (ConstantUtil.player.get(webId) != null) {
						ConstantUtil.send(template, ConstantUtil.player.get(webId), JSON.toJSONString(message));
						log.debug("send distance: {}, all: {}", webId, JSON.toJSONString(message));
					}
				}
			});
		});
	}
}