package com.mit.models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mit.dao.notification.NewsDAO;
import com.mit.dao.notification.UserNewsDAO;
import com.mit.entities.notification.CNDLevelNews;
import com.mit.entities.notification.News;
import com.mit.entities.notification.NotificationItem;
import com.mit.entities.notification.UserNews;
import com.mit.luv.kafka.producer.ProducerPush;
import com.mit.luv.kafka.producer.ProducerTopic;
import com.mit.utils.JsonUtils;

public class NewsModel {
	public static final NewsModel Instance = new NewsModel();

	private NewsModel() {
	}

	public Map<String, Object> getNewsList(int userId, int from, int size) {
		Map<String, Object> rs = new HashMap<String, Object>();

		List<UserNews> userNews = UserNewsDAO.getInstance().getNews(userId, from, size + 1);

		boolean hasMore = false;
		if (userNews.size() > size) {
			hasMore = true;
			userNews = userNews.subList(0, size);
		}

		List<Long> newsIds = new LinkedList<Long>();
		// List<Long> unviewNewsIds = new LinkedList<Long>();
		for (UserNews userNewz : userNews) {
			newsIds.add(userNewz.getuId());
		}

		Map<Long, News> news = NewsDAO.getInstance().getMapByListId(newsIds);
		List<UserNews.UserView> userNewsViews = new LinkedList<UserNews.UserView>();
		for (UserNews userNewz : userNews) {
			News newz = news.get(userNewz.getuId());
			if (newz != null) {
				userNewsViews.add(userNewz.buildUserView().setContent(newz.buildUserView()));
			}
		}

		UserNewsDAO.getInstance().resetCount(userId);

		rs.put("news", userNewsViews);
		rs.put("hasMore", hasMore);
		rs.put("newCount", UserNewsDAO.getInstance().countNewItems(userId));

		return rs;
	}
	
	public void viewNews(int userId, List<Long> newsIds) {
		 if (newsIds.size() > 0) {
			 ProducerPush.send(ProducerTopic.NEWS_VIEW, System.currentTimeMillis()
			 + "\t" + userId + "\t" + StringUtils.join(newsIds, ","));
		 }
	}

	public Map<String, Object> getNewsCount(int userId) {
		Map<String, Object> rs = new HashMap<String, Object>();
		rs.put("newCount", UserNewsDAO.getInstance().countNewItems(userId));
		return rs;
	}

	public void notifyProduct(long productId, List<Long> userIds) {
		int partSize = 10000;
		for (int i = 0; i < userIds.size(); i += partSize) {
			List<Long> part = userIds.subList(i, Math.min(i + partSize, userIds.size()));
			ProducerPush.send(ProducerTopic.NEWS_PRODUCT,
					System.currentTimeMillis() + "\t" + productId + "\t" + StringUtils.join(part, ","));
		}
	}

	public void notifyCndLevel(String msg, long photo, int userId) {
		CNDLevelNews news = new CNDLevelNews(0, msg, photo, News.EVENT_NONE, News.ACTIVE);

		if (news != null) {
			int rsCode = NewsDAO.getInstance().insert(news);

			if (rsCode >= 0) {
				UserNews userNews = new UserNews(news.getuId(), false, 0);
				UserNewsDAO.getInstance().addItem(userId, userNews);
			}

			NotificationItem notiItem = news.buildNotificationItem(userId);
			byte[] msgData = JsonUtils.Instance.toByteJson(notiItem);
			ProducerPush.send(ProducerTopic.NOTIFICATION_ANDROID, msgData);
			ProducerPush.send(ProducerTopic.NOTIFICATION_IOS, msgData);
		}
	}

	public void notifyWeb(long webId, List<Long> userIds) {
		int maxSize = 10000;
		for (int i = 0; i < userIds.size(); i += maxSize) {
			List<Long> part = userIds.subList(i, Math.min(userIds.size(), i + maxSize));
			ProducerPush.send(ProducerTopic.NEWS_WEB,
					System.currentTimeMillis() + "\t" + webId + "\t" + StringUtils.join(part, ","));
		}
	}
	
	public void notifyEvent(long eventId, String message, List<Long> userIds) {
		int maxSize = 10000;
		for (int i = 0; i < userIds.size(); i += maxSize) {
			List<Long> part = userIds.subList(i, Math.min(userIds.size(), i + maxSize));
			ProducerPush.send(ProducerTopic.NEWS_EVENT,
					System.currentTimeMillis() + "\t" + eventId + "\t" + message + "\t" + StringUtils.join(part, ","));
		}
	}
	
	public void notifyBooth(long boothId, String message, List<Long> userIds) {
		int maxSize = 10000;
		for (int i = 0; i < userIds.size(); i += maxSize) {
			List<Long> part = userIds.subList(i, Math.min(userIds.size(), i + maxSize));
			ProducerPush.send(ProducerTopic.NEWS_BOOTH,
					System.currentTimeMillis() + "\t" + boothId + "\t" + message + "\t" + StringUtils.join(part, ","));
		}
	}
	
	public static void main(String[] args) {
	}
}
