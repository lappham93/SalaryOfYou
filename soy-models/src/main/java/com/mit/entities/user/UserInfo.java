package com.mit.entities.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mit.dao.user.UserStatDAO;
import com.mit.utils.LinkBuilder;


public class UserInfo extends ProfileInfo {
	private String firstName;
	private String lastName;
	private String email;
	private String mobilePhone;
	private String countryCode;
	private String birthday;
	private int gender;
	private long photo;
//	private String licenseCode;
//	private long defaultBillingAddress;
//	private long defaultShippingAddress;
//	private long bcPoint;
//	private long cndPoint;
//	private int cndLevel;
//	private BigDecimal cndPurAmtInPer;
//	private BigDecimal cndBufPurAmtInPer;
//	private long cndExpirationDate;
//	private boolean isPreviewing;

	public UserInfo() {}
	
	public UserInfo(int id) {
		super(id);
	}

	public UserInfo(int id, String firstName, String lastName, int gender, String email, String mobilePhone,
			String countryCode, String birthday) {
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.mobilePhone = mobilePhone;
		this.countryCode = countryCode;
		this.birthday = birthday;
		this.gender = gender;
//		this.licenseCode = licenseCode;
//		this.cndPurAmtInPer = BigDecimal.ZERO;
//		this.cndBufPurAmtInPer = BigDecimal.ZERO;
	}
	
	public UserInfo(int id, String firstName, String lastName, int gender, String email,
			String mobilePhone, String countryCode, String birthday , long photo, byte status, long createTime, long updateTime) {
		super(id, status, createTime, updateTime);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.mobilePhone = mobilePhone;
		this.countryCode = countryCode;
		this.birthday = birthday;
		this.gender = gender;
		this.photo = photo;
//		this.licenseCode = licenseCode;
//		this.defaultBillingAddress = defaultBillingAddress;
//		this.defaultShippingAddress = defaultShippingAddress;
//		this.bcPoint = bcPoint;
//		this.cndPoint = cndPoint;
//		this.cndPurAmtInPer = cndPurAmtInPer;
//		this.isPreviewing = isPreviewing;
	}

//	public UserInfo(int id, String firstName, String lastName, int gender, String email,
//			String mobilePhone, String countryCode, String birthday,
//			long photo, String licenseCode,
//			long defaultBillingAddress, long defaultShippingAddress,
//			long bcPoint, long cndPoint, int cndLevel,
//			BigDecimal cndPurAmtInPer, BigDecimal cndBufPurAmtInPer, long cndExpirationDate, 
//			boolean isPreviewing, byte status, long createTime, long updateTime) {
//		super(id, status, createTime, updateTime);
//		this.firstName = firstName;
//		this.lastName = lastName;
//		this.email = email;
//		this.mobilePhone = mobilePhone;
//		this.countryCode = countryCode;
//		this.birthday = birthday;
//		this.gender = gender;
//		this.photo = photo;
//		this.licenseCode = licenseCode;
//		this.defaultBillingAddress = defaultBillingAddress;
//		this.defaultShippingAddress = defaultShippingAddress;
//		this.bcPoint = bcPoint;
//		this.cndPoint = cndPoint;
//		this.cndLevel = cndLevel;
//		this.cndPurAmtInPer = cndPurAmtInPer;
//		this.cndBufPurAmtInPer = cndBufPurAmtInPer;
//		this.cndExpirationDate = cndExpirationDate;
//		this.isPreviewing = isPreviewing;
//	}
	
