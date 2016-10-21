package com.mit.models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mit.dao.notification.NewsDAO;
import com.mit.dao.notification.UserNewsDAO;
import com.mit.entities.notification.News;
import com.mit.entities.notification.UserNews;

public class NotificationModel {
	public static final NotificationModel Instance = new NotificationModel();
	
	private NotificationModel() {}
	
	public Map<String, Object> getNews(int userId, int from, int size) {
		Map<String, Object> rs = new HashMap<String, Object>();
		
		List<UserNews> userNews = UserNewsDAO.getInstance().getNews(userId, from, size+1);
		
		boolean hasMore = false;
		if (userNews.size() > size) {
			hasMore = true;
			userNews = userNews.subList(0, size);
		}
		
		List<Long> newsIds = new LinkedList<Long>();
		for (UserNews userNewz: userNews) {
			newsIds.add(userNewz.getuId());
		}
		
		List<News> news = NewsDAO.getInstance().getByListId(newsIds);
		
		List<UserNews.UserView> newsViews = new LinkedList<UserNews.UserView>();
		
		rs.put("news", newsViews);
		rs.put("hasMore", hasMore);
		
		return rs;
	}
}
