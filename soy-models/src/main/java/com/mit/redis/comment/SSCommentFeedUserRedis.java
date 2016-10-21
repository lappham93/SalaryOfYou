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

package com.mit.redis.comment;

import com.mit.redis.RedisClient;
import com.mit.utils.ECommentType;
import java.util.HashMap;
import java.util.Map;
import org.redisson.Redisson;
import org.redisson.core.RMap;

/**
 *
 * @author nghiatc
 * @since May 25, 2016
 */
public class SSCommentFeedUserRedis {
    
    private final String prefixFeed = "comment.feed.";
    private final String prefixProduct = "comment.product.";
    private final String prefixVideoShop = "comment.videoshop.";
    
    
    private final Redisson redisson;
    
    public static SSCommentFeedUserRedis Instance = new SSCommentFeedUserRedis();
    
    public SSCommentFeedUserRedis() {
        redisson = RedisClient.getInstance("commentfeed").getConnect();
		if(redisson == null) {
			System.out.println("Don't create connect to redis");
		}
    }
    
    private String genKey(ECommentType commentType, long feedId) {
        if(feedId > 0){
            if(commentType == ECommentType.FEED){
                return prefixFeed + feedId;
            } else if(commentType == ECommentType.PRODUCT){
                return prefixProduct + feedId;
            } else if(commentType == ECommentType.SHOP_VIDEO){
                return prefixVideoShop + feedId;
            }
        }
        return null;
	}
    
    public void setOnlineCommentFeed(long feedId, ECommentType commentType, int userId){
        if(feedId > 0 && userId > 0 && commentType != null){
            RMap<Integer, Long> rmapUser = redisson.getMap(genKey(commentType, feedId));
            rmapUser.put(userId, System.currentTimeMillis());
        }
    }
    
    public Map<Integer, Long> getOnlineCommentFeed(long feedId, ECommentType commentType){
        Map<Integer, Long> mapUser = new HashMap<>();
        if(feedId > 0 && commentType != null){
            RMap<Integer, Long> rmapUser = redisson.getMap(genKey(commentType, feedId));
            if(rmapUser != null && !mapUser.isEmpty()){
                for(int userId : rmapUser.keySet()){
                    mapUser.put(userId, rmapUser.get(userId));
                }
            }
        }
        return mapUser;
    }
    
    public void removeOnlineCommentFeed(long feedId, ECommentType commentType, int userId){
        if(feedId > 0 && userId > 0 && commentType != null){
            RMap<Integer, Long> rmapUser = redisson.getMap(genKey(commentType, feedId));
            if(rmapUser != null){
                rmapUser.remove(userId);
            }
        }
    }
    
    public boolean checkUserOnlineCommentFeed(long feedId, ECommentType commentType, int userId){
        if(feedId > 0 && userId > 0 && commentType != null){
            RMap<Integer, Long> rmapUser = redisson.getMap(genKey(commentType, feedId));
            return rmapUser.containsKey(userId);
        }
        return false;
    }
    
}
