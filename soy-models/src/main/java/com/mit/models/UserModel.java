package com.mit.models;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.RandomStringUtils;
import org.redisson.core.RLock;

import com.mit.constants.UserConstant;
import com.mit.dao.mid.MIdGenDAO;
import com.mit.dao.mid.MIdGenLongDAO;
import com.mit.dao.photo.PhotoCommon;
import com.mit.dao.user.LicenseAcceptanceDAO;
import com.mit.dao.user.UserAccountDAO;
import com.mit.dao.user.UserErrCode;
import com.mit.dao.user.UserInfoDAO;
import com.mit.entities.facebook.FBUserInfo;
import com.mit.entities.user.LicenseAcceptance;
import com.mit.entities.user.UserAccount;
import com.mit.entities.user.UserInfo;
import com.mit.luv.kafka.producer.ProducerPush;
import com.mit.luv.kafka.producer.ProducerTopic;
import com.mit.midutil.MIdNoise;
import com.mit.redis.user.ActiveCodeRedis;
import com.mit.redis.user.BizActiveCodeRedis;
import com.mit.redis.user.LicenseRedis;
import com.mit.utils.AuthenticateUtils;
import com.mit.utils.PasswordUtils;
import com.mit.utils.SendEmailUtils;

public class UserModel {
	public static final String SA_BIZ_CODE = "000000";

	public static UserModel Instance = new UserModel();

	public UserAccount getAccount(int appId, String userName) {
		UserAccount account = null;

		account = UserAccountDAO.getInstance().getByUserNameAndRole(userName, UserConstant.ROLE_USER);

		return account;
	}

	public boolean acceptLicenseUser(String sendInfo, String imei, String activeCode) {
		return acceptLicense(UserConstant.ROLE_USER, sendInfo, imei, activeCode);
	}

	public boolean acceptLicense(int roleId, String sendInfo, String imei, String activeCode) {
		boolean isValid = false;

		LicenseAcceptance acceptance = new LicenseAcceptance(sendInfo, true);
		LicenseAcceptanceDAO.getInstance().insert(acceptance);

		RLock lock = ActiveCodeRedis.Instance.getLock();
		try {
			if (lock.tryLock(3, TimeUnit.SECONDS)) {
				try {
					isValid = ActiveCodeRedis.Instance.setAcceptLicense(roleId, sendInfo, imei, activeCode, acceptance.getId());
				} finally {
					lock.unlock();
				}
			} else {
				activeCode = "";
			}
		} catch (InterruptedException e) {
			activeCode = "";
		} finally {
			if(lock.isLocked()) {
				lock.unlock();
			}
		}

		return isValid;
	}

	public boolean acceptLicenseFBUser(String fbId, String imei) {
		return acceptLicenseFB(UserConstant.ROLE_USER, fbId, imei);
	}

	public boolean acceptLicenseFB(int roleId, String fbId, String imei) {
		boolean isValid = false;

		LicenseAcceptance acceptance = new LicenseAcceptance(fbId, true);
		LicenseAcceptanceDAO.getInstance().insert(acceptance);

		RLock lock = ActiveCodeRedis.Instance.getLock();
		try {
			if (lock.tryLock(3, TimeUnit.SECONDS)) {
				try {
					isValid = LicenseRedis.Instance.setAcceptLicense(roleId, fbId, imei, acceptance.getId());
				} finally {
					lock.unlock();
				}
			}
		} catch (InterruptedException e) {
		} finally {
			if(lock.isLocked()) {
				lock.unlock();
			}
		}

		return isValid;
	}
	
	public boolean acceptLicenseUser(String sendInfo, String imei) {
		return acceptLicense(UserConstant.ROLE_USER, sendInfo, imei);
	}
	
	public boolean acceptLicense(int roleId, String sendInfo, String imei) {
		boolean success = false;
		LicenseAcceptance acceptance = new LicenseAcceptance(sendInfo, true);
		LicenseAcceptanceDAO.getInstance().insert(acceptance);

		RLock lock = ActiveCodeRedis.Instance.getLock();
		try {
			if (lock.tryLock(3, TimeUnit.SECONDS)) {
				try {
					ActiveCodeRedis.Instance.setAcceptLicense(roleId, sendInfo, imei, acceptance.getId());
					success = true;
				} finally {
					lock.unlock();
				}
			} else {
			}
		} catch (InterruptedException e) {
		} finally {
			if(lock.isLocked()) {
				lock.unlock();
			}
		}
		
		return success;
	}

