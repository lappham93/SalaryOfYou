/*
 * Copyright 2016 nghiatc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mit.entities.chat;

import com.mit.redis.RedisClient;
import org.redisson.Redisson;
import org.redisson.core.RAtomicLong;

/**
 *
 * @author nghiatc
 * @since Jun 28, 2016
 */
public class LiveSizeRedis {
    private final String prefixKey = "live.size.";
    private final Redisson redisson;
    public static LiveSizeRedis instance = new LiveSizeRedis();

    public LiveSizeRedis() {
        redisson = RedisClient.getInstance("live").getConnect();
		if(redisson == null) {
			System.out.println("Don't create connect to redis");
		}
    }
    
    private String genKey(long liveId) {
		return prefixKey + liveId;
	}
    
    public long increaseSize(long liveId) {
		RAtomicLong sizeLive = redisson.getAtomicLong(genKey(liveId));
		try {
            if(sizeLive != null){
                return sizeLive.incrementAndGet();
            }
		} catch (Exception e) {
		}
        return 0L;
	}
    
    public long decreaseSize(long liveId) {
		RAtomicLong sizeLive = redisson.getAtomicLong(genKey(liveId));
		try {
            if(sizeLive != null){
                return sizeLive.decrementAndGet();
            }
		} catch (Exception e) {
		}
        return 0L;
	}
    
    public long getCurSize(long liveId) {
		RAtomicLong sizeLive = redisson.getAtomicLong(genKey(liveId));
		try {
            if(sizeLive != null){
                return sizeLive.get();
            }
		} catch (Exception e) {
		}
        return 0L;
	}
    
    public void setCurSize(long liveId, long value) {
		RAtomicLong sizeLive = redisson.getAtomicLong(genKey(liveId));
		try {
            if(sizeLive != null){
                sizeLive.set(value);
            }
		} catch (Exception e) {
		}
	}
}
