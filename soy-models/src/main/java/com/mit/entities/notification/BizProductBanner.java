package com.mit.entities.notification;

import com.mit.utils.LinkBuilder;



public class BizProductBanner extends BizBanner {
	public static final int TYPE = BannerType.PRODUCT.getValue();
	
	private long productId;
	private long skuId;
	private String msg;
	private long thumb;
	
	public BizProductBanner() {}

	public BizProductBanner(long uId, int bizId, long productId, long skuId, String msg, long thumb, int status) {
		super(uId, bizId, TYPE, status);
		this.productId = productId;
		this.skuId = skuId;
		this.msg = msg;
		this.thumb = thumb;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
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
		private long productId;
		private long skuId;
		private String msg;
		private String thumb;

		private UserView(BizProductBanner productBanner) {
			super(productBanner);
			this.productId = productBanner.getProductId();
			this.skuId = productBanner.getSkuId();
			this.msg = productBanner.getMsg();
			this.thumb = LinkBuilder.buildBannerThumbLink(productBanner.getThumb());
		}

		public long getProductId() {
			return productId;
		}

		public long getSkuId() {
			return skuId;
		}

		public String getMsg() {
			return msg;
		}

		public String getThumb() {
			return thumb;
		}	
	}
}
