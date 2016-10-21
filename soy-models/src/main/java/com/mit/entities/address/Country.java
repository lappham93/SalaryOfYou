package com.mit.entities.address;

public class Country {
	public static final int ACTIVE = 1;
	
	private String isoCode;
	private String name;
	private int status;
	
	public Country() {
		super();
	}	

	public Country(String isoCode, String name) {
		super();
		this.isoCode = isoCode;
		this.name = name;
		this.status = ACTIVE;
	}

	public Country(String isoCode, String name, int status) {
		super();
		this.isoCode = isoCode;
		this.name = name;
		this.status = status;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}	
	
}
