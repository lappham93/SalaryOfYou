package com.mit.entities.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mit.dao.social.FeedDAO;
import com.mit.dao.user.UserInfoDAO;
import com.mit.entities.app.AppKey;
import com.mit.entities.social.Feed;
import com.mit.entities.user.UserInfo;
import com.mit.utils.LinkBuilder;

public class SocialNews extends News{
	public static final int TYPE = NewsType.SOCIAL.getValue();
	
	public enum SocialNewsType {
		LIKE_FEED(1, "liked your feed", "liked your friend's feed"),
		COMMENT(2, "commented your feed", "commented your friend's feed"),
		LIKE_COMMENT(3, "liked your comment", "liked your comment"),
		REPLY(4, "replied your comment", "replied on your feed"),
		LIKE_REPLY(5, "liked your comment", "liked your comment"),
		POST_FEED(6, "posted a feed ", "posted a feed");
		
		private int value;
		private String message;
		private String omessage;
		
		private SocialNewsType(int value, String message, String omessage) {
			
			this.value = value;
			this.message = message;
			this.omessage = omessage;
		}
		
		public int getValue() {
			return value;
		}
		
		public String getMessage() {
			return message;
		}
		
		public String getOtherMessage() {
			return omessage;
		}
		
		public static SocialNewsType getSocialNewsType(int value) {
			for (SocialNewsType type : SocialNewsType.values()) {
				if (type.getValue() == value) {
					return type;
				}
			}
			return null;
		}
	}
	
	private long userId;
	private long feedId;
	private long photoId;
	private long cmtId;
	private List<Long> fromUserIds;
	private int socialNewsType;
	
	public SocialNews(long userId, long uId, int event, int status, long feedId, long photoId, List<Long> userIds, int socialNewsType, long commentId) {
		super(uId, TYPE, event, status);
		this.userId = userId;
		this.feedId = feedId;
		this.cmtId = commentId;
		this.fromUserIds = userIds;
		this.photoId = photoId;
		this.socialNewsType = socialNewsType;
	}
	
	public void addFromUser(long userId) {
		if (fromUserIds == null) {
			fromUserIds = new ArrayList<>();
		}
		if (userId != this.userId) {
			this.fromUserIds.remove(userId);
			fromUserIds.add(0, userId);
		}
	}
	
	public long getUserId() {
		return userId;
	}
	
	public long getFeedId() {
		return feedId;
	}
	
	public long getPhotoId() {
		return photoId;
	}

	public void setFeedId(long feedId) {
		this.feedId = feedId;
	}
	
	public long getCommentId() {
		return cmtId;
	}
	
	public void setCommentId(long cmtId) {
		this.cmtId = cmtId;
	}

	public List<Long> getFromUserIds() {
		return fromUserIds;
	}

	public void setFromUserIds(List<Long> userIds) {
		this.fromUserIds = userIds;
	}

	public int getSocialNewsType() {
		return socialNewsType;
	}

	public void setSocialNewsType(int socialNewsType) {
		this.socialNewsType = socialNewsType;
	}

	@Override
	public UserView buildUserView() {
		return new UserView(this);
	}

	@Override
	public NotificationItem buildNotificationItem(int userId) {
		String msg = "";
		long firstUser = fromUserIds.get(0);
		UserInfo ui = UserInfoDAO.getInstance().getById((int)firstUser);
		msg += ui.getFullName();
		Feed feed = FeedDAO.getInstance().getById(feedId);
		SocialNewsType snType = SocialNewsType.getSocialNewsType(socialNewsType);
		if (snType != null) {
			if (userId == feed.getUserId()) {
				msg += snType.getMessage();
			} else {
				msg += snType.getOtherMessage();
			}
		}
		Map<String, Object> id = new HashMap<>();
		id.put("uId", getuId());
		id.put("feedId", this.getFeedId());
		id.put("socialType", this.socialNewsType);
		id.put("photoId", this.getPhotoId() > 0 ? this.getPhotoId() : "");
		id.put("commentId", this.cmtId);
		return new NotificationItem(id, 0, userId, AppKey.CYOGEL, this.getType(), msg);
	}
	
	public static class UserView extends News.UserView {
		private long feedId;
		private String photoId;
		private long commentId;
		private List<Long> fromUserIds;
		private List<String> fromUserNames;
		private String msg;
		private String thumb;
		private int socialType;

		private UserView(SocialNews socialNews) {
			super(socialNews);
			this.fromUserIds = (List<Long>) (socialNews.getFromUserIds() != null ? socialNews.getFromUserIds() : Arrays.asList());
			fromUserNames = new ArrayList<>();
			for (long userId : fromUserIds) {
				UserInfo ui = UserInfoDAO.getInstance().getById((int)userId);
				fromUserNames.add(ui.getFullName());
			}
			long firstUserId = fromUserIds.get(0);
			UserInfo ui = UserInfoDAO.getInstance().getById((int)firstUserId);
			this.thumb = LinkBuilder.buildUserAvatarLink(ui.getPhoto());
			feedId = socialNews.getFeedId();
			Feed feed = FeedDAO.getInstance().getFeedIgnoreStatus(feedId);
			this.photoId = socialNews.getPhotoId() > 0 ? String.valueOf(socialNews.getPhotoId()) : "";
			this.socialType = socialNews.getSocialNewsType();
			this.commentId = socialNews.getCommentId();
			if (socialNews.getUserId() == feed.getUserId()) {
				this.msg = SocialNewsType.getSocialNewsType(socialType).getMessage();
			} else {
				this.msg = SocialNewsType.getSocialNewsType(socialType).getOtherMessage();
			}
		}
		
		public List<Long> getFromUserIds() {
			return fromUserIds;
		}
		
		public List<String> getFromUserNames() {
			return fromUserNames;
		}
		
		public String getMsg() {
			return msg;
		}

		public String getThumb() {
			return thumb;
		}
		
		public String getPhotoId() {
			return photoId;
		}
		
		public long getFeedId() {
			return feedId;
		}
		
		public long getCommentId() {
			return commentId;
		}
		
		public int getSocialType() {
			return socialType;
		}
		
	}

}
