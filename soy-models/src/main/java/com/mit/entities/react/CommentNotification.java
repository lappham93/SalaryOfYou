package com.mit.entities.react;

import java.util.ArrayList;
import java.util.List;

public class CommentNotification {
	
	private long commentId;
	private int cmtUserId;		// comment own
	private long feedId;
	private List<Integer> userIds; 	// list user reply
	
	public CommentNotification(long commentId, int cmtUserId, long feedId, List<Integer> userIds) {
		super();
		this.commentId = commentId;
		this.cmtUserId = cmtUserId;
		this.feedId = feedId;
		this.userIds = userIds;
	}
	
	public void addUserReply(int userId) {
		if (userId != cmtUserId) {
			if (userIds == null) {
				userIds = new ArrayList<Integer>();
			}
			userIds.add(userId);
		}
	}
	
	public void rmUserReply(int userId) {
		if (userIds != null && userIds.contains(userId)) {
			userIds.remove(new Integer(userId));
		}
	}

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public int getCmtUserId() {
		return cmtUserId;
	}

	public void setCmtUserId(int cmtUserId) {
		this.cmtUserId = cmtUserId;
	}

	public long getFeedId() {
		return feedId;
	}

	public void setFeedId(long feedId) {
		this.feedId = feedId;
	}

	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}
	
}
