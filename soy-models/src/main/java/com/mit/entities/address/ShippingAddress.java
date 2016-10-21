package com.mit.entities.address;

public class ShippingAddress {
	public static final int ACTIVE = 1;
	
	private long id;
	private int userId;
	private String firstName;
	private String lastName;
	private String businessName;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String country;
	private String zipCode;
	private String phone;
	private String countryCode;
	private int status;
	private long createTime;
	private long updateTime;
	
	public ShippingAddress() {
		super();
	}

	public ShippingAddress(long id, int userId, String firstName,
			String lastName, String businessName, String addressLine1, String addressLine2,
			String city, String state, String country, String zipCode,
			String phone, String countryCode, int status, long createTime, long updateTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.businessName = businessName;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zipCode = zipCode;
		this.phone = phone;
		this.countryCode = countryCode;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}	
	
	public UserView buildUserView() {
		return new UserView(this);
	}
	
	public static class UserView {
		private long id;
		private String firstName;
		private String lastName;
		private String businessName;
		private String addressLine1;
		private String addressLine2;
		private String city;
		private String state;
		private String country;
		private String zipCode;
		private String phone;
		private String countryCode;
		private int status;
		private long createTime;
		private long updateTime;
	
		private UserView(ShippingAddress shippingAddress) {
			super();
			this.id = shippingAddress.getId();
			this.firstName = shippingAddress.getFirstName();
			this.lastName = shippingAddress.getLastName();
			this.businessName = shippingAddress.getBusinessName();
			this.addressLine1 = shippingAddress.getAddressLine1();
			this.addressLine2 = shippingAddress.getAddressLine2();
			this.city = shippingAddress.getCity();
			this.state = shippingAddress.getState();
			this.country = shippingAddress.getCountry();
			this.zipCode = shippingAddress.getZipCode();
			this.phone = shippingAddress.getPhone();
			this.countryCode = shippingAddress.getCountryCode();
			this.status = shippingAddress.getStatus();
			this.createTime = shippingAddress.getCreateTime();
			this.updateTime = shippingAddress.getUpdateTime();
		}

		public long getId() {
			return id;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public String getBusinessName() {
			return businessName;
		}

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

		public int getStatus() {
			return status;
		}

		public long getCreateTime() {
			return createTime;
		}

		public long getUpdateTime() {
			return updateTime;
		}
	}
}
