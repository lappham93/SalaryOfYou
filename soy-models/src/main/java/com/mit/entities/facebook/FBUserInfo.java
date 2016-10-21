package com.mit.entities.facebook;

public class FBUserInfo {
	private String id;
	private String firstName;
	private String lastName;
	private String birthday;
	private String avatar;
	private String email;
	private int gender;
	private int interestedInGender;
	
	public FBUserInfo() {}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
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

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getInterestedInGender() {
		return interestedInGender;
	}

	public void setInterestedInGender(int interestedInGender) {
		this.interestedInGender = interestedInGender;
	} 
	
}
