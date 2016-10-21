package com.mit.entities.notification;

import java.util.HashMap;
import java.util.Map;

import com.mit.entities.app.AppKey;
import com.mit.utils.LinkBuilder;


public class WelcomeNews extends News {	
	public static final int TYPE = NewsType.WELCOME.getValue();
	
	private String msg;
	private long thumb;
	
	public WelcomeNews() {}

	public WelcomeNews(long uId, String msg, long thumb, int event, int status) {
		super(uId, TYPE, event, status);
		this.msg = msg;
		this.thumb = thumb;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getThumb() {
		return thumb;
	}

	public void setThumb(long thumb) {
		this.thumb = thumb;
	}
	
	public UserView buildUserView() {
		return new UserView(this);
	}
	
	public NotificationItem buildNotificationItem(int userId) {		
		Map<String, Object> id = new HashMap<>();
		id.put("uId", getuId());
		return new NotificationItem(id, 0, userId, AppKey.CYOGEL, getType(), getMsg());
	}
	
	public static class UserView extends News.UserView {	
		private String msg;
		private String thumb;

		private UserView(WelcomeNews welcomeNews) {
			super(welcomeNews);
			this.msg = welcomeNews.getMsg();
			this.thumb = LinkBuilder.buildNewsThumbLink(welcomeNews.getThumb());
		}

		public String getMsg() {
			return msg;
		}

		public String getThumb() {
			return thumb;
		}	
	}
}
