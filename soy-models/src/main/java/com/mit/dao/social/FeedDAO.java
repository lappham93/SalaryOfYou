package com.mit.dao.social;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.dao.mid.MIdGenLongDAO;
import com.mit.dao.photo.PhotoCommon;
import com.mit.entities.social.Feed;
import com.mit.entities.social.FeedContentType;
import com.mit.entities.social.FeedType;
import com.mit.entities.social.LinkFeed;
import com.mit.entities.social.MessageFeed;
import com.mit.entities.social.PhotoFeed;
import com.mit.entities.social.StickerFeed;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class FeedDAO extends CommonDAO {
	private Logger _logger = LoggerFactory.getLogger(FeedDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static FeedDAO _instance;

	private String TABLE_NAME = PhotoCommon.commentPhotoIdGen;
	private int FEED_TYPE = FeedType.SOCIAL.getValue();
	
	public static FeedDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new FeedDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}
	
	public void setLogger(Logger logger) {
		this._logger = logger;
	}
	
	public FeedDAO() {
	}

	public void setFeedType(int feedType) {
		this.FEED_TYPE = feedType;
	}
	
	public long getTotalFromRange(long from, long end) {
		long rs = 0;
		if (dbSource != null) {
			try {
				Document filter = new Document("createTime", new Document("$gte", new Date(from)))
						.append("createTime", new Document("$lte", new Date(end)))
						.append("status", new Document("$gt", 0));
				rs = dbSource.getCollection(TABLE_NAME).count(filter);
			} catch (final Exception e) {
				_logger.error("getTotalFromRange ", e);
			}
		}

		return rs;
	}
	
	public long getTotal() {
		long rs = 0;
		if (dbSource != null) {
			try {
				Document filter = new Document("status", new Document("$gt", 0));
				rs = dbSource.getCollection(TABLE_NAME).count(filter);
			} catch (final Exception e) {
				_logger.error("getTotal ", e);
			}
		}

		return rs;
	}
	
	public List<Feed> getAll() {
		List<Feed> feeds = null;
		if (dbSource != null) {
			try {
				Document filter = new Document("feedType", FEED_TYPE);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(filter);
				if (doc != null) {
					feeds = new MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("getAll ", e);
			}
		}

		return feeds;
	}

	public long countFeedOfUser(long userId) {
		long rs = 0;
		if (dbSource != null) {
			try {
				Document filter = new Document("userId", userId)
						.append("status", new Document("$gt", 0));
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(filter);
				if (doc != null) {
					List<Feed> feeds = new MongoMapper().parseList(doc);
					if (feeds != null) {
						rs = feeds.size();
					}
				}
			} catch (final Exception e) {
				_logger.error("countFeedOfUser ", e);
			}
		}

		return rs;
	}

	public List<Feed> getListFeed(List<Long> userIds, List<Long> feedHides, List<Long> hffus, int count, int from, String fieldSort, int sortOption) {
		List<Feed> feeds = null;
		if (dbSource != null) {
			try {
				Document filter = new Document("status", new Document("$gt", 0));
				if (userIds != null && !userIds.isEmpty()) {
					filter.append("userId", new Document("$in", userIds));
				}
				if (feedHides != null && !feedHides.isEmpty()) {
					filter.append("_id", new Document("$nin", feedHides));
				}
				if (hffus != null && !hffus.isEmpty()) {
					filter.append("userId", new Document("$nin", hffus));
				}
				Document sort = new Document(fieldSort, sortOption);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort).skip(from)
						.limit(count);
				if (doc != null) {
					feeds = new MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("getListFeed ", e);
			}
		}

		return feeds;
	}
	
	public int countNewFeed(long curFeedId, List<Long> userIds, List<Long> feedHides, List<Long> hffus) {
		int count = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document("_id", new Document("$gt", curFeedId))
						.append("status", new Document("$gt", 0));
				if (userIds != null && !userIds.isEmpty()) {
					filter.append("userId", new Document("$in", userIds));
				}
				if (feedHides != null && !feedHides.isEmpty()) {
					filter.append("_id", new Document("$nin", feedHides));
				}
				if (hffus != null && !hffus.isEmpty()) {
					filter.append("userId", new Document("$nin", hffus));
				}
				count = (int) dbSource.getCollection(TABLE_NAME).count(filter);
			} catch (final Exception e) {
				_logger.error("countNewFeed ", e);
			}
		}

		return count;
	}
	
	public List<Feed> getListNewFeed(long curFeedId, List<Long> userIds, List<Long> feedHides, List<Long> hffus, int count) {
		List<Feed> rs = null;
		if (dbSource != null) {
			try {
				Document filter = new Document("_id", new Document("$gt", curFeedId))
						.append("status", new Document("$gt", 0));
				if (userIds != null && !userIds.isEmpty()) {
					filter.append("userId", new Document("$in", userIds));
				}
				if (feedHides != null && !feedHides.isEmpty()) {
					filter.append("_id", new Document("$nin", feedHides));
				}
				if (hffus != null && !hffus.isEmpty()) {
					filter.append("userId", new Document("$nin", hffus));
				}
				Document sort = new Document("createTime", -1);
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort).limit(count);
				if (docs != null) {
					rs = new MongoMapper().parseList(docs);
				}
			} catch (final Exception e) {
				_logger.error("getListNewFeed ", e);
			}
		}

		return rs;
	}
	
	public List<Long> getListFeedIdRecent() {
		List<Long> feeds = null;
		if (dbSource != null) {
			try {
				Calendar time = Calendar.getInstance();
				time.set(Calendar.DATE, time.get(Calendar.DATE) - 7);
				Document objFinder = new Document("createTime", new Document("$gte", time.getTime()))
						.append("status", new Document("$gt", 0));
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if (doc != null) {
					feeds = new LinkedList<>();
					MongoCursor<Document> tmps = doc.iterator();
					while (tmps.hasNext()) {
						Document tmp = tmps.next();
						feeds.add(tmp.getLong("_id"));
					} 
				}
			} catch (final Exception e) {
				_logger.error("getListFeedIdRecent ", e);
			}
		}

		return feeds;
	}

	public List<Feed> getListfromIds(List<Long> ids) {
		List<Feed> feeds = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", new Document("$in", ids));
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if (doc != null) {
					feeds = new MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("getListfromIds ", e);
			}
		}

		return feeds;
	}
	
	public Map<Long, Feed> getMapFromIds(List<Long> ids) {
		Map<Long, Feed> feeds = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", new Document("$in", ids));
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if (doc != null) {
					feeds = new HashMap<>();
					MongoMapper mapper = new MongoMapper();
					MongoCursor<Document> tmps = doc.iterator();
					while(tmps.hasNext()) {
						Feed feed = mapper.parseObject(tmps.next());
						feeds.put(feed.getId(), feed);
					}
				}
			} catch (final Exception e) {
				_logger.error("getMapFromIds ", e);
			}
		}

		return feeds;
	}

	public Feed getById(long id) {
		Feed feed = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					feed = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return feed;
	}
	
	public Feed getByPhotoId(long photoId) {
		Feed feed = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("type", FeedContentType.PHOTO.getValue())
						.append("photos", photoId);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					feed = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getByPhotoId ", e);
			}
		}

		return feed;
	}
	
	public Feed getFeedIgnoreStatus(long id) {
		Feed feed = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					feed = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getFeedIgnoreStatus ", e);
			}
		}

		return feed;
	}
	
	public List<Feed> getArroundHiring(double minDis, double maxDis, double lon, double lat, int count, int from) {
		List<Feed> feeds = null;
		if (dbSource != null) {
			try {
//				dbSource.getCollection(TABLE_NAME).createIndex(new Document("location", "2dsphere"));
				Document filter = new Document("feedType", FeedType.HIRING.getValue())
						.append("location", new Document("$near", new Document("$geometry", new Document("type", "Point")
								.append("coordinates", Arrays.asList(lon, lat))).append("$minDistance", minDis).append("$maxDistance", maxDis)));
				Document sort = new Document("createTime", -1);
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(filter)
						.sort(sort).skip(from).limit(count);
				if (docs != null) {
					feeds = new MongoMapper().parseList(docs);
				}
			} catch (final Exception e) {
				_logger.error("getArroundHiring ", e);
			}
		} 
		
		return feeds;
	}
	
	public int insert(Feed msg) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				if (msg.getId() <= 0) {
					msg.setId(MIdGenLongDAO.getInstance(TABLE_NAME).getNext());
				}
				Document obj = mapper.toDocument(msg);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch (final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}

	public int update(Feed msg) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", msg.getId());
				Document obj = new Document("$set", mapper.toDocument(msg));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch (final Exception e) {
				_logger.error("update ", e);
			}
		}

		return rs;
	}
	
	public int getView(long id) {
		int rs = 0;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(filter).first();
				if (doc != null) {
					rs = doc.getInteger("views", 0);
				}
			} catch (final Exception e) {
				_logger.error("getView ", e);
			}
		}

		return rs;
	}

	public int delete(long id) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", id);
				Document obj = new Document("$set", new Document("status", 0));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch (final Exception e) {
				_logger.error("delete ", e);
			}
		}

		return rs;
	}

	public int hardDelete(long id) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document("_id", id);
				DeleteResult qRs = dbSource.getCollection(TABLE_NAME).deleteOne(filter);
				rs = (int) qRs.getDeletedCount();
			} catch (final Exception e) {
				_logger.error("hardDelete ", e);
			}
		}

		return rs;
	}

	public int addField(String field, Object defaultValue) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document("feedType", FEED_TYPE);
				Document obj = new Document("$set", new Document(field, defaultValue));
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateMany(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch (final Exception e) {
				_logger.error("addField ", e);
			}
		}

		return rs;
	}
	
	public static void main(String[] args) {
		FeedDAO.getInstance().addField("views", 0);
	}

	private class MongoMapper extends MongoDBParse<Feed> {

		@Override
		public Feed parseObject(Document doc) {
			Feed feed = null;
			long id = doc.getLong("_id");
			long userId = doc.getLong("userId");
			String message = doc.getString("message");
			int status = doc.getInteger("status");
			long createTime = doc.getDate("createTime").getTime();
			long updateTime = doc.getDate("updateTime").getTime();
			int type = doc.getInteger("type");
			if (type == FeedContentType.MESSAGE.getValue()) {
				feed = new MessageFeed(id, userId, message, status, createTime, updateTime);
			} else if (type == FeedContentType.LINK.getValue()) {
				feed = new LinkFeed(id, userId, message, status, createTime, updateTime, 
						doc.getLong("linkId"));
			} else if (type == FeedContentType.PHOTO.getValue()) {
				feed = new PhotoFeed(id, userId, message, status, createTime, updateTime, (List<Long>) doc.get("photos"));
			} else if (type == FeedContentType.STICKER.getValue()) {
				feed = new StickerFeed(id, userId, message, status, createTime, updateTime, doc.getString("sticker"));
			}
			
			return feed;
		}

		@Override
		public Document toDocument(Feed obj) {
			Document doc = new Document("_id", obj.getId())
					.append("userId", obj.getUserId())
					.append("message", obj.getMessage())
					.append("type", obj.getType())
					.append("status", obj.getStatus());
			if (obj instanceof MessageFeed) {
				
			} else if (obj instanceof PhotoFeed){
				PhotoFeed pf = (PhotoFeed) obj;
				doc.append("photos", pf.getPhotos());
			} else if (obj instanceof StickerFeed) {
				StickerFeed sf = (StickerFeed) obj;
				doc.append("sticker", sf.getSticker());
			} else if (obj instanceof LinkFeed) {
				LinkFeed lf = (LinkFeed) obj;
				doc.append("linkId", lf.getLinkId());
			}
			return doc;
		}

	}
}
