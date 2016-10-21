package com.mit.entities.react;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReactStat extends React {
	public static List<Integer> REP_ACTION = Arrays.asList(ReactType.COMMENT.getValue(), ReactType.SHARE.getValue());
	
	private long id;
	private int socialType; // refer SocialType enum
	private List<Long> userIds;
	private int total;
	private double totalRatingPoint; // use for rating

	public ReactStat(int objectType, long objectId, long id, int socialType, List<Long> userIds, int total) {
		super(objectType, objectId);
		this.id = id;
		this.socialType = socialType;
		this.userIds = userIds;
		this.total = total;
		this.totalRatingPoint = 0.0;
	}

	public ReactStat(int objectType, long objectId, long id, int socialType, List<Long> userIds, int total,
			double totalRatingPoint) {
		super(objectType, objectId);
		this.id = id;
		this.socialType = socialType;
		this.userIds = userIds;
		this.total = total;
		this.totalRatingPoint = totalRatingPoint;
	}

	public void addToUserIds(long userId) {
		if (userIds == null) {
			userIds = new ArrayList<Long>();
		}
		if (REP_ACTION.contains(this.getSocialType()) || !userIds.contains(userId)) {
			userIds.add(userId);
			total++;
		}
	}

	public void removeFromUserIds(long userId) {
		if (userIds != null && userIds.contains(userId)) {
			userIds.remove(userId);
			total--;
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSocialType() {
		return socialType;
	}

	public void setSocialType(int socialType) {
		this.socialType = socialType;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public double getTotalRatingPoint() {
		return totalRatingPoint;
	}

	public void setTotalRatingPoint(double totalRatingPoint) {
		this.totalRatingPoint = totalRatingPoint;
	}

}
