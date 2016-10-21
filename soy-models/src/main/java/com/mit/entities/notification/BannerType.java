package com.mit.entities.notification;

public enum BannerType {
	WEB(1), EVENT(2), BOOTH(3), PRODUCT(5), VIDEO(6), WELCOME(4);
	
	private int value;
	
	private BannerType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
