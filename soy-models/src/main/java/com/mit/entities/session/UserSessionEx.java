package com.mit.entities.session;


public class UserSessionEx extends UserSession {
	private int bizId;
	private int roleId;

	public UserSessionEx() {}
	
	public UserSessionEx(int userId) {
		super(userId);
	}

	public UserSessionEx(int userId, int typeId, int bizId, int roleId, String imei) {
		super(userId, typeId, imei);
		this.bizId = bizId;
		this.roleId = roleId;
	}

	public int getBizId() {
		return bizId;
	}

	public void setBizId(int bizId) {
		this.bizId = bizId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}
