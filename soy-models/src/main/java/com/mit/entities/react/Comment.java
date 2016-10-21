package com.mit.entities.react;

import java.util.ArrayList;
import java.util.List;

import com.mit.dao.react.CommentDAO;
import com.mit.dao.react.RatingDAO;
import com.mit.dao.react.ReactStatDAO;
import com.mit.dao.user.UserStatDAO;
import com.mit.dao.user.UserInfoDAO;
import com.mit.entities.social.PhotoFeed;
import com.mit.entities.user.UserInfo;
import com.mit.entities.user.UserStat;
import com.mit.midutil.MIdNoise;
import com.mit.utils.LinkBuilder;

public class Comment extends React{
	private long id;
	private long userId;
	private String text;
	private String sticker;
	private long photoCmtId;
	private int totalChild;
	private long createTime;
	private long updateTime;
	private int status;
	
	public Comment(int objectType, long objectId) {
		super(objectType, objectId);
	}

	public Comment(int objectType, long objectId, long id, long userId, String text, String sticker,
			long photoCmtId) {
		super(objectType, objectId);
		this.id = id;
		this.userId = userId;
		this.text = text;
		this.sticker = sticker;
		this.photoCmtId = photoCmtId;
		this.status = 1;
	}

	public Comment(int objectType, long objectId, long id, long userId, String text, String sticker, long photoCmtId,
			int totalChild, int status, long createTime, long updateTime) {
		super(objectType, objectId);
		this.id = id;
		this.userId = userId;
		this.text = text;
		this.sticker = sticker;
		this.photoCmtId = photoCmtId;
		this.totalChild = totalChild;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.status = status;
	}
	
