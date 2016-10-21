package com.mit.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.dao.user.UserErrCode;
import com.mit.entities.session.LoginInfo;
import com.mit.entities.user.UserAccount;
import com.mit.redis.user.SessionRedis;

public class FBAuthenticateModel extends AuthenticateModel {
	private static Logger logger = LoggerFactory.getLogger(FBAuthenticateModel.class);

	public static FBAuthenticateModel Instance = new FBAuthenticateModel();

	@Override
	public LoginInfo login(int appId, String userName, String password, String imei, boolean autoLogin) {
		LoginInfo loginInfo = new LoginInfo();
		int rsCode = UserErrCode.SUCCESS;
		UserAccount account = UserModel.Instance.getAccount(appId, userName);

		if (account == null) {
			rsCode = UserErrCode.ERR_USERNAME;
		} else if (autoLogin && SessionRedis.Instance.isRestrictAutoLogin(appId, account.getId(), imei)){
			rsCode = UserErrCode.ERR_DUPLICATE_LOGIN;
		} else {
			if (!account.getPassword().equals(password)) {
				rsCode = UserErrCode.ERR_PASSWORD;
			} else if (account.getStatus() == UserAccount.FORCHANGEPASS) {
				rsCode = UserErrCode.PASSWORD_EXPIRE;
				loginInfo.setSessionKey(generateWeakSession(account));
			} else {				
				rsCode = buildLoginInfo(appId, account, imei, loginInfo, rsCode);
			}
		}

		loginInfo.setErr(rsCode);

		return loginInfo;
	}
}
