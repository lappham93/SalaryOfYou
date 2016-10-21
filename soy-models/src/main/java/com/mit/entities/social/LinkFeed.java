package com.mit.entities.social;

import com.mit.dao.link.LinkDAO;
import com.mit.entities.link.Link;

public class LinkFeed extends Feed{
	private static int TYPE = FeedContentType.LINK.getValue();
	
	private long linkId;
	
	public LinkFeed() {
		super(TYPE);
	}

	public LinkFeed(long id, long userId, String message, int status, long createTime, long updateTime,
			long linkId) {
		super(id, userId, TYPE, message, status, createTime, updateTime);
		this.linkId = linkId;
	}
	
	public long getLinkId() {
		return linkId;
	}

	public void setLinkId(long linkId) {
		this.linkId = linkId;
	}

	@Override
	public UserView buildView(long userViewId) {
		return new UserView(this, userViewId);
	}
	
	public class UserView extends Feed.UserView {
		private Link.LinkView link;
		
		public UserView(LinkFeed feed, long userViewId) {
			super(feed, userViewId);
			Link l = LinkDAO.getInstance().getById(feed.getLinkId());
			if (l != null) {
				link = l.buildLinkView();
			}
		}

		public Link.LinkView getLink() {
			return link;
		}
		
	}
	
}
