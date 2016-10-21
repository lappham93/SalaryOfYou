package com.mit.entities.user;

public enum NotificationType {
	SILENT, VIBRATE, SOUND;

	public byte getValue() {
		return (byte) (ordinal());
	}
}