	public Comment(int objectType, long objectId, long id, long userId, String text, String sticker, String photoCmtIdNoise) {
		super(objectType, objectId);
		this.id = id;
		this.userId = userId;
		this.text = text;
		this.sticker = sticker;
		this.photoCmtId = photoCmtIdNoise != null && !photoCmtIdNoise.isEmpty() ? MIdNoise.deNoiseLId(photoCmtIdNoise) : 0;
		this.status = 1;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSticker() {
		return sticker;
	}

	public void setSticker(String sticker) {
		this.sticker = sticker;
	}

	public long getPhotoCmtId() {
		return photoCmtId;
	}

	public void setPhotoCmtId(long photoCmtId) {
		this.photoCmtId = photoCmtId;
	}
	
	public int getTotalChild() {
		return totalChild;
	}

	public void setTotalChild(int totalChild) {
		this.totalChild = totalChild;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
	
	public CommentView buildCommentView(long viewUserId) {
		return new CommentView(this, viewUserId);
	}
	
	public static List<CommentView> buildListCommentView(List<Comment> cmts, long viewUserId) {
		List<CommentView> proCmts = new ArrayList<>();
		if (cmts != null && !cmts.isEmpty()) {
			for (Comment cmt : cmts) {
				proCmts.add(cmt.buildCommentView(viewUserId));
			}
		}
		return proCmts;
	}
	
	public class CommentView {
		private long id;
		private long userId;
		private String avatar;
		private String displayName;
		private int type = CommentType.MESSAGE.getValue();
		private String sticker;
		private String content;
		private PhotoFeed.PhotoView photoCmt;
		private boolean isFollowed;
		private long createTime;
		private long updateTime;
		
		public CommentView(Comment cmt, long viewUserId) {
			id = cmt.getId();
			userId = cmt.getUserId();
			UserInfo ui = UserInfoDAO.getInstance().getById((int)userId);
			if (ui != null) {
				this.avatar = LinkBuilder.buildUserPhotoLink(ui.getPhoto());
				this.displayName = ui.getFirstName() + " " + ui.getLastName();
			}
			sticker = cmt.getSticker();
			content = cmt.getText();
			photoCmt = new PhotoFeed().new PhotoView(cmt.getPhotoCmtId());
			if (sticker != null && !sticker.isEmpty()) {
				type = CommentType.STICKER.getValue();
			} else if (cmt.getPhotoCmtId() > 0) {
				type = CommentType.PHOTO.getValue();
			}
			UserStat ff = UserStatDAO.getInstance().getById(viewUserId);
			if (ff != null) {
				isFollowed = ff.getFollowUserIds() != null && ff.getFollowUserIds().contains(cmt.getUserId());
			}
			createTime = cmt.getCreateTime();
			updateTime = cmt.getUpdateTime();
		}

		public long getId() {
			return id;
		}

		public long getUserId() {
			return userId;
		}

		public String getAvatar() {
			return avatar;
		}

		public String getDisplayName() {
			return displayName;
		}

		public int getType() {
			return type;
		}

		public String getSticker() {
			return sticker;
		}

		public String getContent() {
			return content;
		}

		public PhotoFeed.PhotoView getPhotoCmt() {
			return photoCmt;
		}
		
		public boolean getIsFollowed() {
			return isFollowed;
		}

		public long getCreateTime() {
			return createTime;
		}

		public long getUpdateTime() {
			return updateTime;
		}
		
	}
	
	public ProductCommentView buildProductCommentView(long viewUserId) {
		return new ProductCommentView(this, viewUserId);
	}
	
	public static List<ProductCommentView> buildListProductCommentView(List<Comment> cmts, long viewUserId) {
		List<ProductCommentView> proCmts = new ArrayList<>();
		if (cmts != null && !cmts.isEmpty()) {
			for (Comment cmt : cmts) {
				proCmts.add(cmt.buildProductCommentView(viewUserId));
			}
		}
		return proCmts;
	}
	
	public class ProductCommentView extends CommentView{
		private boolean isRated = false;
		private double ratingPoint = 0;

		public ProductCommentView(Comment cmt, long viewUserId) {
			super(cmt, viewUserId);
			Rating rating = RatingDAO.getInstance().getByObjectAndUser(cmt.getObjectType(), cmt.getObjectId(), cmt.getUserId());
			if (rating != null) {
				isRated = true;
				ratingPoint = rating.getPoint();
			}
		}
		
		public boolean isRated() {
			return isRated;
		}

		public double getRatingPoint() {
			return ratingPoint;
		}
	}
	
	public static List<FeedCommentView> buildListFeedCommentView(List<Comment> cmts, long viewUserId) {
		List<FeedCommentView> proCmts = new ArrayList<>();
		if (cmts != null && !cmts.isEmpty()) {
			for (Comment cmt : cmts) {
				proCmts.add(cmt.buildFeedCommentView(viewUserId));
			}
		}
		return proCmts;
	}
	
	public FeedCommentView buildFeedCommentView(long viewUserId) {
		return new FeedCommentView(this, viewUserId);
	}
	
	public class FeedCommentView extends CommentView{
		private int likes;
		private boolean isLiked;
		private int childCount;
		private FeedCommentView lstChild;
		
		public FeedCommentView(Comment cmt, long viewUserId) {
			super(cmt, viewUserId);
			this.childCount = cmt.getTotalChild();
			Comment lCmt = CommentDAO.getInstance().getLast(ObjectType.COMMENT.getValue(), cmt.getId());
			this.lstChild = lCmt != null ? lCmt.buildFeedCommentView(viewUserId) : null;
			ReactStat rs  = ReactStatDAO.getInstance().getByObjectAndSocialType(ObjectType.COMMENT.getValue(), cmt.getId(), ReactType.LIKE.getValue());
			if (rs != null) {
				this.likes = rs.getTotal();
				this.isLiked = rs.getUserIds() != null && rs.getUserIds().contains(viewUserId);
			}
		}

		public int getLikes() {
			return likes;
		}

		public void setLikes(int likes) {
			this.likes = likes;
		}

		public boolean getIsLiked() {
			return isLiked;
		}

		public void setLiked(boolean isLiked) {
			this.isLiked = isLiked;
		}

		public int getChildCount() {
			return childCount;
		}

		public void setChildCount(int childCount) {
			this.childCount = childCount;
		}

		public FeedCommentView getLstChild() {
			return lstChild;
		}

		public void setLstChild(FeedCommentView lstChild) {
			this.lstChild = lstChild;
		}

	}
}
