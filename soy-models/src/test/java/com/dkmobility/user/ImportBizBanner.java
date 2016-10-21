package com.dkmobility.user;

import com.mit.dao.notification.BizBannerDAO;
import com.mit.entities.notification.BizBanner;
import com.mit.entities.notification.BizProductBanner;
import com.mit.entities.notification.BizVideoBanner;
import com.mit.entities.notification.BizWebBanner;
import com.mit.entities.notification.BizWelcomeBanner;

public class ImportBizBanner {
	public static void main(String[] args) {
		int bizId = 21;
		
		BizBanner banner1 = new BizWelcomeBanner(0, bizId, "Welcome CYOGEL", 0, 1);
		BizBannerDAO.getInstance().insert(banner1);
		
		BizBanner banner2 = new BizProductBanner(0, bizId, 1, 1, "Gel Mix", 0, 1);
		BizBannerDAO.getInstance().insert(banner2);
		
		BizBanner banner3 = new BizVideoBanner(0, bizId, "https://www.youtube.com/watch?v=KwIg5KLeCtY", "CYOGEL", 0, 1);
		BizBannerDAO.getInstance().insert(banner3);
		
		BizBanner banner4 = new BizWebBanner(0, bizId, "https://cyogel.com/", "CYOGEL Homepage", 0, 1);
		BizBannerDAO.getInstance().insert(banner4);
	}
}
