package cn.blmdz.game.move.util;

import java.util.List;

import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.GeoRadiusParam;

public class RedisUtil {
	static final Object lock = new Object();
	static Jedis jedis = new Jedis("127.0.0.1", 6379);

	public static Long del(final String key) {
		synchronized (lock) {
			return jedis.del(key);
		}
	}

	public static Long zrem(final String key, final String... members) {
		synchronized (lock) {
			return jedis.zrem(key, members);
		}
	}

	public static Long geoadd(final String key, final double longitude, final double latitude, final String member) {
		synchronized (lock) {
			return jedis.geoadd(key, longitude, latitude, member);
		}
	}

	public static String set(final String key, final String value) {
		synchronized (lock) {
			return jedis.set(key, value);
		}
	}

	public static String get(final String key) {
		synchronized (lock) {
			return jedis.get(key);
		}
	}

	public static List<GeoRadiusResponse> georadiusByMember(final String key, final String member,
			final double radius, final GeoUnit unit, final GeoRadiusParam param) {
		synchronized (lock) {
			return jedis.georadiusByMember(key, member, radius, unit, param);
		}
	}
}
