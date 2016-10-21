package com.mit.entities.notification;

import com.mit.utils.LinkBuilder;


public class BoothBanner extends Banner {	
	public static final int TYPE = BannerType.BOOTH.getValue();
	
	private long eventId;
	private long boothId;
	private String msg;
	private long thumb;
	
	public BoothBanner() {}

	public BoothBanner(long uId, long eventId, long boothId, long ownId, int ownType, String msg, long thumb, int status) {
		super(uId, ownId, ownType, TYPE, status);
		this.boothId = boothId;
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
	
	public static class UserView extends Banner.UserView {		
		private long boothId;
		private String msg;
		private String thumb;

		private UserView(BoothBanner webBanner) {
			super(webBanner);
			boothId = webBanner.getBoothId();
			this.msg = webBanner.getMsg();
			this.thumb = LinkBuilder.buildBannerThumbLink(webBanner.getThumb());
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

    @Override
    public String toString() {
        return "WebBanner{" + "id=" + boothId + ", msg=" + msg + ", thumb=" + thumb + ", status" + this.getStatus() + ", " + '}';
    }
    
    
}