	public boolean checkRequestInfo(String sendInfo, int roleId) {
		UserAccount account = UserAccountDAO.getInstance().getByUserNameAndRole(sendInfo, roleId);
		return account != null;
	}

	public boolean validCodeUser(String email, String imei, String activeCode) {
		return validCode(UserConstant.ROLE_USER, email, imei, activeCode);
	}

	public boolean validCode(short roleId, String sendInfo, String imei, String activeCode) {
		String storedActiveCode = ActiveCodeRedis.Instance.getActiveCode(roleId, sendInfo, imei);
		boolean isValid = activeCode.equals(storedActiveCode);

		if(isValid) {
			ActiveCodeRedis.Instance.increaseExpire(roleId, sendInfo, imei, 2);
		}

		return isValid;
	}

	public int checkAcceptLicenseUser(String sendInfo, String imei, String activeCode) {
		return checkAcceptLicense(UserConstant.ROLE_USER, sendInfo, imei, activeCode);
	}

	public int checkAcceptLicense(int roleId, String sendInfo, String imei, String activeCode) {
		return ActiveCodeRedis.Instance.getAcceptLicense(roleId, sendInfo, imei, activeCode);
	}

	public int checkAcceptLicenseFBUser(String sendInfo, String imei) {
		return checkAcceptLicenseFB(UserConstant.ROLE_USER, sendInfo, imei);
	}

	public int checkAcceptLicenseFB(int roleId, String sendInfo, String imei) {
		return LicenseRedis.Instance.getAcceptLicense(roleId, sendInfo, imei);
	}
	
	public int checkAcceptLicenseUser(String sendInfo, String imei) {
		return checkAcceptLicense(UserConstant.ROLE_USER, sendInfo, imei);
	}

	public int checkAcceptLicense(int roleId, String sendInfo, String imei) {
		return ActiveCodeRedis.Instance.getAcceptLicense(roleId, sendInfo, imei);
	}

	public int checkAccount(int roleId, String userName, String password, String repeatPassword) {
		int err = UserErrCode.SUCCESS;

		if (!PasswordUtils.checkPassword(password, userName, false)) {
			err =  UserErrCode.PASSWORD_WEAK;
		} else if(!repeatPassword.equals(password)) {
			err = UserErrCode.ERR_REPEAT_PASSWORD;
		} else {
			UserAccount curAccount = UserAccountDAO.getInstance()
					.getByUserNameAndRole(userName, roleId);

			if (curAccount != null) {
				err = UserErrCode.DUPLICATE_USERNAME;
			}
		}

		return err;
	}

	public Map<String, Object> requestCodeUser(String info, String imei) {
		return requestCode(UserConstant.ROLE_USER, info, imei);
	}

	public int checkAllowRegister(short roleId, String info, String imei, int err) {
		if (checkRequestInfo(info, roleId)) {
			err = UserErrCode.DUPLICATE_SEND_INFO;
		}

		return err;
	}

	public String genActiveCode(short roleId, String info, String imei) {
		String activeCode = null;
		RLock lock = ActiveCodeRedis.Instance.getLock();
		try {
			if (lock.tryLock(3, TimeUnit.SECONDS)) {
				try {
					activeCode = RandomStringUtils.randomNumeric(4);

					ActiveCodeRedis.Instance.setActiveCode(roleId, info, imei, activeCode);
				} finally {
					lock.unlock();
				}
			}
		} catch (InterruptedException e) {
			activeCode = null;
		} finally {
			if(lock.isLocked()) {
				lock.unlock();
			}
		}

		return activeCode;
	}

	public Map<String, Object> requestCode(short roleId, String info, String imei) {
		Map<String, Object> rs = new HashMap<String, Object>();
		int err = UserErrCode.SUCCESS;

		err = checkAllowRegister(roleId, info, imei, err);

		if (err != UserErrCode.DUPLICATE_SEND_INFO) {
			String activeCode = ActiveCodeRedis.Instance.getActiveCode(roleId, info, imei);
			if (activeCode == null) {
				activeCode = genActiveCode(roleId, info, imei);
			} else {
				ActiveCodeRedis.Instance.increaseExpire(roleId, info, imei);
			}
			if(activeCode != null) {
				rs.put("activeCode", activeCode);
			} else {
				err = UserErrCode.SERVER_ERROR;
			}
		}

		rs.put("err", err);

		return rs;
	}

