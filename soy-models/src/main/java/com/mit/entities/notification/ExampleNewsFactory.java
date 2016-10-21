package com.mit.entities.notification;

import java.util.HashMap;
import java.util.Map;

import com.mit.entities.app.AppKey;

public class ExampleNewsFactory {
	public static final ExampleNewsFactory Instance = new ExampleNewsFactory();
	
	private ExampleNewsFactory() {}
	
	public WebNews createWebNews() {
		return new WebNews(0, "https://cyogel.com/cyogel/customer-testimonials", "testimonial", 0, 0, 1);
	}
	
	public ProductNews createProductNews() {
		return new ProductNews(0, 1, 9, "Hot news. Sale 30% off  2oz mix", 0, 0, 1);
	}
	
	public OrderNews createOrderNews() {
		return new OrderNews(0, "1", "Hot news. Sale 30% off  2oz mix", 0, 0, 1);
	}
	
	public WelcomeNews createWelcomeNews() {
		return new WelcomeNews(0, "Welcome CYOGEL", 0, 0, 1);
	}
	
	public NotificationItem createWebItem(int userId) {
		WebNews news = createWebNews();
		NotificationItem item = new NotificationItem(news.getId(), 0, userId, AppKey.CYOGEL, news.getType(), news.getMsg());
		return item;
	}
	
	public NotificationItem createProductItem(int userId) {
		ProductNews news = createProductNews();
		Map<String, Long> id = new HashMap<String, Long>();
		id.put("productId", news.getProductId());
		id.put("skuId", news.getSkuId());
		NotificationItem item = new NotificationItem(id, 0, userId, AppKey.CYOGEL, news.getType(), news.getMsg());
		return item;
	}
	
	public NotificationItem createOrderItem(int userId) {
		OrderNews news = createOrderNews();
		NotificationItem item = new NotificationItem(news.getId(), 0, userId, AppKey.CYOGEL, news.getType(), news.getMsg());
		return item;
	}
	
	public NotificationItem createWelcomeItem(int userId) {
		WelcomeNews news = createWelcomeNews();
		NotificationItem item = new NotificationItem("", 0, userId, AppKey.CYOGEL, news.getType(), news.getMsg());
		return item;
	}
}
