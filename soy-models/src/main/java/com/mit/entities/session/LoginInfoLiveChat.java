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

package com.mit.entities.session;

/**
 *
 * @author nghiatc
 * @since Jun 23, 2016
 */
public class LoginInfoLiveChat {
    private int err;
	private String sessionKey;
	private int userId;
	private byte roleId;
    private String imei;
    private String sessionId;
    private long liveId;
    private long feedId;

    public LoginInfoLiveChat() {
    }

    public LoginInfoLiveChat(int err, String sessionKey, int userId, byte roleId, String imei, String sessionId, long liveId, long feedId) {
        this.err = err;
        this.sessionKey = sessionKey;
        this.userId = userId;
        this.roleId = roleId;
        this.imei = imei;
        this.sessionId = sessionId;
        this.liveId = liveId;
        this.feedId = feedId;
    }

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte getRoleId() {
        return roleId;
    }

    public void setRoleId(byte roleId) {
        this.roleId = roleId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getLiveId() {
        return liveId;
    }

    public void setLiveId(long liveId) {
        this.liveId = liveId;
    }

    public long getFeedId() {
        return feedId;
    }

    public void setFeedId(long feedId) {
        this.feedId = feedId;
    }

    @Override
    public String toString() {
        return "LoginInfoLiveChat{" + "err=" + err + ", sessionKey=" + sessionKey + ", userId=" + userId + ", roleId=" + roleId + ", imei=" + imei + ", sessionId=" + sessionId + ", liveId=" + liveId + ", feedId=" + feedId + '}';
    }
    
    
    
}
