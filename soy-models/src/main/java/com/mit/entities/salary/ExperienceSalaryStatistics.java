package com.mit.entities.salary;

public class ExperienceSalaryStatistics extends SalaryStatistics{
	public static final int TYPE = SalaryStatisticsType.EXPERIENCE.getValue();
	
	private int yearExperience;
	
	public ExperienceSalaryStatistics() {
		super();
	}

	public ExperienceSalaryStatistics(long id, int yearExperience, double mean, long shareCount, int status, long createTime,
			long updateTime) {
		super(id, TYPE, mean, shareCount, status, createTime, updateTime);
		this.yearExperience = yearExperience;
	}

	public int getYearExperience() {
		return yearExperience;
	}

	public void setYearExperience(int yearExperience) {
		this.yearExperience = yearExperience;
	}	
	
}
