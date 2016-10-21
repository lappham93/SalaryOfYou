package com.mit.entities.user;

public class UserAccount {
	public static enum UserStatus {
		DELETE(0, "Delete"),
		ACTIVE(1, "Active"),
		FORCHANGEPASS(2, "ForChangePass"),
		WAITING_APPROVAL(3, "WaitingApproval"),
		NOT_VERIFY(4, "NotVerify"),
		DECLINED(5, "Declined");
		
		private int value;
		private String name;
		
		private UserStatus(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public int getValue() {
			return this.value;
		}
		
		public String getName() {
			return this.name;
		}
		
		public static UserStatus getUserStatus(int value) {
			for (UserStatus us : UserStatus.values()) {
				if (us.getValue() == value) {
					return us;
				}
			}
			
			return null;
		}
	}
	
	public static enum LoginType {
		EMAIL(1),
		GOOGLE(2),
		FACEBOOK(3);
		
		private int value;
		
		private LoginType(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static LoginType getLoginType(int value) {
			for (LoginType type : LoginType.values()) {
				if (type.getValue() == value) {
					return type;
				}
			}
			return null;
		}
	}
	public static final byte DEL = 0;
	public static final byte ACTIVE = 1;
	public static final byte FORCHANGEPASS = 2;
	public static final byte WAITING_APPROVAL = 3;
	public static final byte NOT_VERIFY = 4;
	public static final byte DECLINED = 5;

	private int id;
	private int roleId;
	private String username;
	private String password;
	private String salt;
	private String email;
	private int defaultBizId;
	private int type;
	private byte status;
	private long createTime;
	private long updateTime;

	public UserAccount() {}

	public UserAccount(int id, int roleId, String username, String password,
			String salt, String email, int type) {
		this.id = id;
		this.roleId = roleId;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.email = email;
		this.status = NOT_VERIFY;
		this.type = type;
	}

	public UserAccount(int id, int roleId, String username, String password,
			String salt, String email, int defaultBizId, int type) {
		this.id = id;
		this.roleId = roleId;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.email = email;
		this.defaultBizId = defaultBizId;
		this.status = NOT_VERIFY;
		this.type = type;
	}

	public UserAccount(int id, int roleId, String username, String password,
			String salt, String email, int defaultBizId, byte status, int type, long createTime, long updateTime) {
		super();
		this.id = id;
		this.roleId = roleId;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.email = email;
		this.defaultBizId = defaultBizId;
		this.status = status;
		this.type = type;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(short roleId) {
		this.roleId = roleId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getDefaultBizId() {
		return defaultBizId;
	}

	public void setDefaultBizId(int bizId) {
		this.defaultBizId = bizId;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
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
