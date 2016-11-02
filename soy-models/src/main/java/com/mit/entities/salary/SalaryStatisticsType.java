package com.mit.entities.salary;

public enum SalaryStatisticsType {
	ALL(0), JOB(1), EXPERIENCE(2), PLACE(3);
	
	private int value;
	
	private SalaryStatisticsType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

}
