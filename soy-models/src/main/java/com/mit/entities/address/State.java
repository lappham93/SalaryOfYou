package com.mit.entities.address;

public class State {
	public static final int ACTIVE = 1;
	
	private String isoCode;
	private String countryIsoCode;
	private String name;
	private int status;
	
	public State() {
		super();
	}	

	public State(String isoCode, String countryIsoCode, String name) {
		super();
		this.isoCode = isoCode;
		this.countryIsoCode = countryIsoCode;
		this.name = name;
		this.status = ACTIVE;
	}

	public State(String isoCode, String countryIsoCode, String name, int status) {
		super();
		this.isoCode = isoCode;
		this.countryIsoCode = countryIsoCode;
		this.name = name;
		this.status = status;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getCountryIsoCode() {
		return countryIsoCode;
	}

	public void setCountryIsoCode(String countryIsoCode) {
		this.countryIsoCode = countryIsoCode;
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
