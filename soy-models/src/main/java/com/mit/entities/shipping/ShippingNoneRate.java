package com.mit.entities.shipping;

import java.math.BigDecimal;

public class ShippingNoneRate extends ShippingRate {		
	public ShippingNoneRate() {
		super(3, 0, 0, BigDecimal.ZERO, RateType.FLAT.getValue(), 1);
	}
}
