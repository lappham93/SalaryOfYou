package com.mit.entities.user;


public enum UserProfileType {
	USER, BIZ_INFO;

	public byte getValue() {
		return (byte) (ordinal() + 1);
	}
}
