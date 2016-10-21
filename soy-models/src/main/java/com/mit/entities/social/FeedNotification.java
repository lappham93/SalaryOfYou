package com.mit.entities.social;

import java.util.ArrayList;
import java.util.List;

import com.mit.entities.react.React;

public class FeedNotification extends React{
	
	private long userId;
	private List<Long> otherUserIds;
	
	public FeedNotification(int objectType, long objectId, long userId, List<Long> otherUserIds) {
		super(objectType, objectId);
		this.userId = userId;
		this.otherUserIds = otherUserIds;
	}
	
	public void addOtherUserId(long otherUserId) {
		if (otherUserId != userId) {
			if (otherUserIds == null) {
				otherUserIds = new ArrayList<>();
			}
			if (!otherUserIds.contains(otherUserId)) {
				otherUserIds.add(otherUserId);
			}
		}
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<Long> getOtherUserIds() {
		return otherUserIds;
	}

	public void setOtherUserIds(List<Long> otherUserIds) {
		this.otherUserIds = otherUserIds;
	}
	
}
