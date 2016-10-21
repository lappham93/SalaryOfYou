package com.mit.entities.shipping;

public enum RateType {
	FLAT(1), SCALE(2);
	private int value;
	
	private RateType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
