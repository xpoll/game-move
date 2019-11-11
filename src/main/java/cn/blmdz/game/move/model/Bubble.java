package cn.blmdz.game.move.model;

import lombok.Data;

/**
 *
 */
@Data
public class Bubble {
	private Long id;
	private Integer x;
	private Integer y;
	private Integer radius;
	private String name;
//	private boolean self;
	private Double distance;
}
