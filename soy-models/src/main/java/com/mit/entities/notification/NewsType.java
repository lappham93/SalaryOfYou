package com.mit.entities.notification;

public enum NewsType {
	WEB(1), PRODUCT(2), EVENT(3), BOOTH(4), ORDER(9), WELCOME(10), SOCIAL(5), HIRING(6), COMMUNITY(7), CND_LEVEL(8);
	
	private int value;
	
	private NewsType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
