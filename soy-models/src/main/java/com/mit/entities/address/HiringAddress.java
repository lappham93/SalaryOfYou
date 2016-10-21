package com.mit.entities.address;

import java.util.ArrayList;
import java.util.List;

public class HiringAddress {
	
	private long id;
	private String idRef;
	private String name;
	private String address;
	private String city;
	private String state;
	private String country;
	private String zipCode;
	private String phone;
	private double lon;
	private double lat;
	private boolean isVerified;
	private boolean isHiring;
	
	public HiringAddress(){}
	
	public HiringAddress(long id, String idRef, String name, String address, String city, String state, String country, String zipCode,
			String phone, double lon, double lat, boolean isVerified, boolean isHiring) {
		super();
		this.id = id;
		this.idRef = idRef;
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zipCode = zipCode;
		this.phone = phone;
		this.lon = lon;
		this.lat = lat;
		this.isVerified = isVerified;
		this.isHiring = isHiring;
	}
	
	public AddressView buildAddView() {
		return new AddressView(this);
	}
	
	public static List<AddressView> buildListAddView(List<HiringAddress> adds) {
		List<AddressView> addV = new ArrayList<>();
		if (adds != null && !adds.isEmpty()) {
			for (HiringAddress add: adds) {
				addV.add(add.buildAddView());
			}
		}
		return addV;
	}
	
//	public HiringAddressView buildHiringAddView(Feed feed) {
//		return new HiringAddressView(this, feed);
//	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getIdRef() {
		return idRef;
	}
	public void setIdRef(String idRef) {
		this.idRef = idRef;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public boolean getIsVerified() {
		return isVerified;
	}
	public void setIsVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}
	public boolean getIsHiring() {
		return this.isHiring;
	}
	public void setIsHiring(boolean isHiring) {
		this.isHiring = isHiring;
	}
	
	public class AddressView {
		private long id;
		private String name;
		private String address;
		private String city;
		private String state;
		private String country;
		private String zipCode;
		private String phone;
		
		public AddressView(HiringAddress add) {
			id = add.getId();
			name = add.getName();
			address = add.getAddress();
			city = add.getCity();
			state = add.getState();
			country = add.getCountry();
			zipCode = add.getZipCode();
			phone = add.getPhone();
		}

		public long getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getAddress() {
			return address;
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
		
	}
	
}
