package com.mit.models;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.NumberUtils;
import org.redisson.core.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.dao.user.DeviceTokenDAO;
import com.mit.dao.user.UserErrCode;
import com.mit.dao.user.UserInfoDAO;
import com.mit.entities.app.AppKey;
import com.mit.entities.session.LoginInfo;
import com.mit.entities.session.LoginInfoLiveChat;
import com.mit.entities.session.LoginInfoSocket;
import com.mit.entities.user.DiscountCodeType;
import com.mit.entities.user.UserAccount;
import com.mit.entities.user.UserInfo;
import com.mit.redis.user.SessionRedis;
import com.mit.utils.AuthenticateUtils;

public class AuthenticateModel implements IAuthenticate {
	private static Logger logger = LoggerFactory.getLogger(AuthenticateModel.class);

	public static AuthenticateModel Instance = new AuthenticateModel();

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
			String hashPassword = AuthenticateUtils.hashPassword(password,
					account.getSalt());
			if (!account.getPassword().equals(hashPassword)) {
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

	public int buildLoginInfo(int appId, UserAccount account, String imei, LoginInfo loginInfo, int errCode) {
		UserInfo info = UserInfoDAO.getInstance().getByAccountId(account.getId());
		
		int typeId = DiscountCodeType.RETAILER;
		
		int bizId = 0;
		byte roleId = 0;
		
		int sessionBizId = 0;
		if (errCode == UserErrCode.SUCCESS) {
			sessionBizId = bizId;
		}
		
		String sessionKey = generateSession(appId, account, imei, typeId, sessionBizId, roleId);
		if (sessionKey == null) {
			errCode = UserErrCode.SERVER_ERROR;
		}

		loginInfo.setSessionKey(sessionKey);
		loginInfo.setUserId(info.getId());
		loginInfo.setRoleId(roleId);		
		loginInfo.setBizId(bizId);	
		loginInfo.setUserInfo(info);
		
		if (appId == AppKey.BIZ_LOOP && bizId == 0) {
			errCode = UserErrCode.NO_BIZ;
		}

		return errCode;
	}

	public String generateSession(int appId, UserAccount account, String imei, int typeId, int bizId, byte roleId) {
		String oldSession = SessionRedis.Instance.getUserSession(appId, account.getId(), imei);
		if (oldSession != null) {
			SessionRedis.Instance.remove(oldSession);
		}

		String sessionKey = AuthenticateUtils.generateSession(account.getUsername());
//		RLock lock = BizSessionRedis.Instance.getLock();
		try {
//			if (lock.tryLock(3, TimeUnit.SECONDS)) {
				while (SessionRedis.Instance.getUserId(sessionKey) > 0) {
					sessionKey = AuthenticateUtils.generateSession(account.getUsername());
				}

				SessionRedis.Instance.set(sessionKey, account.getId(), typeId, bizId, roleId, imei);
				SessionRedis.Instance.setUserSession(appId, account.getId(), imei, sessionKey);
//			} else {
//				sessionKey = null;
//			}
		} catch (Exception e) {
			sessionKey = null;
		}
//		finally {
//			lock.unlock();
//		}

		return sessionKey;
	}

	public String generateWeakSession(UserAccount account) {
		String sessionKey = AuthenticateUtils.generateSession(account
				.getUsername());
		RLock lock = SessionRedis.Instance.getLock();
		try {
			if (lock.tryLock(3, TimeUnit.SECONDS)) {
				while (SessionRedis.Instance.getUserId(sessionKey) > 0) {
					sessionKey = AuthenticateUtils.generateSession(account
							.getUsername());
				}

				SessionRedis.Instance.setWeakSession(sessionKey, account.getId());
			}
		} catch (Exception e) {
			sessionKey = "";
		}
		finally {
			lock.unlock();
		}

		return sessionKey;
	}

	@Override
	public LoginInfo isAccessable(String sessionKey, short functionId) {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setSessionKey(sessionKey);
		int rsCode = UserErrCode.SUCCESS;
		int userId = SessionRedis.Instance.getUserId(sessionKey);
		if (userId <= 0) {
			rsCode = UserErrCode.NOT_AUTHENTICATE;
		} else {
			loginInfo.setUserId(userId);
		}

		loginInfo.setErr(rsCode);
		return loginInfo;
	}

	@Override
	public LoginInfo logged(String sessionKey) {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setSessionKey(sessionKey);
		int rsCode = UserErrCode.SUCCESS;
		Map<String, String> cacheLogin = SessionRedis.Instance.get(sessionKey);
		if (cacheLogin == null || cacheLogin.get("uid") == null ) {
			rsCode = UserErrCode.NOT_AUTHENTICATE;
		} else {
			rsCode = loginInfo.getErr();
			loginInfo.setUserId(NumberUtils.toInt(cacheLogin.get("uid")));
			loginInfo.setTypeId(NumberUtils.toInt(cacheLogin.get("typeId")));
			loginInfo.setBizId(NumberUtils.toInt(cacheLogin.get("bizId")));
			loginInfo.setRoleId(NumberUtils.toByte(cacheLogin.get("roleId")));
			loginInfo.setImei(cacheLogin.get("imei"));
		}

		loginInfo.setErr(rsCode);
		return loginInfo;
	}
    
    public LoginInfoSocket loggedSocket(String sessionKey) {
		LoginInfoSocket loginInfo = new LoginInfoSocket();
		loginInfo.setSessionKey(sessionKey);
		int rsCode = UserErrCode.SUCCESS;
		Map<String, String> cacheLogin = SessionRedis.Instance.get(sessionKey);
		if (cacheLogin == null || cacheLogin.get("uid") == null ) {
			rsCode = UserErrCode.NOT_AUTHENTICATE;
		} else {
			loginInfo.setUserId(NumberUtils.toInt(cacheLogin.get("uid")));
			loginInfo.setRoleId(NumberUtils.toByte(cacheLogin.get("roleId")));
			loginInfo.setImei(cacheLogin.get("imei"));
		}

		loginInfo.setErr(rsCode);
		return loginInfo;
	}
    
    public LoginInfoLiveChat loggedLiveChat(String sessionKey) {
		LoginInfoLiveChat loginInfo = new LoginInfoLiveChat();
		loginInfo.setSessionKey(sessionKey);
		int rsCode = UserErrCode.SUCCESS;
		Map<String, String> cacheLogin = SessionRedis.Instance.get(sessionKey);
		if (cacheLogin == null || cacheLogin.get("uid") == null ) {
			rsCode = UserErrCode.NOT_AUTHENTICATE;
		} else {
			loginInfo.setUserId(NumberUtils.toInt(cacheLogin.get("uid")));
			loginInfo.setRoleId(NumberUtils.toByte(cacheLogin.get("roleId")));
			loginInfo.setImei(cacheLogin.get("imei"));
		}

		loginInfo.setErr(rsCode);
		return loginInfo;
	}

	public int getAccountIdFromWeakSession(String sessionKey) {
		Integer accountId = SessionRedis.Instance.getWeakSession(sessionKey);
		return accountId;
	}

	@Override
	public int isAccessable(int uid, short functionId) {
		return UserErrCode.SUCCESS;
	}

	@Override
	public boolean logout(int appId, String sessionKey) {
		LoginInfo loginInfo = logged(sessionKey);
		if(loginInfo != null) {
			SessionRedis.Instance.remove(sessionKey);
			SessionRedis.Instance.removeUserSession(appId, loginInfo.getUserId(), loginInfo.getImei());
//			List<String> sessionKeys = SessionRedis.Instance.getAllUserSession(appId, loginInfo.getUserId());
//			for (String sk : sessionKeys) {
//				SessionRedis.Instance.remove(sk);
//			}
//			SessionRedis.Instance.removeAllUserSession(appId, loginInfo.getUserId());
			DeviceTokenDAO.getInstance().delete(loginInfo.getUserId(), appId, loginInfo.getImei());
		}
		return true;
	}

	@Override
	public boolean logout(int appId, int uid, String imei) {
		String sessionKey = SessionRedis.Instance.getUserSession(appId, uid, imei);
		SessionRedis.Instance.remove(sessionKey);
		SessionRedis.Instance.removeUserSession(appId, uid, imei);
		return true;
	}

	@Override
	public boolean logoutAll(int appId, int uid) {
		List<String> sessionKeys = SessionRedis.Instance.getAllUserSession(appId, uid);
		for (String sessionKey: sessionKeys) {
			SessionRedis.Instance.remove(sessionKey);
		}
		SessionRedis.Instance.removeAllUserSession(appId, uid);
		return true;
	}

	@Override
	public boolean logoutAll(int uid) {
		logoutAll(AppKey.CYOGEL, uid);
		return true;
	}
}
