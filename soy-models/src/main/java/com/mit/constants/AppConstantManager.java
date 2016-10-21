package com.mit.constants;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.core.RBucket;
import org.redisson.core.RLock;

import com.mit.dao.AppConstantDAO;
import com.mit.redis.RedisClient;
import com.mit.utils.NumberUtils;

public class AppConstantManager {
	public static AppConstantManager Instance = new AppConstantManager();
	
	public String getString(AppConstantKey appConstantKey) {
		String value = AppConstantRedis.Instance.getStringValue(appConstantKey.getKey());
		
		if (value == null) {
			RLock lock = AppConstantRedis.Instance.getLock();
			try {
				if (lock.tryLock(3, TimeUnit.SECONDS)) {
					value = AppConstantRedis.Instance.getStringValue(appConstantKey.getKey());
					
					if (value == null) {
						AppConstant appConstant = AppConstantDAO.getInstance().getByKey(appConstantKey.getKey());
						AppConstantRedis.Instance.setStringValue(appConstantKey.getKey(), appConstant.getValue());
						value = appConstant.getValue();
					}
				}
			} catch (Exception e) {
			} finally {
				lock.unlock();
			}
			
		}
		
		return value;
	}
	
	public int getInt(AppConstantKey appConstantKey) {
		Integer value = AppConstantRedis.Instance.getIntValue(appConstantKey.getKey());
		
		if (value == null) {
			RLock lock = AppConstantRedis.Instance.getLock();
			try {
				if (lock.tryLock(3, TimeUnit.SECONDS)) {
					value = AppConstantRedis.Instance.getIntValue(appConstantKey.getKey());
					
					if (value == null) {
						AppConstant appConstant = AppConstantDAO.getInstance().getByKey(appConstantKey.getKey());
						AppConstantRedis.Instance.setIntValue(appConstantKey.getKey(), appConstant.getIntValue());
						value = appConstant.getIntValue();
					}
				}
			} catch (Exception e) {
			} finally {
				lock.unlock();
			}			
		}
		
		return NumberUtils.toPrimitive(value);
	}
	
	public long getLong(AppConstantKey appConstantKey) {
		Long value = AppConstantRedis.Instance.getLongValue(appConstantKey.getKey());
		
		if (value == null) {
			RLock lock = AppConstantRedis.Instance.getLock();
			try {
				if (lock.tryLock(3, TimeUnit.SECONDS)) {
					value = AppConstantRedis.Instance.getLongValue(appConstantKey.getKey());
					
					if (value == null) {
						AppConstant appConstant = AppConstantDAO.getInstance().getByKey(appConstantKey.getKey());
						AppConstantRedis.Instance.setLongValue(appConstantKey.getKey(), appConstant.getLongValue());
						value = appConstant.getLongValue();
					}
				}
			} catch (Exception e) {
			} finally {
				lock.unlock();
			}				
		}
		
		return NumberUtils.toPrimitive(value);
	}
	
	public BigDecimal getBigDecimal(AppConstantKey appConstantKey) {
		BigDecimal value = AppConstantRedis.Instance.getBigDecimalValue(appConstantKey.getKey());
		
		if (value == null) {
			RLock lock = AppConstantRedis.Instance.getLock();
			try {
				if (lock.tryLock(3, TimeUnit.SECONDS)) {
					value = AppConstantRedis.Instance.getBigDecimalValue(appConstantKey.getKey());
					
					if (value == null) {
						AppConstant appConstant = AppConstantDAO.getInstance().getByKey(appConstantKey.getKey());
						AppConstantRedis.Instance.setBigDecimalValue(appConstantKey.getKey(), appConstant.getBigDecimalValue());
						value = appConstant.getBigDecimalValue();
					}
				}
			} catch (Exception e) {
			} finally {
				lock.unlock();
			}				
		}
		
		return value;
	}
	
	public void remove(AppConstantKey appConstantKey) {
		if (appConstantKey != null) {
			AppConstantRedis.Instance.remove(appConstantKey.getKey());
		}
	}
	
	private static class AppConstantRedis {
		private final int _hourExpire = 1;

		private final String _prefixAppConst = "cyog.const.";

		private final Redisson redisson;

		private final String _lockAppConst = "cyog.const";

		public static AppConstantRedis Instance = new AppConstantRedis();

		private AppConstantRedis() {
			redisson = RedisClient.getInstance("appconst").getConnect();
			if(redisson == null) {
				System.out.println("Don't create connect to redis");
			}
		}

		public RLock getLock() {
			return redisson.getLock(_lockAppConst);
		}

		public Integer getIntValue(String key) {
			RBucket<Integer> bucket = redisson.getBucket(genKey(key));
			return bucket.get();
		}

		public Long getLongValue(String key) {
			RBucket<Long> bucket = redisson.getBucket(genKey(key));
			return bucket.get();
		}

		public String getStringValue(String key) {
			RBucket<String> bucket = redisson.getBucket(genKey(key));
			return bucket.get();
		}

		public BigDecimal getBigDecimalValue(String key) {
			RBucket<String> bucket = redisson.getBucket(genKey(key));
			return bucket.get() != null ? new BigDecimal(bucket.get()) : null;
		}

		public void setIntValue(String key, int value) {
			RBucket<Integer> bucket = redisson.getBucket(genKey(key));
			bucket.set(value, _hourExpire, TimeUnit.HOURS);
		}

		public void setLongValue(String key, long value) {
			RBucket<Long> bucket = redisson.getBucket(genKey(key));
			bucket.set(value, _hourExpire, TimeUnit.HOURS);
		}	

		public void setStringValue(String key, String value) {
			RBucket<String> bucket = redisson.getBucket(genKey(key));
			bucket.set(value, _hourExpire, TimeUnit.HOURS);
		}

		public void setBigDecimalValue(String key, BigDecimal value) {
			RBucket<String> bucket = redisson.getBucket(genKey(key));
			bucket.set(value.toString(), _hourExpire, TimeUnit.HOURS);
		}
		
		public void remove(String key) {
			redisson.getBucket(genKey(key)).delete();
		}

		private String genKey(String key) {
			return _prefixAppConst + key;
		}
	}
}
