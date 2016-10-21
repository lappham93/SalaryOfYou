package com.mit.entities.user;

import java.math.BigDecimal;

public class DiscountCodeType {
	public static final int ACTIVE = 1;
	
	public static final int RETAILER = 1;
	public static final int SALON_BUYER = 2;
	public static final int DISTRIBUTOR = 3;
	
	private int id;
	private String name;
	private BigDecimal commissionRatio;
	private BigDecimal upSalePercent;
	private int status;
	private long createTime;
	private long updateTime;
	
	public DiscountCodeType() {
		super();
	}
	
	public DiscountCodeType(int id, String name, BigDecimal commissionRatio,
			BigDecimal upSalePercent, int status, long createTime, long updateTime) {
		super();
		this.id = id;
		this.name = name;
		this.commissionRatio = commissionRatio;
		this.upSalePercent = upSalePercent;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getCommissionRatio() {
		return commissionRatio;
	}

	public void setCommissionRatio(BigDecimal commissionRatio) {
		this.commissionRatio = commissionRatio;
	}

	public BigDecimal getUpSalePercent() {
		return upSalePercent;
	}

	public void setUpSalePercent(BigDecimal upSalePercent) {
		this.upSalePercent = upSalePercent;
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
