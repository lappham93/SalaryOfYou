package com.mit.entities.user;

import java.util.ArrayList;
import java.util.List;

public class UserStat {
	public enum ListType {
		FOLLOWING(1), FOLLOWER(2), HFFU(3), HFTU(4), EVENT_FAVORITE(5), BOOTH_FAVORITE(6), PRODUCT_FAVORITE(7), HIDDEN_FEED(8);
		
		private int value;
		
		private ListType(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	private long userId;
	private List<Long> hffuIds; //hide feeds from user
	private List<Long> followUserIds; //following
	private List<Long> userFollowIds; //followed
	private List<Long> hftuIds; // hide my feeds to user
	private List<Long> feedHides; // hide feeds to me
	private List<Long> eventIds;
	private List<Long> boothIds;
	private List<Long> productIds;
	private long createTime;
	private long updateTime;
	
	public UserStat(long userId) {
		this.userId = userId;
	}
	
	public UserStat(long userId, long destUserId, boolean isFollow, boolean isActive) {
		this.userId = userId;
		if (isActive) {
			addUserActive(destUserId, isFollow);
		} else {
			addUserPassive(destUserId, isFollow);
		}
	}
	
	public UserStat(long userId, List<Long> hffuIds, List<Long> followUserIds, List<Long> userFollowIds, List<Long> hftuIds, List<Long> feedHides, 
			List<Long> eventIds, List<Long> boothIds, List<Long> productIds) {
		this.userId = userId;
		this.hffuIds = hffuIds;
		this.followUserIds = followUserIds;
		this.userFollowIds = userFollowIds;
		this.hftuIds = hftuIds;
		this.feedHides = feedHides;
		this.eventIds = eventIds;
		this.boothIds = boothIds;
		this.productIds = productIds;
	}
	
	public UserStat(long userId, List<Long> hffuIds, List<Long> followUserIds, List<Long> userFollowIds, List<Long> hftuIds, List<Long> feedHides, 
			List<Long> eventIds, List<Long> boothIds, List<Long> productIds,long createTime, long updateTime) {
		this.userId = userId;
		this.hffuIds = hffuIds;
		this.followUserIds = followUserIds;
		this.userFollowIds = userFollowIds;
		this.hftuIds = hftuIds;
		this.feedHides = feedHides;
		this.eventIds = eventIds;
		this.boothIds = boothIds;
		this.productIds = productIds;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<Long> getFollowUserIds() {
		return followUserIds;
	}

	public void setFollowUserIds(List<Long> followUserIds) {
		this.followUserIds = followUserIds;
	}

	public List<Long> getUserFollowIds() {
		return userFollowIds;
	}

	public void setUserFollowIds(List<Long> userFollowIds) {
		this.userFollowIds = userFollowIds;
	}

	public List<Long> getHffuIds() {
		return hffuIds;
	}

	public void setHffuIds(List<Long> hffuIds) {
		this.hffuIds = hffuIds;
	}

	public List<Long> getHftuIds() {
		return hftuIds;
	}

	public void setHftuIds(List<Long> hftuIds) {
		this.hftuIds = hftuIds;
	}

	public List<Long> getFeedHides() {
		return feedHides;
	}

	public void setFeedHides(List<Long> feedHides) {
		this.feedHides = feedHides;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	
	public List<Long> getEventIds() {
		return eventIds;
	}

	public void setEventIds(List<Long> eventIds) {
		this.eventIds = eventIds;
	}

	public List<Long> getBoothIds() {
		return boothIds;
	}

	public void setBoothIds(List<Long> boothIds) {
		this.boothIds = boothIds;
	}

	public List<Long> getProductIds() {
		return productIds;
	}

	public void setProductIds(List<Long> productIds) {
		this.productIds = productIds;
	}

	private void addHFFU(long hffuId) {
		if (this.hffuIds == null) {
			this.hffuIds = new ArrayList<Long>();
		}
		if (this.hffuIds.contains(hffuId) == false) {
			this.hffuIds.add(0, hffuId);
		}
	}
	
	private void removeHFFU(long hffuId) {
		if (this.hffuIds != null) {
			this.hffuIds.remove(hffuId);
		}
	}
	
	private void addFollowUser(long followUserId) {
		if (this.followUserIds == null) {
			this.followUserIds = new ArrayList<Long>();
		}
		if (!this.followUserIds.contains(followUserId)) {
			this.followUserIds.add(0, followUserId);
		}
	}
	
	private void removeFollowUser(long followUser) {
		if (this.followUserIds != null) {
			this.followUserIds.remove(followUser);
		}
	}
	
	private void addUserFollow(long followUserId) {
		if (this.userFollowIds == null) {
			this.userFollowIds = new ArrayList<Long>();
		}
		if (!this.userFollowIds.contains(followUserId)) {
			this.userFollowIds.add(0, followUserId);
		}
	}
	
	private void removeUserFollow(long followUser) {
		if (this.userFollowIds != null) {
			this.userFollowIds.remove(followUser);
		}
	}
	
	private void addHFTU(long hftuId) {
		if (this.hftuIds == null) {
			this.hftuIds = new ArrayList<Long>();
		}
		if (this.hftuIds.contains(hftuId) == false) {
			this.hftuIds.add(0, hftuId);
		}
	}
	
	private void removeHFTU(long hftuId) {
		if (this.hftuIds != null) {
			this.hftuIds.remove(hftuId);
		}
	}
	
	public void addUserActive(long userId, boolean isFollow) {
		if (isFollow) {
			addFollowUser(userId);
			removeHFFU(userId);
		} else {
			removeFollowUser(userId);
			addHFFU(userId);
		}
	}
	
	public void removeUserActive(long userId, boolean isFollow) {
		if (isFollow) {
			removeFollowUser(userId);
		} else {
			removeHFFU(userId);
		}
	}
	
	public void addUserPassive(long userId, boolean isFollowed) {
		if (isFollowed) {
			addUserFollow(userId);
			removeHFTU(userId);
		} else {
			removeUserFollow(userId);
			addHFTU(userId);
		}
	}
	
	public void removeUserPassive(long userId, boolean isFollowed) {
		if (isFollowed) {
			removeUserFollow(userId);
		} else {
			removeUserFollow(userId);
		}
	}
	
	public void addFeedHides(long feedId) {
		if (feedHides == null) {
			feedHides = new ArrayList<>();
		}
		if (!feedHides.contains(feedId)) {
			feedHides.add(feedId);
		}
	}
	
	public void addEvent(long eventId) {
		if (eventIds == null) {
			eventIds = new ArrayList<>();
		}
		if (!eventIds.contains(eventId)) {
			eventIds.add(0, eventId);
		}
	}
	
	public void addBooth(long boothId) {
		if (boothIds == null) {
			boothIds = new ArrayList<>();
		}
		if (!boothIds.contains(boothId)) {
			boothIds.add(0, boothId);
		}
	}
	
	public void addProduct(long productId) {
		if (productIds == null) {
			productIds = new ArrayList<>();
		}
		if (!productIds.contains(productId)) {
			productIds.add(0, productId);
		}
	}
	
	public void removeEvent(long eventId) {
		if (eventIds != null) {
			eventIds.remove(eventId);
		}
	}
	
	public void removeBooth(long boothId) {
		if (boothIds != null) {
			boothIds.remove(boothId);
		}
	}
	
	public void removeProduct(long productId) {
		if (productIds != null) {
			productIds.add(productId);
		}
	}
}
