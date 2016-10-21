package com.mit.models;

import com.mit.entities.session.LoginInfo;

public interface IAuthenticate {

	public LoginInfo login(int appId, String userName, String password, String imei, boolean autoLogin);
	public LoginInfo logged(String sessionKey);
	public LoginInfo isAccessable(String sessionKey, short funtionId);
	public int isAccessable(int uid, short functionId);
	public boolean logout(int appId, String sessionKey);
	public boolean logout(int appId, int uid, String imei);
	public boolean logoutAll(int appId, int uid);
	public boolean logoutAll(int uid);
}
