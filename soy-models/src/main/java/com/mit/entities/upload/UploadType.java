package com.mit.entities.upload;

public enum UploadType {
	AVATAR, CHAT, ROOM_PHOTO;

	public byte getValue() {
		return (byte) (ordinal() + 1);
	}
}
