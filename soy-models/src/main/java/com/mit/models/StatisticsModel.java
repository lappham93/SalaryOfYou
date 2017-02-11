package com.mit.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mit.dao.salary.JobShareDAO;
import com.mit.dao.salary.SalaryStatisticsDAO;
import com.mit.entities.salary.AllSalaryStatistics;
import com.mit.entities.salary.ExperienceSalaryStatistics;
import com.mit.entities.salary.JobSalaryStatistics;
import com.mit.entities.salary.JobShare;
import com.mit.entities.salary.PlaceSalaryStatistics;
import com.mit.entities.salary.SalaryDistributor;
import com.mit.entities.salary.SalaryStatistics;
import com.mit.entities.salary.SalaryStatisticsType;

public class StatisticsModel {
	public static final StatisticsModel Instance = new StatisticsModel();
	public static final int DISTRIBUTE_LEVEL = 10;
	
	private StatisticsModel(){}
	
	public  Map<Integer, Double> getMeanSal(JobShare jobShare) {
		Map<Integer, Double> result = new HashMap<>();
		for (SalaryStatisticsType stype : SalaryStatisticsType.values()) {
			int type = stype.getValue();
			result.put(type, SalaryStatisticsDAO.getInstance().getMeanSalaryByAttr(type, SalaryStatisticsDAO.buildParams(type, jobShare)));
		}
		return result;
	}
	
	public void updateSalaryStatistic(JobShare jobShare) {
		for (SalaryStatisticsType stype : SalaryStatisticsType.values()) {
			int type = stype.getValue();
			SalaryStatistics ss = SalaryStatisticsDAO.getInstance().getByAttr(type, SalaryStatisticsDAO.buildParams(type, jobShare));
			if (ss != null) {
				ss.updateStatistics(jobShare.getMonthlySalary());
				SalaryStatisticsDAO.getInstance().update(ss);
			} else {
				long now = System.currentTimeMillis();
				if (type == SalaryStatisticsType.ALL.getValue()) {
					ss = new AllSalaryStatistics(0L, jobShare.getJobId(), jobShare.getYearExperience(), jobShare.getSkillLevel(), 
							jobShare.getMonthlySalary(), 1L, 1, now, now);
				} else if (type == SalaryStatisticsType.EXPERIENCE.getValue()) {
					ss = new ExperienceSalaryStatistics(0L, jobShare.getYearExperience(), jobShare.getMonthlySalary(), 1L, 1, now, now);
				} else if (type == SalaryStatisticsType.JOB.getValue()) {
					ss = new JobSalaryStatistics(0L, jobShare.getJobId(), jobShare.getMonthlySalary(), 1L, 1, now, now);
				} else if (type == SalaryStatisticsType.PLACE.getValue()) {
					ss = new PlaceSalaryStatistics(0L, jobShare.getCity(), jobShare.getCountry(), jobShare.getMonthlySalary(), 1L, 1, now, now);
				}
				if (ss != null) {
					SalaryStatisticsDAO.getInstance().insert(ss);
				}
			}
		}
	}
	
	public Map<Integer, Map<Integer, SalaryDistributor>> getDistributeSal(JobShare jobShare) {
		Map<Integer, Map<Integer, SalaryDistributor>> salDis = new HashMap<>();
		for (SalaryStatisticsType stype : SalaryStatisticsType.values()) {
			Map<String, Object> params = SalaryStatisticsDAO.buildParams(stype.getValue(), jobShare);
			List<Double> salaries = JobShareDAO.getInstance().getAllInType(stype.getValue(), params);
			SalaryStatistics ss = SalaryStatisticsDAO.getInstance().getByAttr(stype.getValue(), params);
			Map<Integer, SalaryDistributor> sal = HeuristicModel.Instance.getSalaryDistributor(salaries, DISTRIBUTE_LEVEL, ss.getId());
			salDis.put(stype.getValue(), sal);
		}
		
		return salDis;
	}
	
	public static void main(String[] args) {
	}
}
