package com.mit.entities.salary;

public class JobCategory {
	private long id;
	private String name;
	private String desc;
	private long createTime;
	private long updateTime;
	
	public JobCategory() {
		super();
	}

	public JobCategory(long id, String name, String desc, long createTime, long updateTime) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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
