package com.mit.es;

import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.core.RAtomicLong;

import com.mit.redis.RedisClient;

public class LBSQuotaRedis {
	private final String _prefixAccess = "luv.lbs.";

	private final Redisson redisson;

	public static LBSQuotaRedis Instance = new LBSQuotaRedis();

	protected LBSQuotaRedis() {
		redisson = RedisClient.getInstance("access").getConnect();
		if(redisson == null) {
			System.out.println("Don't create connect to redis");
		}
	}

	public int getQuotaAndDecrement(String key) {
		RAtomicLong idGen = redisson.getAtomicLong(genKey(key));
		long number = -1;
		try {
			number = idGen.getAndDecrement();
		} catch (Exception e) {
		}
		return (int)number;
	}

	public int getRemainTimeToLive(String key) {
		RAtomicLong idGen = redisson.getAtomicLong(genKey(key));
		return (int) idGen.remainTimeToLive();
	}

	public int getQuota(String key, int defaultValue, int timeToLive) {
		RAtomicLong idGen = redisson.getAtomicLong(genKey(key));
		long number = 0;
		try {
			number = idGen.get();
		} catch (Exception e) {

		}

		if(number == 0) {
			number = idGen.addAndGet(defaultValue);
			if(idGen.remainTimeToLive() < 0) {
				idGen.expire(timeToLive, TimeUnit.SECONDS);
			}
		}

		return (int)number;
	}

	private String genKey(String key) {
		return _prefixAccess + key;
	}
}
