package com.mit.entities.notification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mit.entities.app.AppKey;
import com.mit.utils.LinkBuilder;



public class BoothNews extends News {
	public static final int TYPE = NewsType.BOOTH.getValue();
	
	private long boothId;
	private String msg;
	private long thumb;
	
	public BoothNews() {}

	public BoothNews(long uId, long boothId, String msg, long thumb, int event, int status) {
		super(uId, TYPE, event, status);
		this.boothId = boothId;
		this.msg = msg;
		this.thumb = thumb;
	}

	public long getBoothId() {
		return boothId;
	}

	public void setBoothId(long boothId) {
		this.boothId = boothId;
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
		id.put("boothId", getBoothId());
		return new NotificationItem(id, 0, userId, AppKey.CYOGEL, getType(), getMsg());
	}
	
	public MultiDestNotificationItem buildMultiDestNotificationItem(List<Integer> userIds) {
		Map<String, Long> id = new HashMap<String, Long>();
		id.put("boothId", getBoothId());
		return new MultiDestNotificationItem(id, 0, userIds, AppKey.CYOGEL, getType(), getMsg());
	}
	
	public static class UserView extends News.UserView {		
		private long boothId;
		private String msg;
		private String thumb;

		private UserView(BoothNews productNews) {
			super(productNews);
			this.boothId = productNews.getBoothId();
			this.msg = productNews.getMsg();
			this.thumb = LinkBuilder.buildBoothPhotoLink(productNews.getThumb());
		}

		public long getBoothId() {
			return boothId;
		}

		public String getMsg() {
			return msg;
		}

		public String getThumb() {
			return thumb;
		}	
	}
}
