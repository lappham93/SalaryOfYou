package com.mit.entities.notification;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mit.entities.app.AppKey;
import com.mit.utils.LinkBuilder;


public class WebNews extends News {	
	public static final int TYPE = NewsType.WEB.getValue();
	
	private String id;
	private String msg;
	private long thumb;
	
	public WebNews() {}

	public WebNews(long uId, String id, String msg, long thumb, int event, int status) {
		super(uId, TYPE, event, status);
		this.id = id;
		this.msg = msg;
		this.thumb = thumb;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		Map<String, Object> id = new HashMap<String, Object>();
		String url = "";
		try {
			url = URLDecoder.decode(getId(), "UTF-8");
		} catch (Exception e) {
			url = getId();
		}
		id.put("uId", getuId());
		id.put("url", url);
		return new NotificationItem(id, 0, userId, AppKey.CYOGEL, getType(), getMsg());
	}
	
	public MultiDestNotificationItem buildMultiDestNotificationItem(List<Integer> userIds) {
		Map<String, Object> id = new HashMap<String, Object>();
		String url = "";
		try {
			url = URLDecoder.decode(getId(), "UTF-8");
		} catch (Exception e) {
			url = getId();
		}
		id.put("uId", getuId());
		id.put("url", url);
		return new MultiDestNotificationItem(id, 0, userIds, AppKey.CYOGEL, getType(), getMsg());
	}
	
	public static class UserView extends News.UserView {		
		private String id;
		private String msg;
		private String thumb;

		private UserView(WebNews webNews) {
			super(webNews);
			try {
				this.id = URLDecoder.decode(webNews.getId(), "UTF-8");
			} catch (Exception e) {
				this.id = webNews.getId();
			}
			this.msg = webNews.getMsg();
			this.thumb = LinkBuilder.buildNewsThumbLink(webNews.getThumb());
		}
		
		public String getId() {
			return id;
		}

		public String getMsg() {
			return msg;
		}

		public String getThumb() {
			return thumb;
		}	
	}
}
