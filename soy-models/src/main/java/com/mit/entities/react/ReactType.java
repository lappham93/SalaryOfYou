package com.mit.entities.react;

public enum ReactType {
	COMMENT(1), LIKE(2), VIEW(3), LIVE(4), RATING(5), SHARE(6), FOLLOW(7);
	
	private int value;
	
	private ReactType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static ReactType getType(int value) {
		for (ReactType type : ReactType.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		
		return null;
	}

}
