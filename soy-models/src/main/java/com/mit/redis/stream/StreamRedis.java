package com.mit.redis.stream;

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

public class StreamRedis {
	private final int _minuteExpire = 30;

	private final String _prefixToken = "bc.stream.token.";
	private final String _prefixName = "bc.stream.name.";

	private final Redisson redisson;

	private final String _lockSession = "bc.stream";

	public static StreamRedis Instance = new StreamRedis();

	private StreamRedis() {
		redisson = RedisClient.getInstance("stream").getConnect();
		if(redisson == null) {
			System.out.println("Don't create connect to redis");
		}
	}

	public RLock getLock() {
		return redisson.getLock(_lockSession);
	}

	public void setToken(String token, String name) {
		RBucket<String> bucket = redisson.getBucket(genTokenKey(token));
		bucket.set(name, _minuteExpire, TimeUnit.MINUTES);
	}

	public void removeToken(String token) {
		RBucket<String> bucket = redisson.getBucket(genTokenKey(token));
		bucket.delete();
	}

	public String getToken(String token) {
		RBucket<String> bucket = redisson.getBucket(genTokenKey(token));
		return bucket.get();
	}

	public void setName(String name, String token) {
		RBucket<String> bucket = redisson.getBucket(genNameKey(name));
		bucket.set(token, _minuteExpire, TimeUnit.MINUTES);
	}

	public void removeName(String name) {
		RBucket<String> bucket = redisson.getBucket(genNameKey(name));
		bucket.delete();
	}

	public String getName(String name) {
		RBucket<String> bucket = redisson.getBucket(genNameKey(name));
		return bucket.get();
	}

	private String genTokenKey(String token) {
		return _prefixToken + token;
	}

	private String genNameKey(String name) {
		return _prefixName + name;
	}
}
