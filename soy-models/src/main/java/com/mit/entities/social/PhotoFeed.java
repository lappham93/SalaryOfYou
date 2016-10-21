package com.mit.entities.social;

import java.util.LinkedList;
import java.util.List;

import com.mit.dao.react.ReactStatDAO;
import com.mit.dao.social.PhotoInfoDAO;
import com.mit.entities.react.ObjectType;
import com.mit.entities.react.ReactStat;
import com.mit.entities.react.ReactType;
import com.mit.utils.LinkBuilder;

public class PhotoFeed extends Feed{
	private static int TYPE = FeedContentType.PHOTO.getValue();
	
	private List<Long> photos;

	public PhotoFeed() {
		super(TYPE);
	}

	public PhotoFeed(long id, long userId, String message, int status, long createTime, long updateTime,
			List<Long> photos) {
		super(id, userId, TYPE, message, status, createTime, updateTime);
		this.photos = photos;
	}

	public List<Long> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Long> photos) {
		this.photos = photos;
	}
	
	public class PhotoView {
		private long id;
		private long feedItemId;
		private String photoLink;
		private int width;
		private int height;
		
		public PhotoView(long photoId) {
			this.id = photoId;
			this.feedItemId = photoId;
			this.photoLink = LinkBuilder.buildFeedPhotoLink(photoId);
			PhotoInfo pi = PhotoInfoDAO.getInstance().getByPhotoIdAndType(photoId, PhotoInfo.PhotoInfoType.FEED.getValue());
			if (pi != null) {
				this.width = pi.getWidth();
				this.height = pi.getHeight();
			}
		}

		public long getId() {
			return id;
		}
		
		public long getFeedItemId() {
			return feedItemId;
		}

		public String getPhotoLink() {
			return photoLink;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}
		
	}

	@Override
	public UserView buildView(long userViewId) {
		return new UserView(this, userViewId);
	}
	
	public class UserView extends Feed.UserView {
		private List<PhotoFeed.PhotoView> photos;
		
		public UserView(PhotoFeed feed, long userViewId) {
			super(feed, userViewId);
			List<Long> photoIds = feed.getPhotos();
			photos = new LinkedList<>();
			if (photoIds != null) {
				for (long photoId : photoIds) {
					photos.add(new PhotoView(photoId));
				}
			}
		}

		public List<PhotoFeed.PhotoView> getPhotos() {
			return photos;
		}
		
	}
	
	public SubFeed buildSubFeed(long photoId, long userViewId) {
		return new SubFeed(this, photoId, userViewId);
	}
	
	public class SubFeed extends Feed.UserView {
		private PhotoFeed.PhotoView photo;
		
		public SubFeed(PhotoFeed feed, long photoId, long userViewId) {
			super(feed, userViewId);
			photo = new PhotoView(photoId);
		}
		
		@Override
		public void getReactStat(Feed feed, long userViewId) {
			if (((PhotoFeed)feed).getPhotos().size() > 1) {
				List<ReactStat> rss = ReactStatDAO.getInstance().getByObject(ObjectType.SUBFEED.getValue(), photo.getId());
				if (rss != null) {
					for (ReactStat rs : rss) {
						int reactType = rs.getSocialType();
						if (reactType == ReactType.LIKE.getValue()) {
							this.setLikes(rs.getTotal());
							this.setLiked(rs.getUserIds() != null && rs.getUserIds().contains(userViewId));
							this.setEmoteType(this.getIsLiked() ? 1 : 0);
						} else if (reactType == ReactType.COMMENT.getValue()) {
							this.setComments(rs.getTotal());
						} else if(reactType == ReactType.VIEW.getValue()) {
							this.setViews(rs.getTotal());
							this.setViewed(rs.getUserIds() != null && rs.getUserIds().contains(userViewId));
						}
					}
				}
			} else {
				super.getReactStat(feed, userViewId);
			}
		}

		public PhotoFeed.PhotoView getPhoto() {
			return photo;
		}
		
	}
	
	
}
