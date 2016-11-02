package com.mit.entities.salary;

public abstract class SalaryStatistics {
	private long id;
	private int type;
	private double mean;
	private long shareCount;
	private int status;
	private long createTime;
	private long updateTime;
	
	public SalaryStatistics() {
		super();
	}

	public SalaryStatistics(long id, int type, double mean, long shareCount, int status, long createTime, long updateTime) {
		super();
		this.id = id;
		this.type = type;
		this.mean = mean;
		this.shareCount = shareCount;
		this.status = status;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public long getShareCount() {
		return shareCount;
	}

	public void setShareCount(long shareCount) {
		this.shareCount = shareCount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
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
