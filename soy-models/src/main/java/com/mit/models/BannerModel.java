package com.mit.models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mit.dao.notification.BannerDAO;
import com.mit.dao.notification.BizBannerDAO;
import com.mit.entities.notification.Banner;
import com.mit.entities.notification.BannerOwnType;
import com.mit.entities.notification.BizBanner;

public class BannerModel {
	public static final BannerModel Instance = new BannerModel();
	
	private BannerModel() {}
	
	public Map<String, Object> getBannerList(int from, int size) {
		Map<String, Object> rs = new HashMap<String, Object>();
		
		List<Banner> banners = BannerDAO.getInstance().getSlice(0L, BannerOwnType.HOME.getValue(), from, size+1);
		
		boolean hasMore = false;
		if (banners.size() > size) {
			hasMore = true;
			banners = banners.subList(0, size);
		}
		
		List<Banner.UserView> bannerViews = new LinkedList<Banner.UserView>();
		for (Banner banner: banners) {
			bannerViews.add(banner.buildUserView());
		}
		
		rs.put("banners", bannerViews);
		rs.put("hasMore", hasMore);
		
		return rs;
	}
	
	public Map<String, Object> getEventBannerList(long eventId, int from, int size) {
		Map<String, Object> rs = new HashMap<String, Object>();
		
		List<Banner> banners = BannerDAO.getInstance().getSlice(eventId, BannerOwnType.EVENT.getValue(), from, size+1);
		
		boolean hasMore = false;
		if (banners.size() > size) {
			hasMore = true;
			banners = banners.subList(0, size);
		}
		
		List<Banner.UserView> bannerViews = new LinkedList<Banner.UserView>();
		for (Banner banner: banners) {
			bannerViews.add(banner.buildUserView());
		}
		
		rs.put("banners", bannerViews);
		rs.put("hasMore", hasMore);
		
		return rs;
	}
	
	public List<Banner.UserView> getEventBannerList(long eventId) {
		List<Banner> banners = BannerDAO.getInstance().getSlice(eventId, BannerOwnType.EVENT.getValue(), 0, 10);
		List<Banner.UserView> bannerViews = new LinkedList<Banner.UserView>();
		if (banners != null) {
			for (Banner banner: banners) {
				bannerViews.add(banner.buildUserView());
			}
		}
		
		return bannerViews;
	}
	
	public List<Banner.UserView> getBoothBannerList(long boothId) {
		List<Banner> banners = BannerDAO.getInstance().getSlice(boothId, BannerOwnType.BOOTH.getValue(), 0, 10);
		List<Banner.UserView> bannerViews = new LinkedList<Banner.UserView>();
		if (banners != null) {
			for (Banner banner: banners) {
				bannerViews.add(banner.buildUserView());
			}
		}
		
		return bannerViews;
	}
	
	public Map<String, Object> getBoothBannerList(long boothId, int from, int size) {
		Map<String, Object> rs = new HashMap<String, Object>();
		
		List<Banner> banners = BannerDAO.getInstance().getSlice(boothId, BannerOwnType.BOOTH.getValue(), from, size+1);
		
		boolean hasMore = false;
		if (banners.size() > size) {
			hasMore = true;
			banners = banners.subList(0, size);
		}
		
		List<Banner.UserView> bannerViews = new LinkedList<Banner.UserView>();
		for (Banner banner: banners) {
			bannerViews.add(banner.buildUserView());
		}
		
		rs.put("banners", bannerViews);
		rs.put("hasMore", hasMore);
		
		return rs;
	}
	
	public Map<String, Object> getBizBannerList(int bizId, int from, int size) {
		Map<String, Object> rs = new HashMap<String, Object>();
		
		List<BizBanner> banners = BizBannerDAO.getInstance().getSlice(bizId, from, size+1);
		
		boolean hasMore = false;
		if (banners.size() > size) {
			hasMore = true;
			banners = banners.subList(0, size);
		}
		
		List<BizBanner.UserView> bannerViews = new LinkedList<BizBanner.UserView>();
		for (BizBanner banner: banners) {
			bannerViews.add(banner.buildUserView());
		}
		
		rs.put("banners", bannerViews);
		rs.put("hasMore", hasMore);
		
		return rs;
	}
	
	public Map<String, Object> getBizBannerList(int bizId) {
		Map<String, Object> rs = new HashMap<String, Object>();
		
		List<BizBanner> banners = BizBannerDAO.getInstance().getListByBizId(bizId);
		
		List<BizBanner.UserView> bannerViews = new LinkedList<BizBanner.UserView>();
		for (BizBanner banner: banners) {
			bannerViews.add(banner.buildUserView());
		}
		
		rs.put("banners", bannerViews);
		
		return rs;
	}
	
	public Map<String, Object> getMultipleBizBannerList(List<Integer> bizIds) {
		Map<String, Object> rs = new HashMap<String, Object>();
		
		Map<Integer, List<BizBanner>> banners = BizBannerDAO.getInstance().getByBizIdList(bizIds);
		
		Map<Integer, List<BizBanner.UserView>> bannerViews = new HashMap<Integer, List<BizBanner.UserView>>();
		for (int bizId: banners.keySet()) {
			bannerViews.put(bizId, new LinkedList<BizBanner.UserView>());
			
			for (BizBanner banner: banners.get(bizId)) {
				bannerViews.get(bizId).add(banner.buildUserView());
			}
		}
		
		rs.put("banners", bannerViews);
		
		return rs;
	}
}
