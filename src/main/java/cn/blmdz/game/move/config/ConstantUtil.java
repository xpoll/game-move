package cn.blmdz.game.move.config;

import java.util.Map;

import com.google.common.collect.Maps;

public class ConstantUtil {

	private static long id = 0;
	public static Map<String, Long> player = Maps.newHashMap();
	/** 频道 */
	public static String GREETINGS = "/simple/greetings";
	public static String REDIS_KEY = "key1";
	
	public static long generateID() {
		return id++;
	}
}
