package com.mit.entities.user;

public class DeviceToken {

	private int accountId;
	private int appId;
	private int deviceId;
	private String imei;
	private String deviceToken;

	public DeviceToken() {}

	public DeviceToken(int accountId, int appId, int deviceId, String imei,
			String deviceToken) {
		this.accountId = accountId;
		this.appId = appId;
		this.deviceId = deviceId;
		this.imei = imei;
		this.deviceToken = deviceToken;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
}
