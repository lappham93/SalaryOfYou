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

import com.mit.entities.user.UserBanning;

/**
 *
 * @author nghiatc
 * @since Apr 28, 2016
 */
public class LockUserRoom extends UserBanning{
	
	public static final int TYPE = BanningType.CHAT.getValue();
//    private long id;
    private int roomId;
//    private int userId;
//    private long timeLock;
    private long timeDuration;

    public LockUserRoom(long id, int roomId, int userId, long timeLock, long timeDuration) {
    	super(id, userId, TYPE);
//        this.id = id;
        this.roomId = roomId;
//        this.userId = userId;
//        this.timeLock = timeLock;
        this.timeDuration = timeDuration;
    }
    
    public LockUserRoom(long id, int roomId, int userId, long timeLock, long timeDuration, int status) {
    	super(id, userId, TYPE, status, timeLock);
    	this.roomId = roomId;
    	this.timeDuration = timeDuration;
    }

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }

//    public long getTimeLock() {
//        return timeLock;
//    }
//
//    public void setTimeLock(long timeLock) {
//        this.timeLock = timeLock;
//    }

    public long getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(long timeDuration) {
        this.timeDuration = timeDuration;
    }

    @Override
    public String toString() {
        return "LockUserRoom{" + "id=" + getId() + ", roomId=" + roomId + ", userId=" + getUserId() + ", timeLock=" + getTime() + ", timeDuration=" + timeDuration + '}';
    }
    
    
}
