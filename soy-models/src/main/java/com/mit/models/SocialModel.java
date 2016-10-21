package com.mit.models;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mit.dao.link.LinkDAO;
import com.mit.dao.link.LinkParser;
import com.mit.dao.mid.MIdGenLongDAO;
import com.mit.dao.photo.PhotoType;
import com.mit.dao.photo.PhotoUtils;
import com.mit.dao.react.FeedErrCode;
import com.mit.dao.react.ReactStatDAO;
import com.mit.dao.social.FeedDAO;
import com.mit.dao.social.FeedReportDAO;
import com.mit.dao.social.PhotoInfoDAO;
import com.mit.dao.user.UserStatDAO;
import com.mit.entities.link.Link;
import com.mit.entities.react.ObjectType;
import com.mit.entities.react.ReactStat;
import com.mit.entities.react.ReactType;
import com.mit.entities.social.Feed;
import com.mit.entities.social.FeedReport;
import com.mit.entities.social.LinkFeed;
import com.mit.entities.social.MessageFeed;
import com.mit.entities.social.PhotoFeed;
import com.mit.entities.social.PhotoInfo;
import com.mit.entities.social.StickerFeed;
import com.mit.entities.user.UserStat;
import com.mit.luv.kafka.producer.ProducerPush;
import com.mit.luv.kafka.producer.ProducerTopic;
import com.mit.utils.ImageInfoUtils;
import com.mit.utils.JsonUtils;
import com.mit.utils.LinkBuilder;

public class SocialModel {
	public enum ListFeedOption {
		DEFAULT(1), HOT(2), FOLLOW(3), MINE(4);
		
		private int value;
		
