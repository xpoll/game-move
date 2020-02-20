package cn.blmdz.game.console;

import com.google.common.base.Objects;

public enum Orientation {
	UP(38),
	DOWN(40),
	LEFt(37),
	RIGHT(39),
	SPACE(32),
	ENTER(13),
	;
	int value;
	
	Orientation (int value) {
		this.value = value;
	}
	
	public static Orientation conversion (int value) {
		for (Orientation item : Orientation.values()) {
			if (Objects.equal(value, item.value)) {
				return item;
			}
		}
		return null;
	}
}
