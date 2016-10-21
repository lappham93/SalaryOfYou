package com.mit.entities.react;

public enum CommentType {
	MESSAGE(0),
	PHOTO(1),
	STICKER(4);
	
	private int value;
	
	private CommentType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}