	public int registerUser(String userName, String password, String imei, String model, String firstName, 
			String lastName, short gender, String email, String mobilePhone, String countryCode, String birthday, boolean acceptLicense, int type) {
		int err = UserErrCode.SUCCESS;
		short roleId = UserConstant.ROLE_USER;

		if (checkRequestInfo(userName, roleId)) {
			err = UserErrCode.DUPLICATE_SEND_INFO;
		} if (!acceptLicense) {
			err = UserErrCode.NOT_ACCEPT_LICENSE;
		} else {
			err = checkAccount(roleId, userName, password, password);

			if (err == 0) {				
//				if (licenseCode != null) {
//					licenseCode = licenseCode.toUpperCase();
//				}
//				LicenseCode code = LicenseCodeDAO.getInstance().getByCode(licenseCode);
				
//				if (code != null && code.getStatus() != LicenseCode.ACTIVE) {
//					err = UserErrCode.LICENSE_CODE_USED;
//				}
				
//				if (err == UserErrCode.SUCCESS) {
					int id = MIdGenDAO.getInstance(UserAccountDAO.TABLE_NAME).getNext();
					String salt = RandomStringUtils.randomAlphanumeric(10);
					String hashPassword = AuthenticateUtils.hashPassword(password, salt);
					UserAccount account = new UserAccount(id, roleId, userName, hashPassword, salt, email, type);
					UserAccountDAO.getInstance().insert(account);
	
					UserInfo user = new UserInfo(id, firstName, lastName, gender, email, mobilePhone, countryCode, birthday);
//					if (code == null && !licenseCode.isEmpty()) {
//						user.setPreviewing(true);
//					}
//					Calendar cal = Calendar.getInstance();
//					cal.add(Calendar.DATE, 90);
//					user.setCndExpirationDate(cal.getTimeInMillis());
					UserInfoDAO.getInstance().insert(user);
	
					LicenseAcceptance acceptance = new LicenseAcceptance(0, account.getId(), userName, true, System.currentTimeMillis());
					LicenseAcceptanceDAO.getInstance().insert(acceptance);
                    
					ProducerPush.send(ProducerTopic.USER_REGISTER, System.currentTimeMillis() + "\t" + id);
					String oidNoise = MIdNoise.enNoiseIId(id);
					SendEmailUtils.Instance.sendRegister(oidNoise, email);
//				}
			}
		}

		return err;
	}
	
	public int checkRegisterUser(String sendInfo, String userName, String password, String imei) {
		int err = UserErrCode.SUCCESS;
		short roleId = UserConstant.ROLE_USER;

		if (checkRequestInfo(sendInfo, roleId)) {
			err = UserErrCode.DUPLICATE_SEND_INFO;
		} else if (checkAcceptLicenseUser(sendInfo, imei) <= 0) {
			err = UserErrCode.NOT_ACCEPT_LICENSE;
		} else {
			err = checkAccount(roleId, userName, password, password);
		}

		return err;
	}
	
//	public UserInfo registerUser(String sendInfo, String email, String userName,
//			String password, String imei, String model, String firstName, String lastName, short gender, int avtVer, String mobilePhone,
//			String birthday) {
//		short roleId = UserConstant.ROLE_USER;
//		int id = MIdGenDAO.getInstance(UserAccountDAO.TABLE_NAME).getNext();
//		String salt = RandomStringUtils.randomAlphanumeric(10);
//		String hashPassword = AuthenticateUtils.hashPassword(password, salt);
//		UserAccount account = new UserAccount(id, roleId, userName, hashPassword, salt, sendInfo);
//		UserAccountDAO.getInstance().insert(account);
//
//		UserInfo user = new UserInfo(id, firstName, lastName, gender, email, mobilePhone, birthday);
//		UserInfoDAO.getInstance().insert(user);
//
//		int licenseId = ActiveCodeRedis.Instance.getAcceptLicense(roleId, email, imei);
//		LicenseAcceptanceDAO.getInstance().updateAccount(licenseId, account.getId());
//
//		ActiveCodeRedis.Instance.removeActive(roleId, sendInfo, imei);
//		
//		return user;
//	}

