package com.mit.entities.zipcode;

import java.util.Arrays;
import java.util.List;

public class CountryZipCode {
	
	public static final int ACTIVE = 1;
	public static final int FULL = 0;
	public static final int PREFIX = 1;
	public static final List<String> lstPrefix = Arrays.asList("CA", "IE", "MT");

	private long id;
	private String countryCode;
	private String zipCode;
	private int type;
	private int status;
	
	public CountryZipCode(long id, String countryCode, String zipCode) {
		this.id = id;
		this.countryCode = countryCode;
		this.zipCode = zipCode;
		this.status = ACTIVE;
		this.type = FULL;
	}
	
	public CountryZipCode(long id, String countryCode, String zipCode, int status) {
		this.id = id;
		this.countryCode = countryCode;
		this.zipCode = zipCode;
		this.status = status;
		this.type = FULL;
	}
	
	public CountryZipCode(long id, String countryCode, String zipCode, int status, int type) {
		this.id = id;
		this.countryCode = countryCode;
		this.zipCode = zipCode;
		this.status = status;
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setPreFixType() {
		this.type = PREFIX;
	}
	
	
}
