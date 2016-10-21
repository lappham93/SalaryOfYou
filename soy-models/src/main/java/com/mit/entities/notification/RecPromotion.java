package com.mit.entities.notification;


public class RecPromotion {
	private int id;
	private boolean viewed;
	private long createTime;
	
	public RecPromotion() {}
	
	public RecPromotion(int id) {
		this.id = id;
	}
	
	public RecPromotion(int id, boolean viewed, long createTime) {
		this.id = id;
		this.viewed = viewed;
		this.createTime = createTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isViewed() {
		return viewed;
	}

	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}	
}
