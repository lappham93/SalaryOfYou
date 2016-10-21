package com.mit.entities.social;

public class StickerFeed extends Feed {
	private static int TYPE = FeedContentType.STICKER.getValue();
	
	private String sticker;

	public StickerFeed() {
		super(TYPE);
	}

	public StickerFeed(long id, long userId, String message, int status, long createTime, long updateTime,
			String sticker) {
		super(id, userId, TYPE, message, status, createTime, updateTime);
		this.sticker = sticker;
	}
	
	public String getSticker() {
		return sticker;
	}

	public void setSticker(String sticker) {
		this.sticker = sticker;
	}
	
	public class StickerFeedView extends Feed.UserView {
		private String sticker;

		public StickerFeedView(Feed feed, long userViewId) {
			super(feed, userViewId);
			StickerFeed sf = (StickerFeed) feed;
			this.sticker = sf.getSticker();
		}

		public String getSticker() {
			return sticker;
		}

		public void setSticker(String sticker) {
			this.sticker = sticker;
		}
		
	}

	@Override
	public UserView buildView(long userViewId) {
		return new UserView(this, userViewId);
	}
	
	public class UserView extends Feed.UserView {
		private String feedSticker;
		
		public UserView(StickerFeed feed, long userViewId) {
			super(feed, userViewId);
			this.feedSticker = feed.getSticker();
		}

		public String getFeedSticker() {
			return feedSticker;
		}
		
	}
	
}
