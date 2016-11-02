package com.mit.entities.salary;

public class JobSalaryStatistics extends SalaryStatistics{
	public static final int TYPE = SalaryStatisticsType.JOB.getValue();
	
	private long jobId;
	
	public JobSalaryStatistics() {
		super();
	}

	public JobSalaryStatistics(long id, long jobId, double mean, long shareCount, int status, long createTime,
			long updateTime) {
		super(id, TYPE, mean, shareCount, status, createTime, updateTime);
		this.jobId = jobId;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	
}
