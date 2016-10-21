package com.mit.entities.social;

public enum FeedType {
	SOCIAL(1),
	COMMUNITY(2),
	HIRING(3),
	LIVE(4);
	
	private int value;
	
	FeedType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public FeedType getFeedType(int value) {
		for (FeedType ft : FeedType.values()) {
			if (ft.getValue() == value) {
				return ft;
			}
		}
		return null;
	}
	
}
