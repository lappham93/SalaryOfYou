package com.mit.entities.link;

import com.mit.utils.LinkBuilder;

public class Link {
	
	private long id;
	private String link;
	private String site;
	private String title;
	private String desc;
	private long thumbnail;
	private int status;
	private long createTime;
	private long updateTime;
	
	public Link() {
		super();
	}

	public Link(long id, String link, String site, String title, String desc, long thumbnail, int status,
			long createTime, long updateTime) {
		super();
		this.id = id;
		this.link = link;
		this.site = site;
		this.title = title;
		this.desc = desc;
		this.thumbnail = thumbnail;
		this.status = status;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(long thumbnail) {
		this.thumbnail = thumbnail;
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
	
	public LinkView buildLinkView() {
		return new LinkView(this);
	}

	public class LinkView {
		private long id;
		private String link = "";
		private String site = "";
		private String title = "";
		private String desc = "";
		private String thumbnail = "";
		
		public LinkView(Link link) {
			this.id = link.getId();
			this.link = link.getLink();
			this.site = link.getSite();
			this.title = link.getTitle();
			this.desc = link.getDesc();
			this.thumbnail = LinkBuilder.buildFeedPhotoLink(link.getThumbnail());
		}

		public long getId() {
			return id;
		}

		public String getLink() {
			return link;
		}

		public String getSite() {
			return site;
		}

		public String getTitle() {
			return title;
		}

		public String getDesc() {
			return desc;
		}

		public String getThumbnail() {
			return thumbnail;
		}
		
	}
	
}
