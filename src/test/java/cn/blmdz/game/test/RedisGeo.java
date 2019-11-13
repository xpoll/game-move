package cn.blmdz.game.test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.GeoRadiusParam;

public class RedisGeo {

	public static void main(String[] args) {
		
		@SuppressWarnings("resource")
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		/*
		 * 有效的经度从-180度到180度。
		 * 有效的纬度从-85.05112878度到85.05112878度。
		 * 
		 * 
		 * geoadd 将一定的位置对象（维度，经度，名字）添加到key
		 * geopos 从key里面返回所有给定位置对象的位置
		 * geodist 返回两个给定位置之间的距离
		 * geohash 返回一个或多个位置对象的geohash表示
		 * georadius 以给定的经纬度为中心，返回目标集合中与中心距离不超过给定最大距离的所有位置对象
		 * georadiusbymember 以给定位置对象为中心，返回与其距离不超过给定最大距离的所有位置对象
		 */
		
		
		String key = "k1";
		int member = 100000;
		int width = 2000;
		int height = 2000;
		int radius = 100;
		
		System.out.println("地图宽: " + width + ", 高: " + height);
		System.out.println("移除所有点: " + jedis.del(key));
		long a = System.currentTimeMillis();
		
		for (int i = 0; i < member; i++) {
			jedis.geoadd(key, ThreadLocalRandom.current().nextInt(width) * 0.00001, ThreadLocalRandom.current().nextInt(height) * 0.00001, "m" + i);
		}
		
		long b = System.currentTimeMillis();
		System.out.println("增加: " + member + "人, 耗时: " + (b - a) + "ms");
		
		int m = ThreadLocalRandom.current().nextInt(member);
		
		a = System.currentTimeMillis();
		
		jedis.zrem(key, "m" + m);
		System.out.println(jedis.geoadd(key, ThreadLocalRandom.current().nextInt(width) * 0.00001, ThreadLocalRandom.current().nextInt(height) * 0.00001, "m" + m));
		
		List<GeoRadiusResponse> georadiusMembers = jedis.georadiusByMember(key, "m" + m, radius, GeoUnit.M, GeoRadiusParam.geoRadiusParam().withCoord().withDist().sortAscending());
		
		List<GeoCoordinate> geopos = jedis.geopos(key, "m" + m);
		System.out.println("随机人物: m" + m + "(" + ((int) (geopos.get(0).getLongitude() * 100000)) + ", " + ((int) (geopos.get(0).getLatitude() * 100000)) + "), 范围: " + radius + " 的人物有: " + georadiusMembers.size() + "个");
		b = System.currentTimeMillis();
		System.out.println("查找耗时: " + (b - a) + "ms");
		
		
		georadiusMembers.forEach(item -> {
			System.out.println(
					" 人物: " + new String(item.getMember())
					+ " 位置: (" + ((int) (item.getCoordinate().getLongitude() * 100000)) + ", " + ((int) (item.getCoordinate().getLatitude() * 100000)) + ")"
					+ " 距离: " + item.getDistance());
		});
	}
}
