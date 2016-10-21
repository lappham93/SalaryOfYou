package com.mit.entities.stream;

import com.mit.common.conts.Common;
import com.mit.entities.user.UserInfo;

public class SocialStream {
	public enum SocialStreamStatus {
		DELETE(0), ACTIVE(1);
		
		private int value;
		private SocialStreamStatus(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}		
	}
	
	private long id;
	private int userId;
	private String name;
	private String title;
	private String token;
	private long feedId;
	private int status;
	private long createTime;
	private long updateTime;

	public SocialStream(long id, int userId, String name, String title,
			String token, long feedId, int status, long createTime,
			long updateTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.title = title;
		this.token = token;
		this.feedId = feedId;
		this.status = status;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getFeedId() {
		return feedId;
	}

	public void setFeedId(long feedId) {
		this.feedId = feedId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	
	public UserView buildUserView() {
		return new UserView(this);
	}
	
	public static class UserView {
		UserInfo.SocialView userInfo;
		private String title;
		private String streamUrl;
		private String rtspStreamUrl;
		private String chatUrl;
		private long liveId;
		private long feedId;
		private long createTime;
		
		private UserView(SocialStream ss) {
			this.title = ss.getTitle();
//			this.streamUrl = "http://115.79.45.86:1935/liveedge/" + ss.getName() + "/playlist.m3u8";
			this.streamUrl = "rtmp://115.79.45.86:1935/liveedge/" + ss.getName();
			this.rtspStreamUrl = "rtsp://115.79.45.86:1935/liveedge/" + ss.getName();
			this.chatUrl = Common.DOMAIN_CHAT_LIVE + "/socket/live";
			this.liveId = ss.getId();	
			this.feedId = ss.getFeedId();
			this.createTime = ss.getCreateTime();
		}

		public UserView setUserInfo(UserInfo.SocialView userInfo) {
			this.userInfo = userInfo;
			return this;
		}

		public UserInfo.SocialView getUserInfo() {
			return userInfo;
		}

		public String getTitle() {
			return title;
		}

		public String getStreamUrl() {
			return streamUrl;
		}

		public String getRtspStreamUrl() {
			return rtspStreamUrl;
		}

		public String getChatUrl() {
			return chatUrl;
		}

		public long getLiveId() {
			return liveId;
		}

		public long getFeedId() {
			return feedId;
		}

		public long getCreateTime() {
			return createTime;
		}
		
	}
}
