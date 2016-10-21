package com.mit.constants;

public enum BizRoleType {
	BIZ_OWNER(1), BIZ_MANAGER(2);
	
	private int value;
	
	private BizRoleType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
