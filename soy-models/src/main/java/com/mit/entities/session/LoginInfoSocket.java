/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mit.entities.session;

/**
 *
 * @author nghiatc
 * @since Mar 1, 2016
 */
public class LoginInfoSocket {
    private int err;
	private String sessionKey;
	private int userId;
	private byte roleId;
    private String imei;

	public LoginInfoSocket() {}

	public LoginInfoSocket(int err, String sessionKey, int userId, byte roleId, String imei) {
		super();
		this.err = err;
		this.sessionKey = sessionKey;
		this.userId = userId;
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

	public byte getRoleId() {
		return roleId;
	}

	public void setRoleId(byte roleId) {
		this.roleId = roleId;
	}
    
    public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

    @Override
    public String toString() {
        return "LoginInfoSocket{" + "err=" + err + ", sessionKey=" + sessionKey + ", userId=" + userId + ", roleId=" + roleId + ", imei=" + imei + '}';
    }
    
    
}
