package com.mit.entities.session;


public class UserSession {
	private int userId;
	private int typeId;
	private String imei;

	public UserSession() {}
	
	public UserSession(int userId) {
		super();
		this.userId = userId;
	}

	public UserSession(int userId, int typeId, String imei) {
		super();
		this.userId = userId;
		this.typeId = typeId;
		this.imei = imei;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

}
