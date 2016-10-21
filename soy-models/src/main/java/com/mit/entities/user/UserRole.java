package com.mit.entities.user;

import com.fasterxml.jackson.annotation.JsonRawValue;

public class UserRole {
	private short id;
	private String name;
	private String functions;
	private byte status;
	private long createTime;
	private long updateTime;

	public UserRole() {}

	public UserRole(short id, String name, String functions, byte status, long createTime, long updateTime) {
		this.id = id;
		this.name = name;
		this.functions = functions;
		this.status = status;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	@JsonRawValue
	public String getFunctions() {
		return functions;
	}

	public void setFunctions(String functions) {
		this.functions = functions;
	}

	public byte getStatus() {
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
