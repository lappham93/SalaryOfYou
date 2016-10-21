package com.mit.entities.notification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mit.entities.app.AppKey;
import com.mit.utils.LinkBuilder;



public class ProductNews extends News {
	public static final int TYPE = NewsType.PRODUCT.getValue();
	
	private long productId;
	private long skuId;
	private String msg;
	private long thumb;
	
	public ProductNews() {}

	public ProductNews(long uId, long productId, long skuId, String msg, long thumb, int event, int status) {
		super(uId, TYPE, event, status);
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
	
	public NotificationItem buildNotificationItem(int userId) {
		Map<String, Long> id = new HashMap<String, Long>();
		id.put("productId", getProductId());
		id.put("skuId", getSkuId());
		return new NotificationItem(id, 0, userId, AppKey.CYOGEL, getType(), getMsg());
	}
	
	public MultiDestNotificationItem buildMultiDestNotificationItem(List<Integer> userIds) {
		Map<String, Long> id = new HashMap<String, Long>();
		id.put("uId", getuId());
		id.put("productId", getProductId());
		id.put("skuId", getSkuId());
		return new MultiDestNotificationItem(id, 0, userIds, AppKey.CYOGEL, getType(), getMsg());
	}
	
	public static class UserView extends News.UserView {		
		private long productId;
		private long skuId;
		private String msg;
		private String thumb;

		private UserView(ProductNews productNews) {
			super(productNews);
			this.productId = productNews.getProductId();
			this.skuId = productNews.getSkuId();
			this.msg = productNews.getMsg();
			this.thumb = LinkBuilder.buildNewsThumbLink(productNews.getThumb());
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
