package com.mit.entities.message;

public enum MessageStatusType {
	OFFLINE, SENT, RECEIVED, SEEN;

	public byte getValue() {
		return (byte) (ordinal() + 1);
	}
}