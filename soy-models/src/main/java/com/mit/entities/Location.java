package com.mit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Location {
	private String countryCode;
	private String city;
	private String state;
	private String stateCode;
	private String county = "";
	private String countyCode = "";
	private double lat;
	private double lon;
	private int weight;

	public Location() {}

	public Location(String countryCode, String city,
			String state, String stateCode, String county, String countyCode,
			double lat, double lon) {
		this.countryCode = countryCode;
		this.city = city;
		this.state = state;
		this.stateCode = stateCode;
		this.county = county;
		this.countyCode = countyCode;
		this.lat = lat;
		this.lon = lon;
	}

	public Location(String countryCode, String placeName,
			String state, String stateCode, String county, String countyCode) {
		this.countryCode = countryCode;
		this.city = placeName;
		this.state = state;
		this.stateCode = stateCode;
		this.county = county;
		this.countyCode = countyCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCountyCode() {
		return countyCode;
	}

	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}

	@JsonIgnore
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	@JsonIgnore
	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
	
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public boolean equals(Location location) {
		return getCity().equalsIgnoreCase(location.getCity()) && getState().equalsIgnoreCase(location.getState());
	}
}
