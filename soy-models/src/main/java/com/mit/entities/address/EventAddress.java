package com.mit.entities.address;

public class EventAddress {
	public static final int ACTIVE = 1;

	private long id;
	private String name;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String country;
	private String zipCode;
	private String phone;
	private String countryCode;
	private double lon;
	private double lat;
	private int status;
	private long createTime;
	private long updateTime;

	public EventAddress() {
		super();
	}

	public EventAddress(long id, String name, String addressLine1, String addressLine2, String city, String state,
			String country, String zipCode, String phone, String countryCode, double lon, double lat, int status,
			long createTime, long updateTime) {
		super();
		this.id = id;
		this.name = name;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zipCode = zipCode;
		this.phone = phone;
		this.countryCode = countryCode;
		this.lon = lon;
		this.lat = lat;
		this.status = status;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhone() {
		return phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
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

	public UserView buildUserView() {
		return new UserView(this);
	}

	public static class UserView {
//		private long id;
//		private String name;
		private String addressLine1;
		private String addressLine2;
		private String city;
		private String state;
		private String country;
		private String zipCode;
		private String phone;
		private String countryCode;
		private double lon;
		private double lat;

		private UserView(EventAddress billingAddress) {
			super();
//			this.id = billingAddress.getId();
//			this.name = billingAddress.getName();
			this.addressLine1 = billingAddress.getAddressLine1();
			this.addressLine2 = billingAddress.getAddressLine2();
			this.city = billingAddress.getCity();
			this.state = billingAddress.getState();
			this.country = billingAddress.getCountry();
			this.zipCode = billingAddress.getZipCode();
			this.phone = billingAddress.getPhone();
			this.countryCode = billingAddress.getCountryCode();
			this.lon = billingAddress.getLon();
			this.lat = billingAddress.getLat();
		}

//		public String getName() {
//			return name;
//		}

		public double getLon() {
			return lon;
		}

		public double getLat() {
			return lat;
		}

//		public long getId() {
//			return id;
//		}

		public String getAddressLine1() {
			return addressLine1;
		}

		public String getAddressLine2() {
			return addressLine2;
		}

		public String getCity() {
			return city;
		}

		public String getState() {
			return state;
		}

		public String getCountry() {
			return country;
		}

		public String getZipCode() {
			return zipCode;
		}

		public String getPhone() {
			return phone;
		}

		public String getCountryCode() {
			return countryCode;
		}
	}
}
