package com.mit.entities.notification;

import java.util.HashMap;
import java.util.Map;

import com.mit.entities.app.AppKey;
import com.mit.utils.LinkBuilder;


public class OrderNews extends News {	
	public static final int TYPE = NewsType.ORDER.getValue();
	
	private String id;
	private String msg;
	private long thumb;
	
	public OrderNews() {}

	public OrderNews(long uId, String id, String msg, long thumb, int event, int status) {
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
		Map<String, Object> id = new HashMap<>();
		id.put("uId", getuId());
		id.put("orderId", getId());
		return new NotificationItem(id, 0, userId, AppKey.CYOGEL, getType(), getMsg());
	}
	
	public static class UserView extends News.UserView {		
		private String id;
		private String msg;
		private String thumb;

		private UserView(OrderNews orderNews) {
			super(orderNews);
			this.id = orderNews.getId();
			this.msg = orderNews.getMsg();
			this.thumb = LinkBuilder.buildProductPhotoLink(orderNews.getThumb());
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
