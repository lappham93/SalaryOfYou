package com.mit.redis.user;

import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.core.RLock;
import org.redisson.core.RMap;

import com.mit.redis.RedisClient;

public class BizActiveCodeRedis {
	private final int _hourExpire = 1;
	private final String _lockActive = "loop.biz.active";

	private final Redisson redisson;

	public static BizActiveCodeRedis Instance = new BizActiveCodeRedis();

	private BizActiveCodeRedis() {
		redisson = RedisClient.getInstance("bizactive").getConnect();
		if(redisson == null) {
			System.out.println("Don't create connect to redis");
		}
	}

	public RLock getLock() {
		return redisson.getLock(_lockActive);
	}

	public String getActiveCode(String refId) {
		RMap<String, Object> map = redisson.getMap(genKeyActive(refId));
		return (String)map.get("activeCode");
	}

	public void setActiveCode(String refId, String activeCode) {
		RMap<String, Object> map = redisson.getMap(genKeyActive(refId));
		map.put("activeCode", activeCode);
		map.expire(_hourExpire, TimeUnit.HOURS);
	}

	public void removeActive(String refId) {
		RMap<String, Object> map = redisson.getMap(genKeyActive(refId));
		map.delete();
	}

	public boolean increaseExpire(String refId, int hour) {
		RMap<String, Object> map = redisson.getMap(genKeyActive(refId));
		return map.expire(hour, TimeUnit.HOURS);
	}

	public boolean increaseExpire(String refId) {
		RMap<String, Object> map = redisson.getMap(genKeyActive(refId));
		return map.expire(_hourExpire, TimeUnit.HOURS);
	}

	public String genKeyActive(String refId) {
		return String.format("loop.biz.active:%s", refId);
	}
}
