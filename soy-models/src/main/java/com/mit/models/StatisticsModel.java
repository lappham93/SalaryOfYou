package com.mit.models;

import com.mit.dao.salary.SalaryStatisticsDAO;

public class StatisticsModel {
	public static final StatisticsModel Instance = new StatisticsModel();
	
	private StatisticsModel(){}
	
	public  double getMeanSal(long jobId)  {
		
	}
	
	public double getMeanSal(int yearExperience) {
		
	}
	
	public double getMeanSal(String city, String country) {
		
	}
	
	public double getMeanSal(long jobId, int yearExperience, int skillLevel) {
		
	}
}
