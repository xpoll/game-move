package cn.blmdz.game.test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 格子范围广播
 * @author xpoll
 * @address https://xpoll.blmdz.cn
 */
public class GameLatticeDemo {
	// 地图 X 长度
    private static final Long MAP_X = 2000L;
    // 地图 Y 长度
    private static final Long MAP_Y = 2000L;
    // 格子边长
    private static final Long LATTICE_LENGTH = 10L;
    // 通信圈  半径
    private static final Long CIRCLE_RADIUS = 100L;
    // 地图玩家人数
    private static final Long PLAYER_NUM = 100000L;
    
    public static void main(String[] args) {
    	System.out.println("######################################################");
    	System.out.println(String.format("地图大小 %s x %s", MAP_X, MAP_Y));
        System.out.println(String.format("格子边长 %s", LATTICE_LENGTH));
    	System.out.println(String.format("通信圈(假园)半径 %s", CIRCLE_RADIUS));
    	System.out.println(String.format("地图玩家人数 %s", PLAYER_NUM));
    	System.out.println("######################################################");
    	
    	// 初始化玩家和位置
    	System.out.println(String.format("地图玩家人数 %s", PLAYER_NUM));
    	Long a = System.currentTimeMillis();
    	Long player, lattice;
    	Coordinate coordinate;
        for (int i = 0; i < PLAYER_NUM; i++) {
        	coordinate = getInitCoordinate();
        	player = (long) i;
        	lattice = getLatticeNumber(coordinate);
        	CONNECTIONS.put(player, new Connection(player, coordinate, lattice));
        	putLattices(lattice, player);
		}
    	System.out.println(String.format("初始化玩家耗时 %s毫秒", System.currentTimeMillis() - a));
        
        // 随机一个玩家走动
        player = ThreadLocalRandom.current().nextLong(PLAYER_NUM);
        coordinate = getInitCoordinate();
        lattice = getLatticeNumber(coordinate);
    	System.out.println(String.format("随机玩家 %s 位置 (%s, %s) ==> (%s, %s), 格子 %s ==> %s。",
    			player,
    			CONNECTIONS.get(player).getCoordinate().getX(),
    			CONNECTIONS.get(player).getCoordinate().getY(),
    			coordinate.getX(),
    			coordinate.getY(),
    			CONNECTIONS.get(player).getLattice(),
    			lattice));
    	a = System.currentTimeMillis();
        if (Objects.equals(CONNECTIONS.get(player).getLattice(), lattice)) {
        	// 格子不变
        	CONNECTIONS.get(player).setCoordinate(coordinate);
        	String content = String.format("%s 位置变动后格子没变, 是%s。", player, lattice);
        	// 周围格子
        	Set<Long> lattices = getAroundLatticeNumber(lattice);
        	// 周围玩家
        	Set<Long> players = Sets.newHashSet();
        	lattices.forEach(item -> {
        		players.addAll(MoreObjects.firstNonNull(LATTICES.get(item), Sets.newHashSet()));
        	});
        	players.remove(player);

        	System.out.println(String.format("获取到可以通信玩家耗时 %s毫秒", System.currentTimeMillis() - a));
        	players.forEach(item -> {
        		CONNECTIONS.get(item).say(content);
        	});
        } else {
        	// 格子变动
        	Long oldLattice = CONNECTIONS.get(player).getLattice();
        	CONNECTIONS.get(player).setCoordinate(coordinate);
        	CONNECTIONS.get(player).setLattice(lattice);
        	removeLattices(oldLattice, player);
        	putLattices(lattice, player);
        	String content1 = String.format("移除: %s 位置变动后格子变了, 是 %s ==> %s。", player, oldLattice, lattice);
        	String content2 = String.format("添加: %s 位置变动后格子变了, 是 %s ==> %s。", player, oldLattice, lattice);
        	
        	// 旧的周围格子
        	Set<Long> lattices1 = getAroundLatticeNumber(oldLattice);
        	// 周围玩家
        	Set<Long> players1 = Sets.newHashSet();
        	lattices1.forEach(item -> {
        		players1.addAll(MoreObjects.firstNonNull(LATTICES.get(item), Sets.newHashSet()));
        	});
        	players1.remove(player);
        	
        	// 新的周围格子
        	Set<Long> lattices2 = getAroundLatticeNumber(lattice);
        	// 周围玩家
        	Set<Long> players2 = Sets.newHashSet();
        	lattices2.forEach(item -> {
        		players2.addAll(MoreObjects.firstNonNull(LATTICES.get(item), Sets.newHashSet()));
        	});
        	players2.remove(player);

        	System.out.println(String.format("获取到可以通信玩家耗时 %s毫秒, 移除: %s, 新增: %s", System.currentTimeMillis() - a, players1.size(), players2.size()));
        	players1.forEach(item -> {
        		CONNECTIONS.get(item).say(content1);
        	});
        	players2.forEach(item -> {
        		CONNECTIONS.get(item).say(content2);
        	});
        }
        
    }
    
