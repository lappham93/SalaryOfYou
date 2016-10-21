package com.mit.entities.social;

public class MessageFeed extends Feed{
	public static final int TYPE = FeedContentType.MESSAGE.getValue();

	public MessageFeed() {
		super(TYPE);
	}

	public MessageFeed(long id, long userId, String message, int status, long createTime, long updateTime) {
		super(id, userId, TYPE, message, status, createTime, updateTime);
	}

	@Override
	public UserView buildView(long userViewId) {
		return new UserView(this, userViewId);
	}
	
	public class UserView extends Feed.UserView {

		public UserView(MessageFeed feed, long userViewId) {
			super(feed, userViewId);
		}
		
	}
	
}
