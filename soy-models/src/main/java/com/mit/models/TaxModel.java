package com.mit.models;

import java.math.BigDecimal;

public class TaxModel {
	private final BigDecimal TAX_PERCENT = new BigDecimal(0.06);
	public static final TaxModel Instance = new TaxModel();
	
	private TaxModel() {}
	
	public BigDecimal getTax(BigDecimal subTotal, String state, String country) {
		if (country.equalsIgnoreCase("US") && state.equalsIgnoreCase("Florida")) {
			return subTotal.multiply(TAX_PERCENT).setScale(2, BigDecimal.ROUND_UP);
		}
		
		return BigDecimal.ZERO;
	}
}
