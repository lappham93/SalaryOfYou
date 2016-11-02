package com.mit.entities.salary;

public class PlaceSalaryStatistics extends SalaryStatistics{
	public static final int TYPE = SalaryStatisticsType.PLACE.getValue();
	
	private String city;
	private String country;
	
	public PlaceSalaryStatistics() {
		super();
	}

	public PlaceSalaryStatistics(long id, String city, String country, double mean, long shareCount, int status, long createTime,
			long updateTime) {
		super(id, TYPE, mean, shareCount, status, createTime, updateTime);
		this.city = city;
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
