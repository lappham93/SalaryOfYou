package com.mit.entities.shipping;

import java.math.BigDecimal;

public class ShippingDomesticRate extends ShippingRate {	
	public ShippingDomesticRate() {
		super();
	}

	public ShippingDomesticRate(int id, double weight, int weightUnit,
			BigDecimal rate, int rateType, int status, long createTime,
			long updateTime) {
		super(id, weight, weightUnit, rate, rateType, status, createTime, updateTime);
	}
	
	public ShippingDomesticRate(double weight, int weightUnit,
			BigDecimal rate, int rateType) {
		super(1, weight, weightUnit, rate, rateType, 1);
	}
}
