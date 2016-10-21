//package com.mit.models;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import com.mit.dao.address.AddressErrCode;
//import com.mit.dao.address.BillingAddressDAO;
//import com.mit.dao.address.ShippingAddressDAO;
//import com.mit.dao.address.StateDAO;
//import com.mit.dao.user.UserInfoDAO;
//import com.mit.entities.address.BillingAddress;
//import com.mit.entities.address.ShippingAddress;
//import com.mit.utils.ZipCodeValidation;
//
//public class AddressModel {
//	public static final int MAX_SHIPPING_ADDRESS = 10;
//	public static final int MAX_BILLING_ADDRESS = 10;
//	
//	public static final AddressModel Instance = new AddressModel();
//	
//	private AddressModel() {}
//	
//	public List<ShippingAddress.UserView> getListShippingAddress(int userId) {
//		
//		List<ShippingAddress> shippingAddresses = ShippingAddressDAO.getInstance().getByUserId(userId);
//		
//		List<ShippingAddress.UserView> addressUserViews = new LinkedList<ShippingAddress.UserView>();
//		
//		for (ShippingAddress shippingAddress: shippingAddresses) {
//			addressUserViews.add(shippingAddress.buildUserView());
//		}		
//		
//		return addressUserViews;
//	}
//	
//	public Map<String, Object> getListShippingAddressWithDefault(int userId) {
//		Map<String, Object> rs = new HashMap<String, Object>();
//		
//		List<ShippingAddress.UserView> shippingAddresses = getListShippingAddress(userId);
//		rs.put("shippingAddresses", shippingAddresses);
//		
//		long defaultShippingAddress = UserInfoDAO.getInstance().getDefaultShippingAddress(userId);
////		if (defaultShippingAddress == 0 && shippingAddresses.size() > 0) {
////			defaultShippingAddress = shippingAddresses.get(0).getId();
////		}
//		
//		rs.put("defaultShippingAddress", defaultShippingAddress);
//		
//		return rs;
//	}
//	
//	public Map<String, Object> addShippingAddress(int userId, String firstName,
//			String lastName, String businessName, String addressLine1, String addressLine2,
//			String city, String state, String country, String zipCode,
//			String phone, String countryCode, boolean isDefault, String checkoutToken, String sessionKey) {
//		int err = AddressErrCode.SUCCESS;
//		Map<String, Object> rs = new HashMap<String, Object>();
//		
//		int addressCount = ShippingAddressDAO.getInstance().countAddress(userId);
//		
//		if (addressCount >= MAX_SHIPPING_ADDRESS) {
//			err = AddressErrCode.LIMIT_ADDRESS;
//		} else if (!ZipCodeValidation.validateZC(countryCode, zipCode)) {
//			err = AddressErrCode.ZIP_CODE_INVALID;
//		} else if (state.isEmpty() && hasState(countryCode)) {
//			err = AddressErrCode.STATE_INVALID;
//		} else {
//			CheckoutModel.Instance.expireTokenIfNotCurrent(userId, sessionKey, checkoutToken);
//			
//			ShippingAddress shippingAddress = new ShippingAddress(0, userId, 
//					firstName, lastName, businessName, addressLine1, addressLine2, city, state, country, 
//					zipCode, phone, countryCode, ShippingAddress.ACTIVE, 0, 0);
//			int rsCode = ShippingAddressDAO.getInstance().insert(shippingAddress);
//			
//			if (rsCode >= 0) {
//				rs.put("shippingAddress", shippingAddress);
//
////				long defaultShippingAddress = UserInfoDAO.getInstance().getDefaultShippingAddress(userId);
////				if (isDefault || defaultShippingAddress == 0) {
//				if (isDefault) {
//					UserInfoDAO.getInstance().updateDefaultShippingAddress(userId, shippingAddress.getId());
//				}
//			} else {
//				err = AddressErrCode.SERVER_ERROR;
//			}
//		}
//		
//		rs.put("err", err);
//		
//		return rs;
//	}
//	
//	public Map<String, Object> updateShippingAddress(int userId, int shippingAddressId, String firstName,
//			String lastName, String businessName, String addressLine1, String addressLine2,
//			String city, String state, String country, String zipCode,
//			String phone, String countryCode, boolean isDefault, String checkoutToken, String sessionKey) {
//		int err = AddressErrCode.SUCCESS;
//		Map<String, Object> rs = new HashMap<String, Object>();
//		
//		ShippingAddress shippingAddress = ShippingAddressDAO.getInstance().getById(shippingAddressId);
//		
//		if (shippingAddress == null || shippingAddress.getUserId() != userId) {
//			err = AddressErrCode.ADDRESS_NOT_EXIST;
//		} else if (!ZipCodeValidation.validateZC(countryCode, zipCode)) {
//			err = AddressErrCode.ZIP_CODE_INVALID;
//		} else if (state.isEmpty() && hasState(countryCode)) {
//			err = AddressErrCode.STATE_INVALID;
//		} else {
//			CheckoutModel.Instance.expireTokenIfNotCurrent(userId, sessionKey, checkoutToken);
//			
//			shippingAddress.setFirstName(firstName);
//			shippingAddress.setLastName(lastName);
//			shippingAddress.setBusinessName(businessName);
//			shippingAddress.setAddressLine1(addressLine1);
//			shippingAddress.setAddressLine2(addressLine2);
//			shippingAddress.setCity(city);
//			shippingAddress.setState(state);
//			shippingAddress.setCountry(country);
//			shippingAddress.setZipCode(zipCode);
//			shippingAddress.setPhone(phone);
//			shippingAddress.setCountryCode(countryCode);
//
//			int rsCode= ShippingAddressDAO.getInstance().update(shippingAddress);
//			
//			if (rsCode >= 0) {
//				rs.put("shippingAddress", shippingAddress);
//				
//				if (isDefault) {
//					UserInfoDAO.getInstance().updateDefaultShippingAddress(userId, shippingAddress.getId());
//				} else {
//					long defaultShippingAddress = UserInfoDAO.getInstance().getDefaultShippingAddress(userId);
//					
//					if (shippingAddress.getId() == defaultShippingAddress) {
//						UserInfoDAO.getInstance().updateDefaultShippingAddress(userId, 0);
//					}
//				}
//			} else {
//				err = AddressErrCode.SERVER_ERROR;
//			}
//		}
//		
//		rs.put("err", err);
//		
//		return rs;
//	}
//	
//	public int deleteShippingAddress(int userId, long addressId, String checkoutToken, String sessionKey) {
//		int err = AddressErrCode.SUCCESS;
//		
//		ShippingAddress shippingAddress = ShippingAddressDAO.getInstance().getById(addressId);
//		
//		if (shippingAddress == null || shippingAddress.getUserId() != userId) {
//			err = AddressErrCode.ADDRESS_NOT_EXIST;
//		} else {
//			CheckoutModel.Instance.expireTokenIfNotCurrent(userId, sessionKey, checkoutToken);
//			
//			int rsCode = ShippingAddressDAO.getInstance().delete(addressId);
//			
//			if (rsCode < 0) {
//				err = AddressErrCode.SERVER_ERROR;
//			} else {
//				long defaultShippingAddress = UserInfoDAO.getInstance().getDefaultShippingAddress(userId);
//				if (addressId == defaultShippingAddress) {
////					long newDefaultBillingAddress = ShippingAddressDAO.getInstance().getFirstAddressByUserId(userId);
////					UserInfoDAO.getInstance().updateDefaultShippingAddress(userId, newDefaultBillingAddress);
//					UserInfoDAO.getInstance().updateDefaultShippingAddress(userId, 0);
//				}
//			}
//		}
//		
//		return err;
//	}
//	
//	public List<BillingAddress.UserView> getListBillingAddress(int userId) {
//		List<BillingAddress> billingAddresses = BillingAddressDAO.getInstance().getByUserId(userId);
//		
//		List<BillingAddress.UserView> addressUserViews = new LinkedList<BillingAddress.UserView>();
//		
//		for (BillingAddress billingAddress: billingAddresses) {
//			addressUserViews.add(billingAddress.buildUserView());
//		}		
//		
//		return addressUserViews;
//	}
//	
//	
//	public Map<String, Object> getListBillingAddressWithDefault(int userId) {
//		Map<String, Object> rs = new HashMap<String, Object>();
//		
//		List<BillingAddress.UserView> billingAddresses = AddressModel.Instance.getListBillingAddress(userId);		
//		rs.put("billingAddresses", billingAddresses);
//		
//		long defaultBillingAddress = UserInfoDAO.getInstance().getDefaultBillingAddress(userId);
//		rs.put("defaultBillingAddress", defaultBillingAddress);
//				
//		return rs;
//	}
//	
//	public Map<String, Object> addBillingAddress(int userId, String firstName,
//			String lastName, String addressLine1, String addressLine2,
//			String city, String state, String country, String zipCode,
//			String phone, String countryCode, boolean isDefault, String checkoutToken, String sessionKey) {
//		int err = AddressErrCode.SUCCESS;
//		Map<String, Object> rs = new HashMap<String, Object>();
//		
//		int addressCount = BillingAddressDAO.getInstance().countAddress(userId);
//		
//		if (addressCount >= MAX_SHIPPING_ADDRESS) {
//			err = AddressErrCode.LIMIT_ADDRESS;
//		} else if (!ZipCodeValidation.validateZC(countryCode, zipCode)) {
//			err = AddressErrCode.ZIP_CODE_INVALID;
//		} else if (state.isEmpty() && hasState(countryCode)) {
//			err = AddressErrCode.STATE_INVALID;
//		} else {
//			CheckoutModel.Instance.expireTokenIfNotCurrent(userId, sessionKey, checkoutToken);
//			
//			BillingAddress billingAddress = new BillingAddress(0, userId, 
//					firstName, lastName, addressLine1, addressLine2, city, state, country, zipCode, phone, 
//					countryCode, BillingAddress.ACTIVE, 0, 0);
//			int rsCode = BillingAddressDAO.getInstance().insert(billingAddress);
//			
//			if (rsCode >= 0) {
//				rs.put("billingAddress", billingAddress);
//				
////				long defaultBillingAddress = UserInfoDAO.getInstance().getDefaultBillingAddress(userId);
////				if (isDefault || defaultBillingAddress == 0) {
//				if (isDefault) {
//					UserInfoDAO.getInstance().updateDefaultBillingAddress(userId, billingAddress.getId());
//				}
//			} else {
//				err = AddressErrCode.SERVER_ERROR;
//			}
//		}
//		
//		rs.put("err", err);
//		
//		return rs;
//	}
//	
//	public Map<String, Object> updateBillingAddress(int userId, int billingAddressId, String firstName,
//			String lastName, String addressLine1, String addressLine2,
//			String city, String state, String country, String zipCode,
//			String phone, String countryCode, boolean isDefault, String checkoutToken, String sessionKey) {
//		int err = AddressErrCode.SUCCESS;
//		Map<String, Object> rs = new HashMap<String, Object>();
//		
//		BillingAddress billingAddress = BillingAddressDAO.getInstance().getById(billingAddressId);
//		
//		if (billingAddress == null || billingAddress.getUserId() != userId) {
//			err = AddressErrCode.ADDRESS_NOT_EXIST;
//		} else if (!ZipCodeValidation.validateZC(countryCode, zipCode)) {
//			err = AddressErrCode.ZIP_CODE_INVALID;
//		} else if (state.isEmpty() && hasState(countryCode)) {
//			err = AddressErrCode.STATE_INVALID;
//		} else {	
//			CheckoutModel.Instance.expireTokenIfNotCurrent(userId, sessionKey, checkoutToken);
//			
//			billingAddress.setFirstName(firstName);
//			billingAddress.setLastName(lastName);
//			billingAddress.setAddressLine1(addressLine1);
//			billingAddress.setAddressLine2(addressLine2);
//			billingAddress.setCity(city);
//			billingAddress.setState(state);
//			billingAddress.setCountry(country);
//			billingAddress.setZipCode(zipCode);
//			billingAddress.setPhone(phone);
//			billingAddress.setCountryCode(countryCode);
//
//			int rsCode= BillingAddressDAO.getInstance().update(billingAddress);
//			
//			if (rsCode >= 0) {
//				rs.put("billingAddress", billingAddress);
//				
//				if (isDefault) {
//					UserInfoDAO.getInstance().updateDefaultBillingAddress(userId, billingAddress.getId());
//				} else {
//					long defaultBillingAddress = UserInfoDAO.getInstance().getDefaultBillingAddress(userId);
//					
//					if (billingAddress.getId() == defaultBillingAddress) {
//						UserInfoDAO.getInstance().updateDefaultBillingAddress(userId, 0);
//					}
//				}
//			} else {
//				err = AddressErrCode.SERVER_ERROR;
//			}
//		}
//		
//		rs.put("err", err);
//		
//		return rs;
//	}
//	
//	public int deleteBillingAddress(int userId, long addressId, String checkoutToken, String sessionKey) {
//		int err = AddressErrCode.SUCCESS;
//		
//		BillingAddress shippingAddress = BillingAddressDAO.getInstance().getById(addressId);
//		
//		if (shippingAddress == null || shippingAddress.getUserId() != userId) {
//			err = AddressErrCode.ADDRESS_NOT_EXIST;
//		} else {	
//			CheckoutModel.Instance.expireTokenIfNotCurrent(userId, sessionKey, checkoutToken);
//			
//			int rsCode = BillingAddressDAO.getInstance().delete(addressId);
//			
//			if (rsCode < 0) {
//				err = AddressErrCode.SERVER_ERROR;
//			} else {
//				long defaultBillingAddress = UserInfoDAO.getInstance().getDefaultBillingAddress(userId);
//				if (addressId == defaultBillingAddress) {
////					long newDefaultBillingAddress = BillingAddressDAO.getInstance().getFirstAddressByUserId(userId);
////					UserInfoDAO.getInstance().updateDefaultBillingAddress(userId, newDefaultBillingAddress);
//					UserInfoDAO.getInstance().updateDefaultBillingAddress(userId, 0);
//				}
//			}
//		}
//		
//		return err;
//	}
//	
//	public boolean hasState(String countryIsoCode) {
//		return StateDAO.getInstance().hasState(countryIsoCode);
//	}
//}