	public String getFullName() {
		return firstName + " " + lastName;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

//	public long getDefaultBillingAddress() {
//		return defaultBillingAddress;
//	}
//
//	public void setDefaultBillingAddress(long defaultBillingAddress) {
//		this.defaultBillingAddress = defaultBillingAddress;
//	}
//
//	public long getDefaultShippingAddress() {
//		return defaultShippingAddress;
//	}
//
//	public void setDefaultShippingAddress(long defaultShippingAddress) {
//		this.defaultShippingAddress = defaultShippingAddress;
//	}
//
//	public long getBcPoint() {
//		return bcPoint;
//	}
//
//	public void setBcPoint(long bcPoint) {
//		this.bcPoint = bcPoint;
//	}
//
//	public long getCndPoint() {
//		return cndPoint;
//	}
//
//	public void setCndPoint(long cndPoint) {
//		this.cndPoint = cndPoint;
//	}
//
//	public int getCndLevel() {
//		return cndLevel;
//	}
//
//	public void setCndLevel(int cndLevel) {
//		this.cndLevel = cndLevel;
//	}
//
//	public long getCndExpirationDate() {
//		return cndExpirationDate;
//	}
//
//	public void setCndExpirationDate(long cndExpirationDate) {
//		this.cndExpirationDate = cndExpirationDate;
//	}
//
//	public BigDecimal getCndPurAmtInPer() {
//		return cndPurAmtInPer;
//	}
//
//	public void setCndPurAmtInPer(BigDecimal cndPurAmtInPer) {
//		this.cndPurAmtInPer = cndPurAmtInPer;
//	}
//
//	public BigDecimal getCndBufPurAmtInPer() {
//		return cndBufPurAmtInPer;
//	}
//
//	public void setCndBufPurAmtInPer(BigDecimal cndBufPurAmtInPer) {
//		this.cndBufPurAmtInPer = cndBufPurAmtInPer;
//	}

	public long getPhoto() {
		return photo;
	}

	public void setPhoto(long photo) {
		this.photo = photo;
	}

//	public String getLicenseCode() {
//		return licenseCode;
//	}
//
//	public void setLicenseCode(String licenseCode) {
//		this.licenseCode = licenseCode;
//	}
//
//	public boolean isPreviewing() {
//		return isPreviewing;
//	}
//
//	public void setPreviewing(boolean isPreviewing) {
//		this.isPreviewing = isPreviewing;
//	}

	public BasicInfoBuilder buildPublicInfo() {
		return new BasicInfoBuilder(this);
	}

	public BasicInfoBuilder buildPublicInfo(int viewUserId) {
		return new BasicInfoBuilder(this, this.getId() == viewUserId);
	}
	
	public ReportView buildReportView() {
		return new ReportView(this);
	}

	public static class BasicInfoBuilder {
		private int id;
		private String firstName;
		private String lastName;
		private String email;
		private String mobilePhone;
		private String countryCode;
		private String birthday;
		private int gender;
//		private long bcPoint;
//		private long cndPoint;
		private String photo;
//		private String licenseCode;
//		private long defaultBillingAddress;
//		private long defaultShippingAddress;
//		private List<ShippingAddress.UserView> shippingAddresses;
//		private List<BillingAddress.UserView> billingAddresses;
//		private int cartSize;
//		private boolean isPreviewing;

		public BasicInfoBuilder(UserInfo userInfo) {
			this.id = userInfo.getId();
			this.firstName = userInfo.getFirstName();
			this.lastName = userInfo.getLastName();
			this.email = userInfo.getEmail();
			this.gender = userInfo.getGender();
//			this.bcPoint = userInfo.getBcPoint();
//			this.cndPoint = userInfo.getCndPoint();
			this.photo = LinkBuilder.buildUserAvatarLink(userInfo.getPhoto());
//			this.licenseCode = userInfo.getLicenseCode();
//			this.defaultBillingAddress = userInfo.getDefaultBillingAddress();
//			this.defaultShippingAddress = userInfo.getDefaultShippingAddress();
		}

		public BasicInfoBuilder(UserInfo userInfo, boolean isFull) {
			this(userInfo);
			this.mobilePhone = userInfo.getMobilePhone();
			this.countryCode = userInfo.getCountryCode();
			this.birthday = userInfo.getBirthday();
//			this.isPreviewing = userInfo.isPreviewing();
		}

		public int getId() {
			return id;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public int getGender() {
			return gender;
		}

		public String getMobilePhone() {
			return mobilePhone;
		}

		public String getCountryCode() {
			return countryCode;
		}

		public String getBirthday() {
			return birthday;
		}

//		public long getBcPoint() {
//			return bcPoint;
//		}
//
//		public long getCndPoint() {
//			return cndPoint;
//		}

		public String getPhoto() {
			return photo;
		}

//		public String getLicenseCode() {
//			return licenseCode;
//		}

		public String getEmail() {
			return email;
		}

//		public long getDefaultBillingAddress() {
//			return defaultBillingAddress;
//		}
//
//		public long getDefaultShippingAddress() {
//			return defaultShippingAddress;
//		}
//
//		public List<ShippingAddress.UserView> getShippingAddresses() {
//			return shippingAddresses;
//		}
//
//		public BasicInfoBuilder setShippingAddresses(List<ShippingAddress.UserView> shippingAddresses) {
//			this.shippingAddresses = shippingAddresses;
//			return this;
//		}

//		public List<BillingAddress.UserView> getBillingAddresses() {
//			return billingAddresses;
//		}
//
//		public BasicInfoBuilder setBillingAddresses(List<BillingAddress.UserView> billingAddresses) {
//			this.billingAddresses = billingAddresses;
//			return this;
//		}

//		public int getCartSize() {
//			return cartSize;
//		}
//
//		public BasicInfoBuilder setCartSize(int cartSize) {
//			this.cartSize = cartSize;
//			return this;
//		}
//
//		public boolean isPreviewing() {
//			return isPreviewing;
//		}
	}
	
	public static class ReportView {
		private int id;
		private String firstName;
		private String lastName;
		private String email;
		private String mobilePhone;
		private String countryCode;
		private String birthday;
		private String defaultDiscountCode;
		private String defaultDiscountCodeType;
		private long createTime;

		public ReportView(UserInfo userInfo) {
			this.id = userInfo.getId();
			this.firstName = userInfo.getFirstName();
			this.lastName = userInfo.getLastName();
			this.email = userInfo.getEmail();
			this.defaultDiscountCode = "";
			this.defaultDiscountCodeType = "";
			this.mobilePhone = userInfo.getMobilePhone();
			this.countryCode = userInfo.getCountryCode();
			this.birthday = userInfo.getBirthday();
			this.createTime = userInfo.getCreateTime();
		}

		public int getId() {
			return id;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public String getMobilePhone() {
			return mobilePhone;
		}

		public String getCountryCode() {
			return countryCode;
		}

		public String getBirthday() {
			return birthday;
		}

		public String getEmail() {
			return email;
		}

		public String getDefaultDiscountCode() {
			return defaultDiscountCode;
		}

		public ReportView setDefaultDiscountCode(String defaultDiscountCode) {
			this.defaultDiscountCode = defaultDiscountCode;
			return this;
		}

		public String getDefaultDiscountCodeType() {
			return defaultDiscountCodeType;
		}

		public ReportView setDefaultDiscountCodeType(String defaultDiscountCodeType) {
			this.defaultDiscountCodeType = defaultDiscountCodeType;
			return this;
		}

		public long getCreateTime() {
			return createTime;
		}
	}

	public SocialView buildSocialView() {
		return new SocialView(this);
	}
	
	public SocialView buildSocialView(long viewUserId) {
		return new SocialView(this, viewUserId);
	}
	
	public static List<SocialView> buildListSocialView(List<UserInfo> uis) {
		if (uis == null || uis.isEmpty()) {
			return Arrays.asList();
		}
		List<SocialView> socialViews = new ArrayList<SocialView>();
		for (UserInfo ui : uis) {
			socialViews.add(ui.buildSocialView());
		}
		
		return socialViews;
	}
	
	public static List<SocialView> buildListSocialView(List<UserInfo> uis, long viewUserId) {
		if (uis == null || uis.isEmpty()) {
			return Arrays.asList();
		}
		List<SocialView> socialViews = new ArrayList<SocialView>();
		for (UserInfo ui : uis) {
			socialViews.add(ui.buildSocialView(viewUserId));
		}
		
		return socialViews;
	}
	
	public static class SocialView {
		
		private int id;
		private String userName;
		private String photo;
		private boolean isFollowed;
		
		public SocialView(UserInfo ui) {
			this.id = ui.getId();
			this.photo = LinkBuilder.buildUserAvatarLink(ui.getPhoto());
			if (ui != null) {
				this.userName = ui.getFullName();
			}
		}
		
		public SocialView(UserInfo ui, long viewUserId) {
			this.id = ui.getId();
			this.photo = LinkBuilder.buildUserPhotoLink(ui.getPhoto());
			if (ui != null) {  
				this.userName = ui.getFullName();
			}
			UserStat ff = UserStatDAO.getInstance().getById(viewUserId);
			if (ff != null && ff.getFollowUserIds() != null) {
				isFollowed = ff.getFollowUserIds().contains((long)id);
			}
		}
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getPhoto() {
			return photo;
		}
		public void setPhoto(String photo) {
			this.photo = photo;
		}
		public boolean getIsFollowed() {
			return isFollowed;
		}
		public void setIsFollowed(boolean isFollowed) {
			this.isFollowed = isFollowed;
		}
		
	}
}
