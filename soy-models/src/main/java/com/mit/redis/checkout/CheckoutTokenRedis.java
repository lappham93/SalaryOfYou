package com.mit.redis.checkout;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.NumberUtils;
import org.redisson.Redisson;
import org.redisson.core.RLock;
import org.redisson.core.RMap;

import com.mit.redis.RedisClient;

public class CheckoutTokenRedis {
	private final int _minuteExpire = 15;

	private final String _prefixSession = "cyog.checkout.";
	private final String _prefixSessionUser = "cyog.checkout.user.";

	private final Redisson redisson;

	private final String _lockSession = "cyog.checkout";

	public static CheckoutTokenRedis Instance = new CheckoutTokenRedis();

	private CheckoutTokenRedis() {
		redisson = RedisClient.getInstance("session").getConnect();
		if(redisson == null) {
			System.out.println("Don't create connect to redis");
		}
	}

	public RLock getLock() {
		return redisson.getLock(_lockSession);
	}

	public String getUserToken(int uid, String sessionKey) {
		RMap<String, String> bucket = redisson.getMap(genUserKey(uid));
		String token = null;
		if (sessionKey.equals(bucket.get("sk"))) {
			token = bucket.get("tk");
		}
		return token;
	}	

	public Double getWeight(int uid) {
		RMap<String, String> bucket = redisson.getMap(genUserKey(uid));
		return bucket.get("wt") != null ? NumberUtils.toDouble(bucket.get("wt")) : null;
	}		

//	public BigDecimal getTotalPrice(int uid) {
//		RMap<String, String> bucket = redisson.getMap(genUserKey(uid));
//		return bucket.get("tp") != null ? new BigDecimal(bucket.get("tp")) : null;
//	}	

	public void setUserToken(int uid, String sessionKey, String token, double weight) {
		RMap<String, String> bucket = redisson.getMap(genUserKey(uid));
		bucket.put("sk", sessionKey);
		bucket.put("tk", token);
		bucket.put("wt", weight + "");
//		bucket.put("tp", totalPrice.toString());
		bucket.expire(_minuteExpire, TimeUnit.MINUTES);
	}

	public void removeUserToken(int uid) {
		RMap<String, String> bucket = redisson.getMap(genUserKey(uid));
		bucket.delete();
	}

	public int getUserId(String token) {
		RMap<String, String> map = redisson.getMap(genKey(token));
		return NumberUtils.toInt(map.get("uid"));
	}

	public Map<String, String> get(String token) {
		RMap<String, String> map = redisson.getMap(genKey(token));
		try {
			Set<String> allKey = new HashSet<String>();
			allKey.add("uid");
			return map.getAll(allKey);
		} catch (Exception e) {}
		return null;

	}

	public void set(String token, int uid) {
		RMap<String, String> map = redisson.getMap(genKey(token));
		map.put("uid", uid + "");
		map.expire(_minuteExpire, TimeUnit.MINUTES);
	}

	public void remove(String token) {
		RMap<String, String> map = redisson.getMap(genKey(token));
		map.delete();
	}

	private String genKey(String token) {
		return _prefixSession + token;
	}

	private String genUserKey(int userId) {
		return _prefixSessionUser + ":" + userId;
	}
}
