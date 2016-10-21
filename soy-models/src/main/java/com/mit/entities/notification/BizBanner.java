package com.mit.entities.notification;


public abstract class BizBanner {
	private long uId;
	private int bizId;
	private int type;
	private int status;
	
	public BizBanner() {}

	public BizBanner(long uId, int bizId, int type, int status) {
		super();
		this.uId = uId;
		this.bizId = bizId;
		this.type = type;
		this.status = status;
	}

	public long getuId() {
		return uId;
	}

	public void setuId(long uId) {
		this.uId = uId;
	}

	public int getBizId() {
		return bizId;
	}

	public void setBizId(int bizId) {
		this.bizId = bizId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public abstract UserView buildUserView();
	
	public static class UserView {
		private int type;
		
		protected UserView(BizBanner newsItem) {
			this.type = newsItem.getType();
		}

		public int getType() {
			return type;
		}
	}
}