		private ListFeedOption(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	public static final SocialModel Instance = new SocialModel();
	public static final int ADD = 1;
	public static final int REMOVE = 2;
	
	public Map<String, Object> postFeed(long userId, String message, long linkId, String sticker, List<Long> photoIds) {
		Map<String, Object> rs = new HashMap<String, Object>();
		int err;
		if ((message == null || message.isEmpty()) && (linkId == 0) && (sticker == null || sticker.isEmpty()) && (photoIds == null || photoIds.isEmpty())) {
			err = FeedErrCode.FEED_CONTENT_EMPTY;
		} else {
			Feed feed = null;
			if (linkId > 0) {
				feed = new LinkFeed(0, userId, message, Feed.ACTIVE, System.currentTimeMillis(), System.currentTimeMillis(), linkId);
			} else if (sticker != null && !sticker.isEmpty()) {
				feed = new StickerFeed(0, userId, message, Feed.ACTIVE, System.currentTimeMillis(), System.currentTimeMillis(), sticker);
			} else if (photoIds != null && !photoIds.isEmpty()) {
				feed = new PhotoFeed(0, userId, message, Feed.ACTIVE, System.currentTimeMillis(), System.currentTimeMillis(), photoIds);
			} else {
				feed = new MessageFeed(0, userId, message, Feed.ACTIVE, System.currentTimeMillis(), System.currentTimeMillis());
			}
			
			err = FeedDAO.getInstance().insert(feed);
			if (err >= 0) {
				rs.put("feed", feed.buildView(userId));
				err = FeedErrCode.SUCCESS;
				ProducerPush.send(ProducerTopic.FEED_ADD, feed.getId() + "\t" + userId);
			} else {
				err = FeedErrCode.SERVER_ERROR;
			}
		}
		rs.put("err", err);

		return rs;
	}
	
	public Map<String, Object> editFeed(long userId, long feedId, String message, long linkId, String sticker, List<Long> photoIds) {
		Map<String, Object> rs = new HashMap<String, Object>();
		int err = FeedErrCode.SUCCESS;
		Feed feed = FeedDAO.getInstance().getById(feedId);
		if (feed != null) {
			if (feed.getUserId() == userId) {
				if ((message == null || message.isEmpty()) && (linkId == 0) && (sticker == null || sticker.isEmpty()) && (photoIds == null || photoIds.isEmpty())) {
					err = FeedErrCode.FEED_CONTENT_EMPTY;
				} else {
					try {
						if (linkId > 0) {
							((LinkFeed)feed).setLinkId(linkId);
						} else if (sticker != null && !sticker.isEmpty()) {
							((StickerFeed)feed).setSticker(sticker);
						} else if (photoIds != null && !photoIds.isEmpty()) {
							((PhotoFeed)feed).setPhotos(photoIds);
						}
						if (message != null) {
							feed.setMessage(message);
						}
					} catch (Exception e) {
						err = FeedErrCode.SERVER_ERROR;
					}
					
					err = FeedDAO.getInstance().update(feed);
					if (err >= 0) {
						rs.put("feed", feed.buildView(userId));
					} else {
						err = FeedErrCode.SERVER_ERROR;
					}
				}
			} else {
				err = FeedErrCode.PERMISSION_LIMIT;
			} 
		} else {
			err = FeedErrCode.FEED_NOT_EXIST;
		}
		rs.put("err", err);

		return rs;
	}
	
	public Map<String, Object> deleteFeed(long feedId, long userId) {
		Map<String, Object> rs = new HashMap<String, Object>();
		int err = FeedErrCode.SUCCESS;
		Feed feed = FeedDAO.getInstance().getById(feedId);
		if (feed != null) {
			if (feed.getUserId() == userId) {
				err = FeedDAO.getInstance().delete(feedId);
				err = (err >= 0) ? FeedErrCode.SUCCESS : FeedErrCode.SERVER_ERROR;
			} else {
				err = FeedErrCode.PERMISSION_LIMIT;
			}
		} else {
			err = FeedErrCode.FEED_NOT_EXIST;
		}
		rs.put("err", err);

		return rs;
	}
	
	public Map<String, Object> getListFeed(long viewUserId, long feedOwnId, int count, int from, int type) {
		Map<String, Object> rs = new HashMap<String, Object>();
		int err = FeedErrCode.SUCCESS;
		List<Feed> feeds = null;
		UserStat ff = UserStatDAO.getInstance().getById(viewUserId);
		List<Long> feedHides = null;
		List<Long> hffuIds = null;
		List<Long> follow = null;
		if (type == ListFeedOption.DEFAULT.getValue()) {
			if (ff != null) {
				feedHides = ff.getFeedHides();
				hffuIds = ff.getHffuIds();
			}
			feeds = FeedDAO.getInstance().getListFeed(follow, feedHides, hffuIds, count + 1, from, "createTime", -1);
		} else if (type == ListFeedOption.FOLLOW.getValue()) {
			follow = new LinkedList<>(Arrays.asList(viewUserId));
			if (ff != null) {
				if (ff.getFollowUserIds() != null) {
					follow.addAll(ff.getFollowUserIds());
				}
				feedHides = ff.getFeedHides();
			}
			feeds = FeedDAO.getInstance().getListFeed(follow, feedHides, hffuIds, count + 1, from, "createTime", -1);
		} else if (type == ListFeedOption.HOT.getValue()) {
			List<Long> recentFeedIds = FeedDAO.getInstance().getListFeedIdRecent();
			List<Long> topFeedIds = new LinkedList<>();
			List<Double> points = new LinkedList<>();
			for (int i = 0; i <= from + count; i++) {
				points.add(-1.0);
			}
			for (long id : recentFeedIds) {
				List<ReactStat> rss = ReactStatDAO.getInstance().getByObject(ObjectType.FEED.getValue(), id);
				double point = 0.0;
				for (ReactStat react : rss) {
					if (react.getSocialType() == ReactType.COMMENT.getValue()) {
						point += react.getTotal() * 0.6;
					} else if (react.getSocialType() == ReactType.LIKE.getValue()) {
						point += react.getTotal() * 0.1;
					} else if (react.getSocialType() == ReactType.SHARE.getValue()) {
						point += react.getTotal() * 0.1;
					} else if (react.getSocialType() == ReactType.VIEW.getValue()) {
						point += react.getTotal() * 0.1;
					}
				}
				for (int i = 0; i <= from + count; i++) {
					if (point > points.get(i)) {
						points.add(i, point);
						topFeedIds.add(i, id);
						break;
					}
				}
			}
			if (topFeedIds.size() > from) {
				int toIdx = topFeedIds.size() >= from + count + 1 ? from + count + 1 : topFeedIds.size();
				topFeedIds = topFeedIds.subList(from, toIdx);
				feeds = new LinkedList<>();
				Map<Long, Feed> feedMap = FeedDAO.getInstance().getMapFromIds(topFeedIds);
				for (long feedId: topFeedIds) {
					if (feedMap.get(feedId) != null) {
						feeds.add(feedMap.get(feedId));
					}
				}
			}
		} else if (type == ListFeedOption.MINE.getValue()) {
			follow = Arrays.asList(feedOwnId);
			feeds = FeedDAO.getInstance().getListFeed(follow, feedHides, hffuIds, count + 1, from, "createTime", -1);
		}
		List<Feed.UserView> feedViews = new LinkedList<>();
		boolean hasMore = false;
		if (feeds != null) {
			if (feeds.size() > count) {
				feeds = feeds.subList(0, count);
				hasMore = true;
			}
			feedViews = Feed.buildListView(feeds, viewUserId);
		}
		rs.put("err", err);
		rs.put("hasMore", hasMore);
		rs.put("feeds", feedViews);

		return rs;
	}
	
	public Map<String, Object> countNewFeed(long feedId, long viewUserId, long feedOwnId, int type) {
		Map<String, Object> rs = new HashMap<>();
		int err = FeedErrCode.SUCCESS;
		int count = 0;
		UserStat ff = UserStatDAO.getInstance().getById(viewUserId);
		if (type == ListFeedOption.DEFAULT.getValue()) {
			List<Long> hffus = ff != null ? ff.getHffuIds() : null;
			count = FeedDAO.getInstance().countNewFeed(feedId, null, null, hffus);
		} else if (type == ListFeedOption.FOLLOW.getValue()) {
			List<Long> followUserIds = ff != null ? ff.getFollowUserIds() : null;
			count = FeedDAO.getInstance().countNewFeed(feedId, followUserIds, null, null);
		} else if (type == ListFeedOption.MINE.getValue()) {
			count = FeedDAO.getInstance().countNewFeed(feedId, Arrays.asList(feedOwnId), null, null);
		}
		rs.put("totalNew", count);
		rs.put("err", err);
		return rs;
	}
	
	public Map<String, Object> getNewFeedList(long feedId, long viewUserId, long feedOwnId, int count, int type) {
		Map<String, Object> rs = new HashMap<String, Object>();
		int err = FeedErrCode.SUCCESS;
		List<Feed> feeds = null;
		UserStat ff = UserStatDAO.getInstance().getById(viewUserId);
		if (type == ListFeedOption.DEFAULT.getValue()) {
			List<Long> hffus = ff != null ? ff.getHffuIds() : null;
			feeds = FeedDAO.getInstance().getListNewFeed(feedId, null, null, hffus, count + 1);
		} else if (type == ListFeedOption.FOLLOW.getValue()) {
			List<Long> followUserIds = ff != null ? ff.getFollowUserIds() : null;
			feeds = FeedDAO.getInstance().getListNewFeed(feedId, followUserIds, null, null, count + 1);
		} else if (type == ListFeedOption.MINE.getValue()) {
			feeds = FeedDAO.getInstance().getListNewFeed(feedId, Arrays.asList(feedOwnId), null, null, count + 1);
		}
		boolean isAppend = true;
		if (feeds != null && feeds.size() > count) {
			isAppend = false;
			feeds = feeds.subList(0, count);
		}
		rs.put("feeds", Feed.buildListView(feeds, viewUserId));
		rs.put("isAppend", isAppend);
		rs.put("err", err);
		return rs;
	}

	public Map<String, Object> getFeed(long feedId, long photoId, long userId) {
		Map<String, Object> rs = new HashMap<String, Object>();
		int err = FeedErrCode.SUCCESS;
		Feed feed = FeedDAO.getInstance().getById(feedId);
		if (feed != null) {
			if (photoId > 0) {
				if (feed instanceof PhotoFeed) {
					PhotoFeed.SubFeed subFeed = ((PhotoFeed)feed).buildSubFeed(photoId, userId);
					subFeed.getReactStat(feed, userId);
					rs.put("subfeed", subFeed);
					rs.put("photo", subFeed.getPhoto());
					rs.put("message", feed.getMessage());
				} else {
					err = FeedErrCode.FEED_NOT_EXIST;
				}
			} else {
				Feed.UserView feedView = feed.buildView(userId);
				feedView.getReactStat(feed, userId);
				rs.put("feed", feedView);
			}
		} else {
			err = FeedErrCode.FEED_NOT_EXIST;
		}
		rs.put("err", err);

		return rs;
	}
	
	public Map<String, Object> hideFeed(long feedId, long userId) {
		Map<String, Object> rs = new HashMap<String, Object>();
		int err = UserStatDAO.getInstance().addToList(userId, UserStat.ListType.HIDDEN_FEED.getValue(), feedId);
		rs.put("err", err);

		return rs;
	}
	
	public Map<String, Object> hffu(long userId, long relatedUserId, boolean isFollow, int type) {
		Map<String, Object> rs = null;
		if (type == ADD) {
			rs = addUserActive(userId, relatedUserId, false);
		} else {
			rs = removeUserActive(userId, relatedUserId, false);
		}
		
		return rs;
	}
	
	public Map<String, Object> follow(long userId, long relatedUserId, boolean isFollow, int type) {
		Map<String, Object> rs = null;
		if (type == ADD) {
			rs = addUserActive(userId, relatedUserId, true);
		} else {
			rs = removeUserActive(userId, relatedUserId, true);
		}
		
		return rs;
	}
	
	private Map<String, Object> addUserActive(long userId, long relatedUserId, boolean isFollow) {
		Map<String, Object> rs = new HashMap<String, Object>();
		int err;
		UserStat ufu = UserStatDAO.getInstance().getById(userId);
		if (ufu != null) {
			ufu.addUserActive(relatedUserId, isFollow);
			err = UserStatDAO.getInstance().update(ufu);
		} else {
			ufu = new UserStat(userId, relatedUserId, isFollow, true);
			err = UserStatDAO.getInstance().insert(ufu);
		}
		err = (err >= 0) ? FeedErrCode.SUCCESS : FeedErrCode.SERVER_ERROR;
		rs.put("err", err);
		if (err >= 0) {
			int total = addUserPassive(relatedUserId, userId, isFollow);
			if (isFollow == true) {
				rs.put("numFollowed", total);
			}
			err = FeedErrCode.SUCCESS;
		} else {
			err = FeedErrCode.SERVER_ERROR;
		}
		rs.put("err", err);
		
		return rs;
	}
	
	private int addUserPassive(long userId, long relatedUserId, boolean isFollowed) {
		UserStat ufu = UserStatDAO.getInstance().getById(userId);
		if (ufu != null) {
			ufu.addUserPassive(relatedUserId, isFollowed);
			UserStatDAO.getInstance().update(ufu);
		} else {
			ufu = new UserStat(userId, relatedUserId, isFollowed, false);
			UserStatDAO.getInstance().insert(ufu);
		}
		int total = isFollowed ? ufu.getUserFollowIds().size() : ufu.getHftuIds().size();
		return total;
	}
	
	private Map<String, Object> removeUserActive(long userId, long relatedUserId, boolean isFollow) {
		Map<String, Object> rs = new HashMap<String, Object>();
		int err;
		UserStat ufu = UserStatDAO.getInstance().getById(userId);
		if (ufu != null) {
			ufu.removeUserActive(relatedUserId, isFollow);
			err = UserStatDAO.getInstance().update(ufu);
		} else {
			err = FeedErrCode.SERVER_ERROR;
		}
		if (err >= 0) {
			int total = removeUserPassive(relatedUserId, userId, isFollow);
			if (isFollow) {
				rs.put("numFollowed", total);
			}
			err = FeedErrCode.SUCCESS;
		}
		rs.put("err", err);

		return rs;
	}
	
	
	private int removeUserPassive(long userId, long relatedUserId, boolean isFollowed) {
		int total = 0;
		UserStat ufu = UserStatDAO.getInstance().getById(userId);
		if (ufu != null) {
			ufu.removeUserPassive(relatedUserId, isFollowed);
			UserStatDAO.getInstance().update(ufu);
			total = isFollowed ? ufu.getUserFollowIds().size() : ufu.getHffuIds().size();
		}
		
		return total;
	}
	public Map<String, Object> parseLink(String link){
		Map<String, Object> rs = new HashMap<String, Object>();
		int err = FeedErrCode.SUCCESS;
		long id = link.hashCode();
		Link feedLink = LinkDAO.getInstance().getById(id);
		if (feedLink == null) {
			LinkParser parser = new LinkParser(link);
			if (parser.getStatus() == LinkParser.FAIL) {
				err = FeedErrCode.PARSE_LINK_ERROR;
			} else {
				String title = parser.parseTitle();
				String desc = parser.parseDescription();
				String site = parser.parseSite();
				String fileName = parser.parseThumbnail();
				byte[] thumbnailData = parser.parseThumbnailData();
				// upload thumbnail
				long thumb;
				try {
					thumb = PhotoUtils.saveDataPhoto(thumbnailData, fileName, PhotoType.COMMENT.getValue());
				} catch (IOException e) {
					thumb = 0L;
					e.printStackTrace();
				}
				feedLink = new Link(id, link, site, title, desc, thumb, 1, System.currentTimeMillis(), System.currentTimeMillis());
				LinkDAO.getInstance().insert(feedLink);
			}
		}
		rs.put("err", err);
		rs.put("feedLink", feedLink.buildLinkView());

		return rs;
	}

	public Map<String, Object> uploadFeedPhoto(byte[] data, String fileName){
		Map<String, Object> rs = new HashMap<String, Object>();
		int err = FeedErrCode.SUCCESS;
		long id;
		try {
			id = PhotoUtils.saveDataPhoto(data, fileName, PhotoType.COMMENT.getValue());
			if (id > 0) {
				ImageInfoUtils iu = new ImageInfoUtils(data);
				int width = iu.getWidth();
				int height = iu.getHeight();
				rs.put("id", id);
				rs.put("photoLink", LinkBuilder.buildFeedPhotoLink(id));
				rs.put("width", width);
				rs.put("height", height);
				PhotoInfo pi = new PhotoInfo(0, id, width, height, PhotoInfo.PhotoInfoType.FEED.getValue());
				PhotoInfoDAO.getInstance().insert(pi);
			} else {
				err = FeedErrCode.SERVER_ERROR;
			}
		} catch (IOException e) {
			err = FeedErrCode.SERVER_ERROR;
		}
		rs.put("err", err);

		return rs;
	}
	
	public Map<String, Object> report(long feedId, long userId) {
		Map<String, Object> rs = new HashMap<String, Object>();
		int err = FeedErrCode.SUCCESS;
		FeedReport freport = FeedReportDAO.getInstance().getByFeedId(feedId);
		if (freport == null) {
			long id = MIdGenLongDAO.getInstance(FeedReportDAO.getInstance().getTableName()).getNext();
			freport = new FeedReport(id, feedId);
			freport.addReport(userId);
			err = FeedReportDAO.getInstance().insert(freport);
		} else {
			if (freport.getUserReportIds() == null || !freport.getUserReportIds().contains(userId)) {
				freport.addReport(userId);
				freport.changeStatus(FeedReport.ReportStatus.NEW.getValue());
				err = FeedReportDAO.getInstance().update(freport);
			}
		}
		err = (err < 0) ? FeedErrCode.SERVER_ERROR : FeedErrCode.SUCCESS;

		rs.put("err", err);
		return rs;
	}
	
	
	public boolean isSubFeed(long feedId, long photoId) {
		boolean rs = false;
		Feed feed = FeedDAO.getInstance().getById(feedId);
		if (feed instanceof PhotoFeed) {
			PhotoFeed pf = (PhotoFeed)feed;
			rs = pf.getPhotos().size() > 1 && pf.getPhotos().contains(photoId);
		}
		
		return rs;
	}
	
	public void broadcastCmt(long feedId, long photoId, long userId, Object data) {
		Map<String, Object> rs = new HashMap<>();
		rs.put("feedId", feedId);
		rs.put("photoId", photoId);
		rs.put("data", data);
		rs.put("userId", userId);
		ProducerPush.send(ProducerTopic.FEED_COMMENT, JsonUtils.Instance.toJson(rs));
	}

	public static void main(String[] args){
	}
}
