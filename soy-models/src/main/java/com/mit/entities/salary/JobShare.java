package com.mit.entities.salary;

public class JobShare {
	private long id;
	private long jobCategoryId;
	private long jobId;
	private int yearExperience;
	private String skill;
	private int skillLevel;
	private int certificate;
	private String city;
	private String country;
	private String companyCountry;
	private double monthlySalary;
	private int status;
	private long createTime;
	private long updateTime;
	
	public JobShare() {
		super();
	}

	public JobShare(long id, long jobCategoryId, long jobId, int yearExperience, String skill, int skillLevel, int certificate,
			String city, String country, String companyCountry, double monthlySalary, int status, long createTime, long updateTime) {
		super();
		this.id = id;
		this.jobCategoryId = jobCategoryId;
		this.jobId = jobId;
		this.yearExperience = yearExperience;
		this.skill = skill;
		this.skillLevel = skillLevel;
		this.certificate = certificate;
		this.city = city;
		this.country = country;
		this.companyCountry = companyCountry;
		this.monthlySalary = monthlySalary;
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

	public long getJobCategoryId() {
		return jobCategoryId;
	}

	public void setJobCategoryId(long jobCategoryId) {
		this.jobCategoryId = jobCategoryId;
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

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public int getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}

	public int getCertificate() {
		return certificate;
	}

	public void setCertificate(int certificate) {
		this.certificate = certificate;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCompanyCountry() {
		return companyCountry;
	}

	public void setCompanyCountry(String companyCountry) {
		this.companyCountry = companyCountry;
	}

	public double getMonthlySalary() {
		return monthlySalary;
	}

	public void setMonthlySalary(double monthlySalary) {
		this.monthlySalary = monthlySalary;
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
