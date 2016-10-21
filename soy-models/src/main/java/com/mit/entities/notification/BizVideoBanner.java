package com.mit.entities.notification;

import com.mit.utils.LinkBuilder;


public class BizVideoBanner extends BizBanner {	
	public static final int TYPE = BannerType.VIDEO.getValue();
	
	private String id;
	private String msg;
	private long thumb;
	
	public BizVideoBanner() {}

	public BizVideoBanner(long uId, int bizId, String id, String msg, long thumb, int status) {
		super(uId, bizId, TYPE, status);
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
	
	public static class UserView extends BizBanner.UserView {		
		private String id;
		private String msg;
		private String thumb;

		private UserView(BizVideoBanner videoBanner) {
			super(videoBanner);
			this.id = videoBanner.getId();
			this.msg = videoBanner.getMsg();
			this.thumb = LinkBuilder.buildBannerThumbLink(videoBanner.getThumb());
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
