package com.mit.constants;

import java.math.BigDecimal;

import org.apache.commons.lang.math.NumberUtils;

public class AppConstant {
	private String key;
	private String value;
	private String desc;
	private long createTime;
	private long updateTime;
	
	public AppConstant() {
		super();
	}
	
	public AppConstant(String key, String value, String desc, 
			long createTime, long updateTime) {
		super();
		this.key = key;
		this.value = value;
		this.desc = desc;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}
	
	public long getLongValue() {
		return NumberUtils.toLong(value);
	}
	
	public int getIntValue() {
		return NumberUtils.toInt(value);
	}
	
	public BigDecimal getBigDecimalValue() {
		return new BigDecimal(value);
	}

	public void setValue(String value) {
		this.value = value;
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
