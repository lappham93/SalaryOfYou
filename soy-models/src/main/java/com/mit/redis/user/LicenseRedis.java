package com.mit.redis.user;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.NumberUtils;
import org.redisson.Redisson;
import org.redisson.core.RLock;
import org.redisson.core.RMap;

import com.mit.redis.RedisClient;

public class LicenseRedis {

	private final int _hourExpire = 4;
	private final String _lockActive = "dkmolbile.license";

	private final Redisson redisson;

	public static LicenseRedis Instance = new LicenseRedis();

	private LicenseRedis() {
		redisson = RedisClient.getInstance("license").getConnect();
		if(redisson == null) {
			System.out.println("Don't create connect to redis");
		}
	}

	public RLock getLock() {
		return redisson.getLock(_lockActive);
	}

	public int getAcceptLicense(int roleId, String email, String imei) {
		RMap<String, Object> map = redisson.getMap(genKeyActive(roleId, email, imei));
		return NumberUtils.toInt(String.valueOf(map.get("acceptedLicense")));
	}

	public boolean setAcceptLicense(int roleId, String email, String imei, long licenseId) {
		RMap<String, Object> map = redisson.getMap(genKeyActive(roleId, email, imei));
		map.put("acceptedLicense", licenseId);
		map.expire(_hourExpire, TimeUnit.HOURS);

		return true;
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
