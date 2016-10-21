package com.mit.entities.social;

public enum FeedContentType {
	PHOTO(1),
	LINK(2),
	MESSAGE(3),
	STICKER(4),
	PRODUCT_SHARE(5),
	VIDEO(6);
	
	private int value;
	
	private FeedContentType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public static FeedType getFeedType(int value) {
		for (FeedType feedType : FeedType.values()) {
			if (feedType.getValue() == value) {
				return feedType;
			}
		}
		return null;
	}
}
