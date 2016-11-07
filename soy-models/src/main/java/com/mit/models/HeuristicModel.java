package com.mit.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mit.dao.salary.SalaryDistributorDAO;
import com.mit.entities.salary.SalaryDistributor;
import com.mit.entities.salary.SkillLevel;
import com.mit.utils.JsonUtils;

public class HeuristicModel {
	public static final HeuristicModel Instance = new HeuristicModel();
	
	private HeuristicModel(){}
	
	public int getSkillLevel(long jobId, String skill) {
		//TODO
		return SkillLevel.JUNIOR.getValue();
	}
	
	public Map<Integer, SalaryDistributor> getSalaryDistributor(List<Double> salary, int level, long id) {
		//find db, if it is not exist or too old then insert or/update (longer than a week is considered too old)
		Map<Integer, SalaryDistributor> rs = null;
		int status = SalaryDistributorDAO.getInstance().getUpdateStatus(id);
		if (status <= 0) {
			rs = new HashMap<>();
			if (salary != null && salary.size() > 0) {
				double min = salary.get(0);
				double max = salary.get(salary.size() - 1);
				if (max < min + level * 5) {
					max = min + level * 5;
				}
				double offset = (max - min) / level;
				for (double eleSal : salary) {
					for (int i = 1; i <= level; i++) {
						if (eleSal < min + i * offset) {
							SalaryDistributor sd = rs.get(i - 1);
							if (sd == null) {
								sd = new SalaryDistributor();
								sd.setMaxRange(min + i * offset);
								sd.setMinRange(min + (i -1) * offset);
								rs.put(i-1, sd);
							}
							sd.addItem(eleSal);
							break;
						}
					}
				}
			}
			if (status == 0) {
				for (SalaryDistributor sal : rs.values()) {
					SalaryDistributorDAO.getInstance().update(sal);
				}
			} else {
				for (SalaryDistributor sal : rs.values()) {
					SalaryDistributorDAO.getInstance().insert(sal);
				}
			}
		} else {
			rs = SalaryDistributorDAO.getInstance().getByStatisticsId(id);
		}
		return rs;
	}
	
	public static void main(String[] args) {
		List<Double> salary = Arrays.asList(5.0, 6.4, 10.2);
		Map<Integer, SalaryDistributor> rs = HeuristicModel.Instance.getSalaryDistributor(salary, 10, 1L);
		System.out.println(JsonUtils.Instance.toJson(rs));
	}
	
}
