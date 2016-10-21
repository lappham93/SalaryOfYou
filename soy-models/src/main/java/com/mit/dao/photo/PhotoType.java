package com.mit.dao.photo;

public enum PhotoType {
	BANNER(1), EVENT(2), BOOTH(3), PRODUCT(4), USER(5), MAP(6), FEED(7), COMMENT(8), MODEL3D(9);
	
	int value;
	
	private PhotoType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
