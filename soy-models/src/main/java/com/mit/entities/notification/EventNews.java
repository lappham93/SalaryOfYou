package com.mit.entities.notification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mit.entities.app.AppKey;
import com.mit.utils.LinkBuilder;



public class EventNews extends News {
	public static final int TYPE = NewsType.EVENT.getValue();
	
	private long eventId;
	private String msg;
	private long thumb;
	
	public EventNews() {}

	public EventNews(long uId, long eventId, String msg, long thumb, int event, int status) {
		super(uId, TYPE, event, status);
		this.eventId = eventId;
		this.msg = msg;
		this.thumb = thumb;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
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
		Map<String, Long> id = new HashMap<String, Long>();
		id.put("eventId", getEventId());
		return new NotificationItem(id, 0, userId, AppKey.CYOGEL, getType(), getMsg());
	}
	
	public MultiDestNotificationItem buildMultiDestNotificationItem(List<Integer> userIds) {
		Map<String, Long> id = new HashMap<String, Long>();
		id.put("eventId", getEventId());
		return new MultiDestNotificationItem(id, 0, userIds, AppKey.CYOGEL, getType(), getMsg());
	}
	
	public static class UserView extends News.UserView {		
		private long eventId;
		private String msg;
		private String thumb;

		private UserView(EventNews productNews) {
			super(productNews);
			this.eventId = productNews.getEventId();
			this.msg = productNews.getMsg();
			this.thumb = LinkBuilder.buildEventPhotoLink(productNews.getThumb());
		}

		public long getEventId() {
			return eventId;
		}

		public String getMsg() {
			return msg;
		}

		public String getThumb() {
			return thumb;
		}	
	}
}
