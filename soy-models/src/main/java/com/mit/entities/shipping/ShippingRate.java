package com.mit.entities.shipping;

import java.math.BigDecimal;

public abstract class ShippingRate {
	private int id;
	private double weight;
	private int weightUnit;
	private BigDecimal rate;
	private int rateType;
	private int status;
	private long createTime;
	private long updateTime;
	
	public ShippingRate() {
		super();
	}

	public ShippingRate(int id, double weight, int weightUnit,
			BigDecimal rate, int rateType, int status, long createTime,
			long updateTime) {
		super();
		this.id = id;
		this.weight = weight;
		this.weightUnit = weightUnit;
		this.rate = rate;
		this.rateType = rateType;
		this.status = status;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	
	public ShippingRate(int id, double weight, int weightUnit,
			BigDecimal rate, int rateType, int status) {
		super();
		this.id = id;
		this.weight = weight;
		this.weightUnit = weightUnit;
		this.rate = rate;
		this.rateType = rateType;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(int weightUnit) {
		this.weightUnit = weightUnit;
	}

	public int getRateType() {
		return rateType;
	}

	public void setRateType(int rateType) {
		this.rateType = rateType;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
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
