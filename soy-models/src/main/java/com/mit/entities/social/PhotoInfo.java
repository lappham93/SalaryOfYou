package com.mit.entities.social;

public class PhotoInfo {
	public enum PhotoInfoType {
		FEED(1);
		
		private int value;
		
		private PhotoInfoType(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	private long id;
	private long photoId;
	private int width;
	private int height;
	private int type;
	
	public PhotoInfo() {
		super();
	}
	
	public PhotoInfo(long id, long photoId, int width, int height, int type) {
		super();
		this.id = id;
		this.photoId = photoId;
		this.width = width;
		this.height = height;
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(long photoId) {
		this.photoId = photoId;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
