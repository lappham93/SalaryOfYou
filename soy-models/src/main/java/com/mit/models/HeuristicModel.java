package com.mit.models;

import com.mit.entities.salary.SkillLevel;

public class HeuristicModel {
	public static final HeuristicModel Instance = new HeuristicModel();
	
	private HeuristicModel(){}
	
	public int getSkillLevel(long jobId, String skill) {
		//TODO
		return SkillLevel.JUNIOR.getValue();
	}
}
