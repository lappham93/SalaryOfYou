package com.mit.promotion.utils;

import com.mit.utils.ByteUtils;
import com.mit.utils.StringUtils;
import com.mit.utils.TEA;

public class PromotionNoise {
	protected static final TEA teaEncrypt = new TEA("promo54encrypt@$%luv".getBytes());

	public static String encrypt(int val) {
		return StringUtils.bytesToHexString(teaEncrypt.encrypt(ByteUtils.intToByte(val)));
	}

	public static int decrypt(String encrypt) {
		byte[] encryptData = StringUtils.hexStringToBytes(encrypt);
		return ByteUtils.byteToInt(teaEncrypt.decrypt(encryptData));
	}
}
