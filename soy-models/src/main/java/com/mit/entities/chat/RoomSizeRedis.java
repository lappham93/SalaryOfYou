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
 * @since Apr 26, 2016
 */
public class RoomSizeRedis {
    private final String prefixKey = "room.size.";
    private final Redisson redisson;
    public static RoomSizeRedis instance = new RoomSizeRedis();

    public RoomSizeRedis() {
        redisson = RedisClient.getInstance("room").getConnect();
		if(redisson == null) {
			System.out.println("Don't create connect to redis");
		}
    }
    
    private String genKey(int roomId) {
		return prefixKey + roomId;
	}
    
    public long increaseSize(int roomId) {
		RAtomicLong sizeRoom = redisson.getAtomicLong(genKey(roomId));
		try {
            if(sizeRoom != null){
                return sizeRoom.incrementAndGet();
            }
		} catch (Exception e) {
		}
        return 0L;
	}
    
    public long decreaseSize(int roomId) {
		RAtomicLong sizeRoom = redisson.getAtomicLong(genKey(roomId));
		try {
            if(sizeRoom != null){
                return sizeRoom.decrementAndGet();
            }
		} catch (Exception e) {
		}
        return 0L;
	}
    
    public long getCurSize(int roomId) {
		RAtomicLong sizeRoom = redisson.getAtomicLong(genKey(roomId));
		try {
            if(sizeRoom != null){
                return sizeRoom.get();
            }
		} catch (Exception e) {
		}
        return 0L;
	}
    
    public void setCurSize(int roomId, long value) {
		RAtomicLong sizeRoom = redisson.getAtomicLong(genKey(roomId));
		try {
            if(sizeRoom != null){
                sizeRoom.set(value);
            }
		} catch (Exception e) {
		}
	}
    
}
