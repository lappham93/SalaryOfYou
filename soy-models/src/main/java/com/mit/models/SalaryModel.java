package com.mit.models;

import java.util.HashMap;
import java.util.Map;

import com.mit.dao.salary.JobShareDAO;
import com.mit.entities.salary.JobShare;

public class SalaryModel {
	
	public static SalaryModel Instance = new SalaryModel();
	
	private SalaryModel(){}
	
	public Map<String, Object> shareJob(long jobCategoryId, long jobId, int yearExperience, String skill, String city, String country, String companyCountry,
			double salary) {
		Map<String, Object> rs = new HashMap<>();
		JobShare jobShare = new JobShare();
		jobShare.setJobCategoryId(jobCategoryId);
		jobShare.setJobId(jobId);
		jobShare.setYearExperience(yearExperience);
		jobShare.setSkill(skill);
		jobShare.setSkillLevel(HeuristicModel.Instance.getSkillLevel(jobId, skill));
		jobShare.setCity(city);
		jobShare.setCountry(country);
		jobShare.setCompanyCountry(companyCountry);
		jobShare.setMonthlySalary(salary);
		
		int err = JobShareDAO.getInstance().insert(jobShare);
		rs.put("jobShare", jobShare);
		rs.put("err", err);
		
		return rs;
	}
	
	public Map<String, Object> getStatistics(long shareJobId) {
		Map<String, Object> statistics = new HashMap<>();
		int err = 0;
		JobShare share = JobShareDAO.getInstance().getById(shareJobId);
		if (share != null) {
			double meanSalary = StatisticsModel.Instance.getMeanSal(share.getJobId(), share.getYearExperience(), share.getSkillLevel());
			double jobMeanSalary = StatisticsModel.Instance.getMeanSal(share.getJobId());
			double experienceMeanSalary = StatisticsModel.Instance.getMeanSal(share.getYearExperience());
			double placeMeanSalary = StatisticsModel.Instance.getMeanSal(share.getCity(), share.getCountry());
			statistics.put("allMS", meanSalary);
			statistics.put("jobMS", jobMeanSalary);
			statistics.put("experienceMS", experienceMeanSalary);
			statistics.put("placeMS", placeMeanSalary);
		} else {
			err = -1;
		}
		statistics.put("err", err);
		
		return statistics;
	}
	
}
