package com.mit.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mit.dao.salary.JobShareDAO;
import com.mit.dao.salary.SalaryStatisticsDAO;
import com.mit.entities.salary.SalaryDistributor;
import com.mit.entities.salary.SalaryStatisticsType;

public class StatisticsModel {
	public static final StatisticsModel Instance = new StatisticsModel();
	public static final int DISTRIBUTE_LEVEL = 10;
	
	private StatisticsModel(){}
	
	public  double getMeanSal(Long jobId)  {
		Map<String, Object> params = new HashMap<>();
		params.put("jobId", jobId);
		return SalaryStatisticsDAO.getInstance().getMeanSalaryByAttr(SalaryStatisticsType.JOB.getValue(), params);
	}
	
	public long getDistributeSal(Long jobId, Map<Integer, SalaryDistributor> salDis) {
		Map<String, Object> params = new HashMap<>();
		params.put("jobId", jobId);
		List<Double> salaries = JobShareDAO.getInstance().getAllInType(SalaryStatisticsType.JOB.getValue(), params);
		salDis = HeuristicModel.Instance.getSalaryDistributor(salaries, DISTRIBUTE_LEVEL, 1L);
		long total = salaries != null ? salaries.size() : 0;
		
		return total;
	}
	
	public double getMeanSal(Integer yearExperience) {
		Map<String, Object> params = new HashMap<>();
		params.put("yearExperience", yearExperience);
		return SalaryStatisticsDAO.getInstance().getMeanSalaryByAttr(SalaryStatisticsType.EXPERIENCE.getValue(), params);
	}
	
	public long getDistributeSal(Integer yearExperience, Map<Integer, SalaryDistributor> salDis) {
		Map<String, Object> params = new HashMap<>();
		params.put("yearExperience", yearExperience);
		List<Double> salaries = JobShareDAO.getInstance().getAllInType(SalaryStatisticsType.JOB.getValue(), params);
		salDis = HeuristicModel.Instance.getSalaryDistributor(salaries, DISTRIBUTE_LEVEL, 1L);
		long total = salaries != null ? salaries.size() : 0;
		
		return total;
	}
	
	public double getMeanSal(String city, String country) {
		Map<String, Object> params = new HashMap<>();
		params.put("city", city);
		params.put("country", country);
		return SalaryStatisticsDAO.getInstance().getMeanSalaryByAttr(SalaryStatisticsType.PLACE.getValue(), params);
	}
	
	public long getDistributeSal(String city, String country, Map<Integer, SalaryDistributor> salDis) {
		Map<String, Object> params = new HashMap<>();
		params.put("city", city);
		params.put("country", country);
		List<Double> salaries = JobShareDAO.getInstance().getAllInType(SalaryStatisticsType.JOB.getValue(), params);
		salDis = HeuristicModel.Instance.getSalaryDistributor(salaries, DISTRIBUTE_LEVEL, 1L);
		long total = salaries != null ? salaries.size() : 0;
		
		return total;
	}
	
	public double getMeanSal(long jobId, int yearExperience, int skillLevel) {
		Map<String, Object> params = new HashMap<>();
		params.put("jobId", jobId);
		params.put("yearExperience", yearExperience);
		params.put("skillLevel", skillLevel);
		return SalaryStatisticsDAO.getInstance().getMeanSalaryByAttr(SalaryStatisticsType.ALL.getValue(), params);
	}
	
	public long getDistributeSal(long jobId, int yearExperience, int skillLevel, Map<Integer, SalaryDistributor> salDis) {
		Map<String, Object> params = new HashMap<>();
		params.put("jobId", jobId);
		params.put("yearExperience", yearExperience);
		params.put("skillLevel", skillLevel);
		List<Double> salaries = JobShareDAO.getInstance().getAllInType(SalaryStatisticsType.JOB.getValue(), params);
		salDis = HeuristicModel.Instance.getSalaryDistributor(salaries, DISTRIBUTE_LEVEL, 1L);
		long total = salaries != null ? salaries.size() : 0;
		
		return total;
	}
	
	public static void main(String[] args) {
//		Map<String, Object> rs = StatisticsModel.Instance.getDistributeSal(2);
//		System.out.println(JsonUtils.Instance.toJson(rs));
	}
}
