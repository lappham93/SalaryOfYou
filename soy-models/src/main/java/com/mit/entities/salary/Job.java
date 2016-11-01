package com.mit.entities.salary;

public class Job {
	private long id;
	private long jobCategoryId;
	private String name;
	private long createTime;
	private long updateTime;
	
	public Job() {
		super();
	}

	public Job(long id, long jobCategoryId, String name, long createTime, long updateTime) {
		super();
		this.id = id;
		this.jobCategoryId = jobCategoryId;
		this.name = name;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getJobCategoryId() {
		return jobCategoryId;
	}

	public void setJobCategoryId(long jobCategoryId) {
		this.jobCategoryId = jobCategoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