	public int updateUserInfo(int accountId, String firstName, String lastName, short gender, String email, String mobilePhone,
			String countryCode, String birthday) {
		int err = UserErrCode.SUCCESS;

		UserInfo user = UserInfoDAO.getInstance().getByAccountId(accountId);
		if (firstName != null) {
			user.setFirstName(firstName);
		}

		if (lastName != null) {
			user.setLastName(lastName);
		}

		if (gender > 0) {
			user.setGender(gender);
		}
		// TODO: check if email is changed?
//		if (email != null && !user.getEmail().equals(email)){
//			user.setEmail(email);
////			UserAccount account = UserAccountDAO.getInstance().getById(accountId);
////			account.setEmail(email);
////			UserAccountDAO.getInstance().update(account);
//		}

		if (mobilePhone != null) {
			user.setMobilePhone(mobilePhone);
		}
		
		if (countryCode != null) {
			user.setCountryCode(countryCode);
		}

		if (birthday != null && !birthday.isEmpty()) {
			user.setBirthday(birthday);
		}
		
//		if (licenseCode != null) {
//			licenseCode = licenseCode.toUpperCase();
//			if (user.getLicenseCode() != null && !user.getLicenseCode().isEmpty()) {
//				if (!licenseCode.equals(user.getLicenseCode())) {
//					err = UserErrCode.UNABLE_UPDATE_CODE;
//				}				
//			} else if (!licenseCode.isEmpty()) {
//				LicenseCode code = LicenseCodeDAO.getInstance().getByCode(licenseCode);
//				
//				if (code != null) {
//					if (code.getStatus() == LicenseCode.ACTIVE) {
//						user.setLicenseCode(licenseCode);
//						code.setStatus(LicenseCode.INACTIVE);
//						LicenseCodeDAO.getInstance().update(code);
//					} else {
//						err = UserErrCode.LICENSE_CODE_USED;
//					}
//				} else {
//					user.setLicenseCode(licenseCode);
//					user.setPreviewing(true);
//				}
//			}
//		}
		
		if (err == 0) {
			int rs = UserInfoDAO.getInstance().update(user);
			
			if (rs < 0) {
				err = UserErrCode.SERVER_ERROR;
			}
		}

		return err;
	}

	public int changePassword(int accountId, String curPassword, String newPassword, String repeatPassword) {
		int rsCode = UserErrCode.SUCCESS;

		UserAccount account = UserAccountDAO.getInstance().getById(accountId);

		if (account == null) {
			rsCode = UserErrCode.NOT_AUTHENTICATE;
		} else {
			String hashPassword = AuthenticateUtils.hashPassword(curPassword,
					account.getSalt());

			if (!account.getPassword().equals(hashPassword)) {
				rsCode = UserErrCode.ERR_PASSWORD;
			} else if ( !PasswordUtils.checkPassword(newPassword, account.getUsername(), false)) {
				rsCode =  UserErrCode.PASSWORD_WEAK;
			} else if(!repeatPassword.equals(newPassword)) {
				rsCode = UserErrCode.ERR_REPEAT_PASSWORD;
			} else {
				String salt = RandomStringUtils.randomAlphanumeric(10);
				String hashNewPassword = AuthenticateUtils.hashPassword(newPassword, salt);

				account.setPassword(hashNewPassword);
				account.setSalt(salt);
				if(account.getStatus() == UserAccount.FORCHANGEPASS) {
					account.setStatus(UserAccount.ACTIVE);
				}
				int sqlRs = UserAccountDAO.getInstance().update(account);
				if(sqlRs < 0) {
					rsCode = UserErrCode.SERVER_ERROR;
				}
			}
		}

		return rsCode;
	}

	public Map<String, Object> resetPassword(int appId, String userName) {
		int rsCode = UserErrCode.SUCCESS;
		Map<String, Object> rs = new HashMap<String, Object>();

		UserAccount account = getAccount(appId, userName);

		if (account == null) {
			rsCode = UserErrCode.ERR_USERNAME;
		} else {
			String newPassword = PasswordUtils.random(account.getUsername(), false);

			String salt = RandomStringUtils.randomAlphanumeric(10);
			String hashNewPassword = AuthenticateUtils.hashPassword(newPassword, salt);

			account.setPassword(hashNewPassword);
			account.setSalt(salt);
			account.setStatus(UserAccount.FORCHANGEPASS);
			int sqlRs = UserAccountDAO.getInstance().update(account);
			if(sqlRs < 0) {
				rsCode = UserErrCode.SERVER_ERROR;
			} else {
				rs.put("email", account.getEmail());
				rs.put("accountId", account.getId());
				rs.put("newPassword", newPassword);
				rs.put("fullName", getFullName(account.getId()));
			}
		}

		rs.put("err", rsCode);

		return rs;
	}

	public UserInfo getUserInfo(int userId) {
		return UserInfoDAO.getInstance().getById(userId);
	}

//	public AccountInfo getUserInfo(int userId, int roleId) {
//		AccountInfo userInfo = null;
//
//		if (roleId == UserConstant.ROLE_USER) {
//			userInfo = getUserInfo(userId);
//		}
//
//		return userInfo;
//	}

