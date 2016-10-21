package com.mit.entities.notification;

import com.mit.utils.LinkBuilder;


public class BizWelcomeBanner extends BizBanner {	
	public static final int TYPE = BannerType.WELCOME.getValue();
	
	private String msg;
	private long thumb;
	
	public BizWelcomeBanner() {}

	public BizWelcomeBanner(long uId, int bizId, String msg, long thumb, int status) {
		super(uId, bizId, TYPE, status);
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
	
	public static class UserView extends BizBanner.UserView {	
		private String msg;
		private String thumb;

		private UserView(BizWelcomeBanner welcomeBanner) {
			super(welcomeBanner);
			this.msg = welcomeBanner.getMsg();
			this.thumb = LinkBuilder.buildBannerThumbLink(welcomeBanner.getThumb());
		}

		public String getMsg() {
			return msg;
		}

		public String getThumb() {
			return thumb;
		}	
	}
}
