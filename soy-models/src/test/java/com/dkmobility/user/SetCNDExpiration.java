package com.dkmobility.user;

import java.util.Calendar;

import com.mit.dao.user.UserInfoDAO;
import com.mit.utils.DateTimeUtils;

public class SetCNDExpiration {
	public static void main(String[] args) {
		int userId = 10;
		long cndExpirationDate = DateTimeUtils.parseStringToDate("06-08-2016").getTime();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 11);
		UserInfoDAO.getInstance().updateCndExpirationDate(userId, cndExpirationDate);
	}
}