	public String getFullName(int accountId) {
		UserInfo userInfo = getUserInfo(accountId);

		if (userInfo != null) {
			return userInfo.getFirstName() + " " + userInfo.getLastName();
		} else {
			return "Valued Customer";
		}
	}

	public void verifyAccount(int accountId) {
		UserAccount account = UserAccountDAO.getInstance().getById(accountId);
		if (account != null && account.getStatus() == UserAccount.NOT_VERIFY) {
			account.setStatus(UserAccount.ACTIVE);
			UserAccountDAO.getInstance().update(account);
		}
	}
	
	public int registerWithFB(FBUserInfo fbUserInfo, String userName, String imei, String mobilePhone, String countryCode, boolean acceptLicense, int type) {
		int err = UserErrCode.SUCCESS;
		short roleId = UserConstant.ROLE_USER;

		if (checkRequestInfo(userName, roleId)) {
			err = UserErrCode.DUPLICATE_SEND_INFO;
		} else if (!acceptLicense) {
			err = UserErrCode.NOT_ACCEPT_LICENSE;
		} else {			
//			LicenseCode code = LicenseCodeDAO.getInstance().getByCode(licenseCode);
//			
//			if (code != null && code.getStatus() != LicenseCode.ACTIVE) {
//				err = UserErrCode.LICENSE_CODE_USED;
//			}
			
//			if (err == UserErrCode.SUCCESS) {
				int id = (int)MIdGenDAO.getInstance(UserAccountDAO.TABLE_NAME).getNext();
				String salt = "";
				String hashPassword = "";
				UserAccount account = new UserAccount(id, roleId, userName, hashPassword, salt, fbUserInfo.getEmail(), type);
				UserAccountDAO.getInstance().insert(account);
	
				UserInfo user = new UserInfo(id, fbUserInfo.getFirstName(), fbUserInfo.getLastName(), fbUserInfo.getGender(),
						fbUserInfo.getEmail(), mobilePhone, countryCode, fbUserInfo.getBirthday());
//				if (code == null && !licenseCode.isEmpty()) {
//					user.setPreviewing(true);
//				}
				long pId = 0;
				if (fbUserInfo.getAvatar() != null && !fbUserInfo.getAvatar().isEmpty()) {
					pId = MIdGenLongDAO.getInstance(PhotoCommon.userPhotoIdGen).getNext();
				}
				user.setPhoto(pId);
//				Calendar cal = Calendar.getInstance();
//				cal.add(Calendar.DATE, 90);
//				user.setCndExpirationDate(cal.getTimeInMillis());
				UserInfoDAO.getInstance().insert(user);
	
				LicenseAcceptance acceptance = new LicenseAcceptance(0, account.getId(), userName, true, System.currentTimeMillis());
				LicenseAcceptanceDAO.getInstance().insert(acceptance);
	
				if (pId > 0) {
					String message = System.currentTimeMillis() + "\t" + id + "\t" + fbUserInfo.getAvatar();
					ProducerPush.send(ProducerTopic.UPLOAD_AVATAR_FROM_URL, message);
				}
				
				ProducerPush.send(ProducerTopic.USER_REGISTER, System.currentTimeMillis() + "\t" + id);
				
				if (fbUserInfo.getEmail() != null && !fbUserInfo.getEmail().isEmpty()) {
					String oidNoise = MIdNoise.enNoiseIId(id);
					SendEmailUtils.Instance.sendRegister(oidNoise, fbUserInfo.getEmail());
				}
//			}
		}

		return err;
	}
	
	public String genBizActiveCode(String refId) {
		String activeCode = BizActiveCodeRedis.Instance.getActiveCode(refId);
		
		if (activeCode == null) {
			RLock lock = BizActiveCodeRedis.Instance.getLock();
			try {
				if (lock.tryLock(3, TimeUnit.SECONDS)) {
					try {
						activeCode = RandomStringUtils.randomNumeric(4);

						BizActiveCodeRedis.Instance.setActiveCode(refId, activeCode);
					} finally {
						lock.unlock();
					}
				}
			} catch (InterruptedException e) {
				activeCode = null;
			} finally {
				if(lock.isLocked()) {
					lock.unlock();
				}
			}
		} else {
			BizActiveCodeRedis.Instance.increaseExpire(refId);
		}		

		return activeCode;
	}
	
	public static void main(String[] args) {
//		 System.out.println(UserAccountDAO.getInstance().getByUserNameAndRole("gg_112363728526348444258", 1).getStatus());
		System.out.println(UserModel.Instance.resetPassword(1, "ezeros88@gmail.com"));
	}
}
