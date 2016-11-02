package com.mit.entities.salary;

public enum SkillLevel {
	JUNIOR(1, 1, "Junior"), SENIOR(1, 2, "Senior"), MASTER(1, 3, "Master");
	
	private int type;
	private int value;
	private String name;
	
	private SkillLevel(int type, int value, String name) {
		this.type = type;
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public SkillLevel getSkillLevel(int type, int value) {
		for (SkillLevel level : SkillLevel.values()) {
			if (level.type == type && level.value == value) {
				return level;
			}
		}
		return null;
	}
}
