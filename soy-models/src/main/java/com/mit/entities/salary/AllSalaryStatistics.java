package com.mit.entities.salary;

public class AllSalaryStatistics extends SalaryStatistics{
	public static final int TYPE = SalaryStatisticsType.ALL.getValue();
	
	private long jobId;
	private int yearExperience;
	private int skillLevel;
	
	public AllSalaryStatistics() {
		super();
	}

	public AllSalaryStatistics(long id, long jobId, int yearExperience, int skillLevel, double mean, long shareCount, int status, long createTime,
			long updateTime) {
		super(id, TYPE, mean, shareCount, status, createTime, updateTime);
		this.jobId = jobId;
		this.yearExperience = yearExperience;
		this.skillLevel = skillLevel;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public int getYearExperience() {
		return yearExperience;
	}

	public void setYearExperience(int yearExperience) {
		this.yearExperience = yearExperience;
	}

	public int getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}

}
