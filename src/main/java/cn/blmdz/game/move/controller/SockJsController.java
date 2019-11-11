package cn.blmdz.game.move.controller;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;

import cn.blmdz.game.move.config.ConstantUtil;
import cn.blmdz.game.move.model.Message;
import cn.blmdz.game.move.properties.OtherProperties;
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

	private SimpMessagingTemplate template;
	private OtherProperties properties;
	private Jedis jedis = new Jedis("127.0.0.1", 6379);
	
	
	@MessageMapping("/say")
	public void greeting(Message message) {
		log.info("message: {}", JSON.toJSONString(message));
		template.convertAndSend(ConstantUtil.GREETINGS, JSON.toJSONString(message));
	}	
	
	private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
	
//	@PostConstruct
	public void init () {
		while (true) {
			Set<Long> players = Sets.newConcurrentHashSet(ConstantUtil.player.values());
			players.forEach(item -> {
				fixedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						List<GeoRadiusResponse> georadiusMembers =  jedis.georadiusByMember(
								ConstantUtil.REDIS_KEY,
								"m" + item,
								100,
								GeoUnit.M,
								GeoRadiusParam.geoRadiusParam().withCoord().withDist().sortAscending());
//						georadiusMembers.
					}
				});
				
			});
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}