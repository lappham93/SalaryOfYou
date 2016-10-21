package com.mit.entities.shipping;

import java.math.BigDecimal;

public class ShippingInternationalRate extends ShippingRate {
	private String countryIsoCode;
	
	public ShippingInternationalRate() {
		super();
	}

	public ShippingInternationalRate(int id, String countryIsoCode,
			double weight, int weightUnit, BigDecimal rate, int rateType,
			int status, long createTime, long updateTime) {
		super(id, weight, weightUnit, rate, rateType, status, createTime, updateTime);
		this.countryIsoCode = countryIsoCode;
	}
	
	public ShippingInternationalRate(String countryIsoCode,
			double weight, int weightUnit, BigDecimal rate, int rateType) {
		super(2, weight, weightUnit, rate, rateType, 1);
		this.countryIsoCode = countryIsoCode;
	}

	public String getCountryIsoCode() {
		return countryIsoCode;
	}

	public void setCountryIsoCode(String countryIsoCode) {
		this.countryIsoCode = countryIsoCode;
	}
}