    // 距离比较值
    private static final double DISTANCE = Math.pow(CIRCLE_RADIUS + Math.sqrt(2) * LATTICE_LENGTH / 2, 2);
    // 人 => 通信  映射
    private static Map<Long, Connection> CONNECTIONS = Maps.newHashMap();
    // 格子 => 人 映射
    private static Map<Long, Set<Long>> LATTICES = Maps.newHashMap();
    // synchronized
    private static Object object = new Object();

    // 格子 X 个数
    private static Long LATTICE_COUNT_X = new BigDecimal(MAP_X).divide(new BigDecimal(LATTICE_LENGTH)).setScale(0, BigDecimal.ROUND_UP).longValue();
    // 格子 Y 个数
//    private static Long LATTICE_COUNT_Y = new BigDecimal(MAP_Y).divide(new BigDecimal(LATTICE_LENGTH)).setScale(0, BigDecimal.ROUND_UP).longValue();
    // 占用格子长度个数
    private static Long OCCUPY_NUM = (new BigDecimal(CIRCLE_RADIUS - LATTICE_LENGTH / 2).divide(new BigDecimal(LATTICE_LENGTH)).setScale(0, BigDecimal.ROUND_UP).longValue()) * 2 + 1;
    // 占用格子个数
    private static Long OCCUPY_TOTAL_NUM = (long) Math.pow(OCCUPY_NUM, 2);
    
    /**
     * 格子增加玩家
     */
    public static void putLattices(Long lattice, Long player) {
    	synchronized (object) {
        	if (LATTICES.get(lattice) == null) {
        	    Set<Long> set = Sets.newHashSet();
        	    set.add(player);
        		LATTICES.put(lattice, set);
        	}
		}
    }
    
    /**
     * 格子移除玩家
     */
    public static void removeLattices(Long lattice, Long player) {
    	synchronized (object) {
    		LATTICES.get(lattice).remove(player);
    		if (LATTICES.get(lattice).isEmpty()) LATTICES.remove(lattice);
		}
    }
    
    /**
     * 返回格子周围定义区域方块
     */
    public static Set<Long> getAroundLatticeNumber(Long lattice) {
        // 格子中心坐标
        Coordinate circle = getLatticeCenter(lattice);
        // 最左上角坐标
        Coordinate leftTopCenter = new Coordinate(circle.getX() - CIRCLE_RADIUS, circle.getY() - CIRCLE_RADIUS);
        Set<Long> contain = Sets.newHashSet();
        Long a, b, x, y;
        for (int i = 1; i <= OCCUPY_TOTAL_NUM; i++) {
        	a = i % OCCUPY_NUM;
        	b = i / OCCUPY_NUM;
			x = leftTopCenter.getX() + (a == 0 ? (OCCUPY_NUM - 1) : a - 1) * LATTICE_LENGTH;
			y = leftTopCenter.getY() + (a == 0 ? (b - 1) : b) * LATTICE_LENGTH;
			// x,y 为中心点不会等于 0
			if (x > 0 && x <= MAP_X && y > 0 && y <= MAP_Y) {
				if (Math.pow(circle.getX() - x, 2) + Math.pow(circle.getY() - y, 2) <= DISTANCE) {
					contain.add(getLatticeNumber(new Coordinate(x, y)));
				}
			}
		}
        return contain;
    }
    
    /**
     * 格子编号中心坐标
     */
    public static Coordinate getLatticeCenter(Long latticeNumber) {
        Long x = latticeNumber % LATTICE_COUNT_X;
        x = (x == 0 ? LATTICE_COUNT_X : x);
        Long y = (latticeNumber - 1) / LATTICE_COUNT_X;
        return new Coordinate(LATTICE_LENGTH * x - LATTICE_LENGTH / 2, LATTICE_LENGTH * y + LATTICE_LENGTH / 2);
    }
    
    /**
     * 根据坐标获取所在格子编号
     */
    public static Long getLatticeNumber(Coordinate coordinate) {
        Long x = coordinate.getX() / LATTICE_LENGTH + 1;
        Long y = coordinate.getY() / LATTICE_LENGTH + 1;
        return (y - 1) * LATTICE_COUNT_X + x;
    }
    
    /**
     * 初始化一个坐标
     */
    public static Coordinate getInitCoordinate() {
        return new Coordinate(ThreadLocalRandom.current().nextLong(MAP_X), ThreadLocalRandom.current().nextLong(MAP_Y));
    }
}

/**
 * 通信组件
 */
@Data
@AllArgsConstructor
class Connection {
	/**
	 * 玩家
	 */
	private Long player;
	/**
	 * 坐标
	 */
	private Coordinate coordinate;
	/**
	 * 坐标所在格子编号
	 */
	private Long lattice;
    /**
     * 说话
     */
    public void say(String content) {
        System.out.println(String.format("我是 %s, 我在(%s, %s)的位置, 格子编号为: %s。接收到的内容是: %s", this.player, coordinate.getX(), coordinate.getY(), lattice, content));
    }
}

/**
 * 坐标
 */
@Data
@AllArgsConstructor
class Coordinate {
    private Long x;
    private Long y;
}