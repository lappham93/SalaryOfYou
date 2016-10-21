package com.mit.entities.notification;


public abstract class Banner {
	private long uId;
	private long ownId; // 0 if it belong to home
	private int ownType;
	private int type;
	private int status;
	
	public Banner() {}

	public Banner(long uId, long ownId, int ownType, int type, int status) {
		super();
		this.uId = uId;
		this.ownId = ownId;
		this.ownType = ownType;
		this.type = type;
		this.status = status;
	}

	public long getuId() {
		return uId;
	}

	public void setuId(long uId) {
		this.uId = uId;
	}

	public int getOwnType() {
		return ownType;
	}

	public void setOwnType(int ownType) {
		this.ownType = ownType;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public long getOwnId() {
		return ownId;
	}

	public void setOwnId(long ownId) {
		this.ownId = ownId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public abstract UserView buildUserView();
	
	public abstract void setThumb(long thumb);
	
	public static class UserView {
		private int type;
		
		protected UserView(Banner newsItem) {
			this.type = newsItem.getType();
		}

		public int getType() {
			return type;
		}
	}
}
