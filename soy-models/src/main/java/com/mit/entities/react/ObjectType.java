package com.mit.entities.react;

public enum ObjectType {
	EVENT(4),
	BOOTH(1),
	PRODUCT(2),
	COMMENT(3),
	FEED(5),
	SUBFEED(6),
	USER(7);
	
	private int value;
	
	private ObjectType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static ObjectType getType(int value) {
		for (ObjectType type : ObjectType.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		
		return null;
	}
}
