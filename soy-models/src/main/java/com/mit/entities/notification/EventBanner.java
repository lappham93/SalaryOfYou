package com.mit.entities.notification;

import com.mit.utils.LinkBuilder;


public class EventBanner extends Banner {	
	public static final int TYPE = BannerType.EVENT.getValue();
	
	private long eventId;
	private String msg;
	private long thumb;
	
	public EventBanner() {}

	public EventBanner(long uId, long eventId, long ownId, int ownType, String msg, long thumb, int status) {
		super(uId, ownId, ownType, TYPE, status);
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
	
	public static class UserView extends Banner.UserView {		
		private long eventId;
		private String msg;
		private String thumb;

		private UserView(EventBanner webBanner) {
			super(webBanner);
			eventId = webBanner.getEventId();
			this.msg = webBanner.getMsg();
			this.thumb = LinkBuilder.buildBannerThumbLink(webBanner.getThumb());
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

    @Override
    public String toString() {
        return "WebBanner{" + "id=" + eventId + ", msg=" + msg + ", thumb=" + thumb + ", status" + this.getStatus() + ", " + '}';
    }
    
    
}
