package com.mit.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.mit.luv.kafka.producer.ProducerPush;
import com.mit.luv.kafka.producer.ProducerTopic;

public class SendUtils {
	public static final int EMAIL = 1;
	public static final int PHONE = 2;

	private String sendInfo;
	private String countryCode = "";
	private int requestRole;
	private int type;

	public SendUtils(String sendInfo, int requestGroup) {
		this.sendInfo = getValidInfo(sendInfo);
		requestRole = requestGroup;
	}

	public SendUtils(String requestInfo) {
		sendInfo = getValidInfo(requestInfo);
	}

	public SendUtils(String requestInfo, String countryCode, int requestGroup) {
		requestRole = requestGroup;
		if(countryCode == null || countryCode.isEmpty()) {
			sendInfo = getValidInfo(requestInfo);
		} else {
			sendInfo = getPhoneNumber(requestInfo, countryCode);
			this.countryCode = countryCode;
		}
	}

	public SendUtils(String requestInfo, String countryCode) {
		if(countryCode == null || countryCode.isEmpty()) {
			sendInfo = getValidInfo(requestInfo);
		} else {
			sendInfo = getPhoneNumber(requestInfo, countryCode);
		}
	}

	public String getSendInfo() {
		return sendInfo;
	}

	public int getType() {
		return type;
	}

	public String getCountryCode() {
		return countryCode;
	}

	private String getValidInfo(String info) {
		String result = "";
		type = PHONE;
		result = getPhoneNumber(info);
		return result;
	}

	private String getPhoneNumber(String info, String countryCode) {
		String result = "";
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		try {
			PhoneNumber phoneNumber = phoneUtil.parse(info, countryCode);
			if(phoneUtil.isValidNumber(phoneNumber)) {
				result = phoneUtil.format(phoneNumber, PhoneNumberFormat.E164).replace("+", "");
			}
			type = PHONE;
		} catch (NumberParseException e) {}

		return result;
	}

	public String getPhoneNumber(String phone) {
		String phoneNumberNational = "";
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

		try {
			PhoneNumber phoneNumber = phoneUtil.parse(phone, "US");
			countryCode = "US";

			if(!phoneUtil.isValidNumber(phoneNumber)) {
				phoneNumber = phoneUtil.parse(phone, "CA");
				countryCode = "CA";
			}

			if(!phoneUtil.isValidNumber(phoneNumber)) {
				phoneNumber = phoneUtil.parse(phone, "VN");
				countryCode = "VN";
			}

			if(phoneUtil.isValidNumber(phoneNumber)) {
				phoneNumberNational = phoneUtil.format(phoneNumber, PhoneNumberFormat.E164).replace("+", "");
			}

		} catch (NumberParseException e) {
		}

		return phoneNumberNational;
	}

	public void pushWorkerSendCode(String activeCode) {
		String message = System.currentTimeMillis() + "\tLoop\t" + countryCode + "\t" + sendInfo + "\t" + activeCode + "\tactive";
		ProducerPush.send(ProducerTopic.SEND_SMS, message);
	}
}
