package com.mit.entities.user;

public class LicenseAcceptance {
	private long id;
	private long accountId;
	private String requestInfo;
	private boolean isAccepted;
	private long acceptTime;

	public LicenseAcceptance(long id, long accountId, String requestInfo, boolean isAccepted, long acceptTime) {
		this.id = id;
		this.accountId = accountId;
		this.requestInfo = requestInfo;
		this.isAccepted = isAccepted;
		this.acceptTime = acceptTime;
	}

	public LicenseAcceptance(String requestInfo, boolean isAccepted) {
		this.requestInfo = requestInfo;
		this.isAccepted = isAccepted;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getRequestInfo() {
		return requestInfo;
	}

	public void setReqeustInfo(String requestInfo) {
		this.requestInfo = requestInfo;
	}

	public boolean isAccepted() {
		return isAccepted;
	}

	public void setAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	public long getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(long acceptTime) {
		this.acceptTime = acceptTime;
	}
}
