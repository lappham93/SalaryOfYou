package com.mit.entities.notification;

public enum BannerOwnType {
	HOME(1), EVENT(2), BOOTH(3);
	
	private int value;
	
	private BannerOwnType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
