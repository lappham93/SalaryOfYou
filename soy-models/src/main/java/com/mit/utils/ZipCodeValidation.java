package com.mit.utils;

import com.mit.dao.zipcode.CountryZipCodeDAO;
import com.mit.entities.zipcode.CountryZipCode;

public class ZipCodeValidation {
	
	public static boolean validateZC(String countryCode, String zipCode) {
		if (countryCode == null) {
			return true;
		}
//		if (isPrefixZC(countryCode)) {
//			int preNum = lstZipCode.get(0).length();
//			if (zipCode.length() >= preNum) {
//				zipCode = zipCode.substring(0, preNum);
//			}
//		}
		
		return CountryZipCodeDAO.getInstance().validateZC(countryCode, zipCode, isPrefixZC(countryCode));
	}
	
	private static boolean isPrefixZC(String countryCode) {
		return CountryZipCode.lstPrefix.contains(countryCode.toUpperCase());
	}
	
	public static void main(String[] args) {
		boolean check = ZipCodeValidation.validateZC("IE", "A94");
		System.out.println(check);
	}

}
