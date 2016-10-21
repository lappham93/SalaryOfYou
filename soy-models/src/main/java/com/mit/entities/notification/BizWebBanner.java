package com.mit.entities.notification;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.mit.utils.LinkBuilder;


public class BizWebBanner extends BizBanner {	
	public static final int TYPE = BannerType.WEB.getValue();
	
	private String id;
	private String msg;
	private long thumb;
	
	public BizWebBanner() {}

	public BizWebBanner(long uId, int bizId, String id, String msg, long thumb, int status) {
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

		private UserView(BizWebBanner webBanner) {
			super(webBanner);
			try {
				this.id = URLDecoder.decode(webBanner.getId(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				this.id = webBanner.getId();
			}
			this.msg = webBanner.getMsg();
			this.thumb = LinkBuilder.buildBannerThumbLink(webBanner.getThumb());
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

    @Override
    public String toString() {
        return "WebBanner{" + "id=" + id + ", msg=" + msg + ", thumb=" + thumb + ", status" + this.getStatus() + ", " + '}';
    }
    
    
}
