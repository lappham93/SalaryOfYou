package com.mit.dao.user;

public class UserErrCode {
	public static final int SUCCESS = 0;
	public static final int SERVER_ERROR = -1;

	public static final int ERR_USERNAME = -101;
	public static final int ERR_PASSWORD = -102;
	public static final int ERR_IMEI = -103;
	public static final int UNACCESSABLE = -104;
	public static final int NOT_AUTHENTICATE = -105;
	public static final int ERR_ACTIVECODE = -106;
	public static final int EXIST_IMEI = -107;
	public static final int PASSWORD_WEAK = -108;
	public static final int ERR_REPEAT_PASSWORD = -109;
	public static final int ERR_DUPLICATE_LOGIN = -110;

	public static final int DUPLICATE_USERNAME = -121;
	public static final int DUPLICATE_SEND_INFO = -122;
	public static final int INVALID_SHOP_CODE = -123;
	public static final int INVALID_ACTIVE_CODE = -124;
	public static final int NO_BIZ = -125;
	public static final int EMAIL_NOT_EXIST = -126;
	public static final int NOT_SERIAL_SHOP = -127;
	public static final int NOT_ACCEPT_LICENSE = -128;
	public static final int ALLOW_REGISTER_MORE = -129;
	public static final int INCORRECT_EMAIL = -130;

	public static final int USER_NOT_EXIST = -131;
	public static final int PASSWORD_EXPIRE = -132;
	public static final int WAITING_APPROVAL = -133;
	public static final int SHOP_EXISTED = -134;
	public static final int REJECTED = -135;

	public static final int LIMIT_BIZ = -140;
	public static final int INVALID_ROLE = -141;
	public static final int REF_BIZ_REGISTERED = -142;
	
	public static final int LICENSE_CODE_USED = -150;
	public static final int UNABLE_UPDATE_CODE = -151;
}
