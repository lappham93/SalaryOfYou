package com.mit.redis.user;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.core.RAtomicLong;

import com.mit.redis.RedisClient;
import com.mit.utils.ConfigUtils;

public class LimitAccessRedis {		
	private final String _prefixAccess = "dkmobile.access.";
	private final String _prefixAccessConf = "access.";

	private final Redisson redisson;
	private final Map<AccessType, Integer> expireTime;
	private final Map<AccessType, Integer> limitNumber;
	
	public static LimitAccessRedis Instance = new LimitAccessRedis();

	protected LimitAccessRedis() {
		redisson = RedisClient.getInstance("access").getConnect();
		if(redisson == null) {
			System.out.println("Don't create connect to redis");
		}
		
		expireTime = new HashMap<AccessType, Integer>();
		limitNumber = new HashMap<AccessType, Integer>();
		for (AccessType accessType : AccessType.values()) {
			expireTime.put(accessType, ConfigUtils.getConfig().getInt(ConfigUtils.genKey(genAccessKey(accessType.getName()), "time")));
			limitNumber.put(accessType, ConfigUtils.getConfig().getInt(ConfigUtils.genKey(genAccessKey(accessType.getName()), "number")));
		}
	}

	public boolean checkLimit(String key, int appId, AccessType accessType) {
		RAtomicLong idGen = redisson.getAtomicLong(genKey(key, appId, accessType));
		long number = 1;
		try {
			number = idGen.get();
		} catch (Exception e) {			
		}
		return number <= limitNumber.get(accessType);
	}
	
	public void increaseExpire(String key, int appId, AccessType accessType) {
		RAtomicLong idGen = redisson.getAtomicLong(genKey(key, appId, accessType));
		try {
			idGen.incrementAndGet();
			idGen.expire(expireTime.get(accessType), TimeUnit.MINUTES);
		} catch (Exception e) {
		}
	}

	private String genKey(String key, int appId, AccessType accessType) {
		return _prefixAccess + accessType.getName() + "." + appId + ":" + key;
	}
	
	private String genAccessKey(String accessName) {
		return _prefixAccessConf + accessName;
	}
	
	public static enum AccessType {
		LOGIN("login"), 
		REQUEST_CODE_BY_PHONE("active.phone"),
		REQUEST_CODE_BY_IMEI("active.imei"), 
		RESET_PASSWORD_BY_PHONE("password.phone"),
		RESET_PASSWORD_BY_IMEI("password.imei");
		
		private String name;

		private AccessType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
