package com.mit.entities.facebook;

public class FBUser {
	private int userId;
	private String facebookId;
	private long createTime;
	
	public FBUser() {}
	
	public FBUser(int userId, String facebookId) {
		this.userId = userId;
		this.facebookId = facebookId;
	}
	
	public FBUser(int userId, String facebookId, long createTime) {
		this.userId = userId;
		this.facebookId = facebookId;
		this.createTime = createTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
