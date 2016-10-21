package com.mit.entities.user;

public class LicenseCode {
	public static final int ACTIVE = 1;
	public static final int INACTIVE = 0;
	
	private String code;
	private String alternativeCode;
	private String licensee;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String country;
	private String zipCode;
	private String countryCode;
	private long licenseDate;
	private long effectiveDate;
	private long expirationDate;
	private int status;
	private long createTime;
	private long updateTime;

	public LicenseCode(String code, String alternativeCode, String licensee,
			String addressLine1, String addressLine2, String city,
			String state, String country, String zipCode, String countryCode,
			long licenseDate, long effectiveDate, long expirationDate,
			int status, long createTime, long updateTime) {
		super();
		this.code = code;
		this.alternativeCode = alternativeCode;
		this.licensee = licensee;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zipCode = zipCode;
		this.countryCode = countryCode;
		this.licenseDate = licenseDate;
		this.effectiveDate = effectiveDate;
		this.expirationDate = expirationDate;
		this.status = status;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAlternativeCode() {
		return alternativeCode;
	}

	public void setAlternativeCode(String alternativeCode) {
		this.alternativeCode = alternativeCode;
	}

	public String getLicensee() {
		return licensee;
	}

	public void setLicensee(String licensee) {
		this.licensee = licensee;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public long getLicenseDate() {
		return licenseDate;
	}

	public void setLicenseDate(long licenseDate) {
		this.licenseDate = licenseDate;
	}

	public long getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(long effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public long getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(long expirationDate) {
		this.expirationDate = expirationDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
}
