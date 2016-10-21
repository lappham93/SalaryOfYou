package com.mit.entities.social;

import java.util.ArrayList;
import java.util.List;

import com.mit.dao.react.ReactStatDAO;
import com.mit.dao.user.UserInfoDAO;
import com.mit.entities.react.ObjectType;
import com.mit.entities.react.ReactStat;
import com.mit.entities.react.ReactType;
import com.mit.entities.user.UserInfo;
import com.mit.utils.LinkBuilder;

/**
 * feedId and photoId: generate in the same id hierarchy
 * subfeed: id = photoId
 *
 */

public abstract class Feed {
	public static final int ACTIVE = 1;

	private long id;
	private long userId;
	private int type;
	private String message;
	private int status;
	private long createTime;
	private long updateTime;
	
	public Feed(int type) {
		super();
		this.type = type;
	}
	
	public Feed(long id, long userId, int type, String message, int status, long createTime, long updateTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.type = type;
		this.message = message;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.status = status;
	}

	public abstract UserView buildView(long userViewId);
	
	public static List<UserView> buildListView(List<Feed> feeds, long userViewId) {
		if (feeds == null) {
			return null;
		}
		List<UserView> lstFeedView = new ArrayList<UserView>();
		for (Feed feed : feeds) {
			UserView feedView = feed.buildView(userViewId);
			feedView.getReactStat(feed, userViewId);
			lstFeedView.add(feedView);
		}
		
		return lstFeedView;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	public class UserView {
		private long id;
		private long userId;
		private long feedItemId;
		private String avatar;
		private String displayName;
		private String message;
		private int type;
		private int likes;
		private int comments;
		private int views;
		private int emoteType;
		private boolean isLiked;
		private boolean isViewed;
		private boolean isFollowed;
		private long createTime;
		private long updateTime;
		
		public UserView(Feed feed, long userViewId) {
			this.id = feed.getId();
			this.feedItemId = feed.getId();
			this.userId = feed.getUserId();
			UserInfo ui = UserInfoDAO.getInstance().getById((int)feed.getUserId());
			if (ui != null) {
				this.avatar = LinkBuilder.buildUserAvatarLink(ui.getPhoto());
				this.displayName = ui.getFullName();
			}
			this.message = feed.getMessage();
			this.type = feed.getType();
			this.createTime = feed.getCreateTime();
			this.updateTime = feed.getUpdateTime();
		}
		
		public void getReactStat(Feed feed, long userViewId) {
			List<ReactStat> rss = ReactStatDAO.getInstance().getByObject(ObjectType.FEED.getValue(), feed.getId());
			if (rss != null) {
				for (ReactStat rs : rss) {
					int reactType = rs.getSocialType();
					if (reactType == ReactType.LIKE.getValue()) {
						this.likes = rs.getTotal();
						this.isLiked = rs.getUserIds() != null && rs.getUserIds().contains(userViewId);
						this.emoteType = isLiked ? 1 : 0;
					} else if (reactType == ReactType.COMMENT.getValue()) {
						this.comments = rs.getTotal();
					} else if(reactType == ReactType.VIEW.getValue()) {
						this.views = rs.getTotal();
						this.isViewed = rs.getUserIds() != null && rs.getUserIds().contains(userViewId);
					}
				}
			}
		}

		public int getEmoteType() {
			return emoteType;
		}

		public void setEmoteType(int emoteType) {
			this.emoteType = emoteType;
		}

		public long getId() {
			return id;
		}

		public long getUserId() {
			return userId;
		}

		public long getFeedItemId() {
			return feedItemId;
		}

		public String getAvatar() {
			return avatar;
		}

		public String getDisplayName() {
			return displayName;
		}

		public String getMessage() {
			return message;
		}

		public int getType() {
			return type;
		}


		public int getLikes() {
			return likes;
		}

		public int getComments() {
			return comments;
		}

		public int getViews() {
			return views;
		}

		public boolean getIsLiked() {
			return isLiked;
		}

		public boolean getIsViewed() {
			return isViewed;
		}

		public boolean getIsFollowed() {
			return isFollowed;
		}
		
		public void setLikes(int likes) {
			this.likes = likes;
		}

		public void setComments(int comments) {
			this.comments = comments;
		}

		public void setViews(int views) {
			this.views = views;
		}

		public void setIsLiked(boolean isLiked) {
			this.isLiked = isLiked;
		}

		public void setIsViewed(boolean isViewed) {
			this.isViewed = isViewed;
		}

		public void setIsFollowed(boolean isFollowed) {
			this.isFollowed = isFollowed;
		}
		
		public void setLiked(boolean isLiked) {
			this.isLiked = isLiked;
		}

		public void setViewed(boolean isViewed) {
			this.isViewed = isViewed;
		}

		public void setFollowed(boolean isFollowed) {
			this.isFollowed = isFollowed;
		}

		public long getCreateTime() {
			return createTime;
		}

		public long getUpdateTime() {
			return updateTime;
		}
		
	}

}
