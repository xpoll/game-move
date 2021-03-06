package cn.blmdz.game.move.enums;

import java.util.Objects;

public enum MessageType {

	F_REGISTER(1, "注册位置点"),
	F_MOVE(2, "位置移动"),
	
	B_DISTANCE(101, "位置返回"),
    ;
    
	
	private final int value;
	
	private final String description;

    public final int value() {
        return value;
    }

    public final String description() {
        return description;
    }
	
	MessageType(int value, String description) {
		this.value = value;
		this.description = description;
	}
	
	public static MessageType conversion(int value) {
		for (MessageType item : MessageType.values()) {
			if (Objects.equals(value, item.value)) return item;
		}
		return null;
	}
}
