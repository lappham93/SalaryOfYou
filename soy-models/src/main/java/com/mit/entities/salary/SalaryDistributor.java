package com.mit.entities.salary;

public class SalaryDistributor {
	private long id;
	private long salaryStatisticsId;
	private int type;
	private int level;
	private double maxRange;
	private double minRange;
	private double max;
	private double min;
	private double mean;
	private long eleCount;
	private long createTime;
	private long updateTime;
	
	public SalaryDistributor() {
		super();
		this.min = Double.MAX_VALUE;
		this.max = Double.MIN_VALUE;
		this.eleCount = 0;
	}

	public SalaryDistributor(long id, long salaryStatisticsId, int type, int level, double maxRange, double minRange, double max, double min, double mean, long eleCount,
			long createTime, long updateTime) {
		super();
		this.id = id;
		this.salaryStatisticsId = salaryStatisticsId;
		this.type = type;
		this.level = level;
		this.maxRange = maxRange;
		this.minRange = minRange;
		this.max = max;
		this.min = min;
		this.mean = mean;
		this.eleCount = eleCount;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	
	public void addItem(double ele) {
		if (ele < this.min) {
			this.min = ele;
		}
		if (ele > this.max) {
			this.max = ele;
		}
		this.mean = (this.mean * this.eleCount + ele) / (this.eleCount + 1);
		this.eleCount ++;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSalaryStatisticsId() {
		return salaryStatisticsId;
	}

	public void setSalaryStatisticsId(long salaryStatisticsId) {
		this.salaryStatisticsId = salaryStatisticsId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public double getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(double maxRange) {
		this.maxRange = maxRange;
	}

	public double getMinRange() {
		return minRange;
	}

	public void setMinRange(double minRange) {
		this.minRange = minRange;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public long getEleCount() {
		return eleCount;
	}

	public void setEleCount(long eleCount) {
		this.eleCount = eleCount;
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
