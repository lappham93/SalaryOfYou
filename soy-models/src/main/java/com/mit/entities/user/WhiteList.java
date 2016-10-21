package com.mit.entities.user;

/**
 * user who can publish live stream
 * 
 */
public class WhiteList {
	public static final int ACTIVE = 1;
	
	private int userId;
	private int status;
	
	public WhiteList(int userId) {
		super();
		this.userId = userId;
		this.status = ACTIVE;
	}
	
	public WhiteList(int userId, int status) {
		super();
		this.userId = userId;
		this.status = status;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
