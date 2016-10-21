package com.dkmobility.user;

import com.mit.dao.notification.NewsDAO;
import com.mit.entities.notification.News;
import com.mit.entities.notification.WelcomeNews;

public class ImportBanner {
	public static void main(String[] args) {
//		Banner banner1 = new WelcomeBanner(0, "Welcome bConnect", 0, 1);
//		BannerDAO.getInstance().insert(banner1);
//		
//		Banner banner2 = new ProductBanner(0, 1, 1, "Gel Mix", 0, 1);
//		BannerDAO.getInstance().insert(banner2);
//		
//		Banner banner3 = new VideoBanner(0, "https://www.youtube.com/watch?v=KwIg5KLeCtY", "CYOGEL", 0, 1);
//		BannerDAO.getInstance().insert(banner3);
//		
//		Banner banner4 = new WebBanner(0, "https://cyogel.com/", "CYOGEL Homepage", 0, 1);
//		BannerDAO.getInstance().insert(banner4);
		
		WelcomeNews news = new WelcomeNews(0, "Welcome to bConnect", 0, News.EVENT_REGISTER, 1);
		NewsDAO.getInstance().insert(news);
	}
}
