package com.mit.redis.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.NumberUtils;
import org.redisson.Redisson;
import org.redisson.core.RBucket;
import org.redisson.core.RLock;
import org.redisson.core.RMap;

import com.mit.redis.RedisClient;

public class SessionRedis {
	private final int _hourExpire = 24;

	private final String _prefixWeakSession = "cyog.session.weak";
	private final String _prefixSession = "cyog.session.";
	private final String _prefixSessionUser = "cyog.session.user.";

	private final Redisson redisson;

	private final String _lockSession = "cyog.session";

	public static SessionRedis Instance = new SessionRedis();

	private SessionRedis() {
		redisson = RedisClient.getInstance("session").getConnect();
		if(redisson == null) {
			System.out.println("Don't create connect to redis");
		}
	}

	public RLock getLock() {
		return redisson.getLock(_lockSession);
	}

	public RMap<String, String> getUserSession(int appId, int uid) {
		RMap<String, String> map = redisson.getMap(genUserKey(appId, uid));
		return map;
	}

	public String getUserSession(int appId, int uid, String imei) {
		RMap<String, String> map = redisson.getMap(genUserKey(appId, uid));
		return map.get(imei);
	}

	public List<String> getAllUserSession(int appId, int uid) {
		RMap<String, String> map = redisson.getMap(genUserKey(appId, uid));
		if(map == null || map.values() == null) {
			return new ArrayList<String>();
		}
		return new ArrayList<String>(map.values());
	}

	public void setUserSession(int appId, int uid, String imei, String session) {
		RMap<String, String> map = redisson.getMap(genUserKey(appId, uid));
		map.put(imei, session);
		map.remove(genRestrict(imei));
		map.expire(_hourExpire, TimeUnit.HOURS);
	}

	public void removeUserSession(int appId, int uid, String imei) {
		RMap<String, String> map = redisson.getMap(genUserKey(appId, uid));
		map.remove(imei);
	}

	public void addRestrictAutoLogin(int appId, int uid, String imei) {
		RMap<String, String> map = redisson.getMap(genUserKey(appId, uid));
		map.remove(imei);
		map.put(genRestrict(imei), "1");
	}

	public boolean isRestrictAutoLogin(int appId, int uid, String imei) {
		RMap<String, String> map = redisson.getMap(genUserKey(appId, uid));
		return "1".equals(map.get(genRestrict(imei)));
	}

	public void removeAllUserSession(int appId, int uid) {
		RMap<String, String> map = redisson.getMap(genUserKey(appId, uid));
		map.delete();
	}

	public int getUserId(String sessionKey) {
		RMap<String, String> map = redisson.getMap(genKey(sessionKey));
		return NumberUtils.toInt(map.get("uid"));
	}

	public Map<String, String> get(String sessionKey) {
		RMap<String, String> map = redisson.getMap(genKey(sessionKey));
		try {
			Set<String> allKey = new HashSet<String>();
			allKey.add("uid");
			allKey.add("typeId");
			allKey.add("bizId");
			allKey.add("roleId");
			allKey.add("imei");
			return map.getAll(allKey);
		} catch (Exception e) {}
		return null;

	}

	public void set(String sessionKey, int uid, int typeId, int bizId, byte roleId, String imei) {
		RMap<String, String> map = redisson.getMap(genKey(sessionKey));
		map.put("uid", uid + "");
		map.put("typeId", typeId + "");
		map.put("bizId", bizId + "");
		map.put("roleId", roleId + "");
		map.put("imei", imei);
		map.expire(_hourExpire, TimeUnit.HOURS);
	}
	
	public void set(String sessionKey, int uid, int typeId, String imei) {
		RMap<String, String> map = redisson.getMap(genKey(sessionKey));
		map.put("uid", uid + "");
		map.put("typeId", typeId + "");
		map.put("imei", imei);
		map.expire(_hourExpire, TimeUnit.HOURS);
	}

	public void remove(String sessionKey) {
		RMap<String, String> map = redisson.getMap(genKey(sessionKey));
		map.delete();
	}

	public Integer getWeakSession(String sessionKey) {
		RBucket<Integer> bucket = redisson.getBucket(genWeakKey(sessionKey));
		return bucket.get();
	}

	public void setWeakSession(String sessionKey, int uid) {
		RBucket<Integer> bucket = redisson.getBucket(genWeakKey(sessionKey));
		bucket.set(uid, 1, TimeUnit.HOURS);
	}

	public void removeWeakSession(String sessionKey) {
		RBucket<Integer> bucket = redisson.getBucket(genWeakKey(sessionKey));
		bucket.delete();
	}

	private String genKey(String sessionKey) {
		return _prefixSession + sessionKey;
	}

	private String genUserKey(int appId, int userId) {
		return _prefixSessionUser + appId + ":" + userId;
	}

	private String genWeakKey(String sessionKey) {
		return _prefixWeakSession + sessionKey;
	}

	private String genRestrict(String imei) {
		return "restrict_" + imei;
	}
}
