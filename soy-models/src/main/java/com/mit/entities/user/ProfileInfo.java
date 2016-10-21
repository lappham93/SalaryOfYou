package com.mit.entities.user;


public abstract class ProfileInfo {
	private int id;
	private byte type = UserProfileType.USER.getValue();
	private byte status;
	private long createTime;
	private long updateTime;

	public ProfileInfo() {}

	public ProfileInfo(int id) {
		this.id = id;
		this.status = 1;
	}

	public ProfileInfo(int id, byte status, long createTime, long updateTime) {
		this.id = id;
		this.status = status;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

}
