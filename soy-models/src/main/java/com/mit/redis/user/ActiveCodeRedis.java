package com.mit.redis.user;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.NumberUtils;
import org.redisson.Redisson;
import org.redisson.core.RLock;
import org.redisson.core.RMap;

import com.mit.redis.RedisClient;

public class ActiveCodeRedis {

	private final int _hourExpire = 4;
	private final String _lockActive = "dkmolbile.active";

	private final Redisson redisson;

	public static ActiveCodeRedis Instance = new ActiveCodeRedis();

	private ActiveCodeRedis() {
		redisson = RedisClient.getInstance("activecode").getConnect();
		if(redisson == null) {
			System.out.println("Don't create connect to redis");
		}
	}

	public RLock getLock() {
		return redisson.getLock(_lockActive);
	}

	public String getActiveCode(int roleId, String email, String imei) {
		RMap<String, Object> map = redisson.getMap(genKeyActive(roleId, email, imei));
		return (String)map.get("activeCode");
	}

	public int getAcceptLicense(int roleId, String email, String imei, String activeCode) {
		RMap<String, Object> map = redisson.getMap(genKeyActive(roleId, email, imei));
		if (activeCode.equals(map.get("activeCode"))) {
			return NumberUtils.toInt(String.valueOf(map.get("acceptedLicense")));
		}
		return 0;
	}
	
	public int getAcceptLicense(int roleId, String email, String imei) {
		RMap<String, Object> map = redisson.getMap(genKeyActive(roleId, email, imei));
		return NumberUtils.toInt(String.valueOf(map.get("acceptedLicense")));
	}

	public void setActiveCode(int roleId, String email, String imei, String activeCode) {
		RMap<String, Object> map = redisson.getMap(genKeyActive(roleId, email, imei));
		map.put("activeCode", activeCode);
		map.put("acceptedLicense", 0);
		map.expire(_hourExpire, TimeUnit.HOURS);
	}

	public boolean setAcceptLicense(int roleId, String requestInfo, String imei, String activeCode, long licenseId) {
		RMap<String, Object> map = redisson.getMap(genKeyActive(roleId, requestInfo, imei));
		if (activeCode.equals(map.get("activeCode"))) {
			map.put("acceptedLicense", licenseId);
			return true;
		}

		return false;
	}
	
	public void setAcceptLicense(int roleId, String requestInfo, String imei, long licenseId) {
		RMap<String, Object> map = redisson.getMap(genKeyActive(roleId, requestInfo, imei));
		map.put("acceptedLicense", licenseId);
	}

	public void removeActive(int roleId, String email, String imei) {
		RMap<String, Object> map = redisson.getMap(genKeyActive(roleId, email, imei));
		map.delete();
	}

	public boolean increaseExpire(int roleId, String email, String imei, int hour) {
		RMap<String, Object> map = redisson.getMap(genKeyActive(roleId, email, imei));
		return map.expire(hour, TimeUnit.HOURS);
	}

	public boolean increaseExpire(int roleId, String email, String imei) {
		RMap<String, Object> map = redisson.getMap(genKeyActive(roleId, email, imei));
		return map.expire(_hourExpire, TimeUnit.HOURS);
	}

	public String genKeyActive(int roleId, String email, String imei) {
		return String.format("spakonect.active.%d:%s.%s", roleId, email, imei);
	}
}
