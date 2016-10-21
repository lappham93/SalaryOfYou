package com.mit.entities.address;

public class City {
	public static final int ACTIVE = 1;

	private String isoCode;
	private String countryIsoCode;
	private String stateIsoCode;
	private String stateName;
	private String name;
	private int status;

	public City() {
		super();
	}

	public City(String isoCode, String countryIsoCode, String stateIsoCode, String stateName, String name) {
		super();
		this.isoCode = isoCode;
		this.countryIsoCode = countryIsoCode;
		this.stateIsoCode = stateIsoCode;
		this.stateName = stateName;
		this.name = name;
		this.status = ACTIVE;
	}

	public City(String isoCode, String countryIsoCode, String stateIsoCode, String stateName, String name, int status) {
		super();
		this.isoCode = isoCode;
		this.countryIsoCode = countryIsoCode;
		this.stateIsoCode = stateIsoCode;
		this.stateName = stateName;
		this.name = name;
		this.status = status;
	}
	
	public CityView buildView() {
		return new CityView(this);
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

	public String getStateIsoCode() {
		return stateIsoCode;
	}

	public void setStateIsoCode(String stateIsoCode) {
		this.stateIsoCode = stateIsoCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
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
	
	public class CityView {
		private String isoCode;
		private String name;
		
		public CityView(City city) {
			isoCode = city.getIsoCode();
			name = city.getName();
		}

		public String getIsoCode() {
			return isoCode;
		}

		public String getName() {
			return name;
		}
		
	}
}
