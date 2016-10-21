package com.mit.entities.session;

import com.mit.entities.user.DiscountCode;
import com.mit.entities.user.UserInfo;


public class LoginInfo {
	private int err;
	private String sessionKey;
	private int userId;
	private UserInfo userInfo;
	private int typeId;
	private int bizId;
	private int roleId;
	private String imei;

	public LoginInfo() {}

	public LoginInfo(int err, String sessionKey, int userId, UserInfo userInfo, byte typeId, int bizId, int roleId, String imei) {
		this.err = err;
		this.sessionKey = sessionKey;
		this.userId = userId;
		this.userInfo = userInfo;
		this.typeId = typeId;
		this.bizId = bizId;
		this.roleId = roleId;
		this.imei = imei;
	}

	public int getErr() {
		return err;
	}

	public void setErr(int err) {
		this.err = err;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
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

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}
}
