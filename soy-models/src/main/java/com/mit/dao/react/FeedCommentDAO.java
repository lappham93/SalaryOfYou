package com.mit.dao.react;
//package com.mit.dao.social;
//
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
//import org.bson.Document;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.mit.constants.MongoErrorCode;
//import com.mit.dao.CommonDAO;
//import com.mit.dao.MongoDBParse;
//import com.mit.entities.social.FeedComment;
//import com.mongodb.client.FindIterable;
//import com.mongodb.client.result.DeleteResult;
//import com.mongodb.client.result.UpdateResult;
//
//public class FeedCommentDAO extends CommonDAO {
//
//	private final Logger _logger = LoggerFactory.getLogger(FeedCommentDAO.class);
//	private static Lock _lock = new ReentrantLock();
//	private static FeedCommentDAO _instance;
//
//	private String TABLE_NAME = "feed_comment";
//
//	public static FeedCommentDAO getInstance() {
//		if (_instance == null) {
//			_lock.lock();
//			try {
//				if (_instance == null) {
//					_instance = new FeedCommentDAO();
//				}
//			} finally {
//				_lock.unlock();
//			}
//		}
//
//		return _instance;
//	}
//
//	private FeedCommentDAO() {
//	}
//	
//	public String getTableName() {
//		return this.TABLE_NAME;
//	}
//	
//	public long getTotalFromRange(long from, long end) {
//		long rs = 0;
//		if (dbSource != null) {
//			try {
//				Document objFinder = new Document("createTime", new Document("$gte", new Date(from)))
//						.append("createTime", new Document("$lte", new Date(end)))
//						.append("status", new Document("$gt", 0));
//				rs = dbSource.getCollection(TABLE_NAME).count(objFinder);
//			} catch (final Exception e) {
//				_logger.error("getTotalFromRange ", e);
//			}
//		}
//
//		return rs;
//	}
//	
//	public long getTotal() {
//		long rs = 0;
//		if (dbSource != null) {
//			try {
//				Document objFinder = new Document("status", new Document("$gt", 0));
//				rs = dbSource.getCollection(TABLE_NAME).count(objFinder);
//			} catch (final Exception e) {
//				_logger.error("getTotal ", e);
//			}
//		}
//
//		return rs;
//	}
//
//
//	public List<FeedComment> getList(long feedId) {
//		List<FeedComment> comments = null;
//		if (dbSource != null) {
//			try {
//				Document objFinder = new Document("feedId", feedId);
//				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
//				if (doc != null) {
//					comments = new MongoMapper().parseList(doc);
//				}
//			} catch (final Exception e) {
//				_logger.error("getList ", e);
//			}
//		}
//
//		return comments;
//	}
//
//	public List<FeedComment> getListSubWithConstraint(long feedId, long commentParentId, long feedItemId, int from, int count, String fieldSort, int sortOption) {
//		List<FeedComment> comments = null;
//		if (dbSource != null) {
//			try {
//				Document filter = new Document("feedId", feedId)
//						.append("feedItemId", feedItemId)
//						.append("commentParentId", commentParentId)
//						.append("status", new Document("$gt", 0));
//				Document sort = new Document(fieldSort, sortOption);
//				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort).skip(from).limit(count);
//				if (doc != null) {
//					comments = new MongoMapper().parseList(doc);
//				} 
//			} catch (final Exception e) {
//					_logger.error("getListWithConstraint ", e);
//			}
//		}
//		
//		return comments;
//	}
//	
//	public List<FeedComment> getListWithConstraint(long feedId, long commentParentId, int from, int count, String fieldSort, int sortOption) {
//		List<FeedComment> comments = null;
//		if (dbSource != null) {
//			try {
//				Document filter = new Document("feedId", feedId)
//						.append("feedItemId", -1)
//						.append("commentParentId", commentParentId)
//						.append("status", new Document("$gt", 0));
//				Document sort = new Document(fieldSort, sortOption);
//				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort).skip(from).limit(count);
//				if (doc != null) {
//					comments = new MongoMapper().parseList(doc);
//				} 
//			} catch (final Exception e) {
//					_logger.error("getListWithConstraint ", e);
//			}
//		}
//		
//		return comments;
//	}
//	
//	public List<FeedComment> getAllChildWithConstraint(long feedId, long commentParentId, String fieldSort, int sortOption) {
//		List<FeedComment> comments = null;
//		if (dbSource != null) {
//			try {
//				Document filter = new Document("feedId", feedId)
//						.append("commentParentId", commentParentId)
//						.append("status", new Document("$gt", 0));
//				Document sort = new Document(fieldSort, sortOption);
//				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort);
//				if (doc != null) {
//					comments = new MongoMapper().parseList(doc);
//				}
//			} catch (Exception e) {
//				_logger.error("getAllChildWithConstraint ", e);
//			}
//		}
//		
//		return comments;
//	}
//	
//	public List<FeedComment> getChildWithConstraint(long commentParentId, int count, int from, String fieldSort, int sortOption) {
//		List<FeedComment> comments = null;
//		if (dbSource != null) {
//			try {
//				Document filter = new Document("commentParentId", commentParentId)
//						.append("status", new Document("$gt", 0));
//				Document sort = new Document(fieldSort, sortOption);
//				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort).skip(from).limit(count);
//				if (doc != null) {
//					comments = new MongoMapper().parseList(doc);
//				}
//			} catch (Exception e) {
//				_logger.error("getAllChildWithConstraint");
//			}
//		}
//		
//		return comments;
//	}
//	
//	public List<FeedComment> getNewComment(long feedId, long feedItemId, long commentId, int count) {
//		List<FeedComment> comments = null;
//		if (dbSource != null) {
//			try {
//				Document filter = new Document("feedId", feedId)
//						.append("feedItemId", feedItemId)
//						.append("_id", new Document("$gt", commentId))
//						.append("status", new Document("$gt", 0));
//				Document sort = new Document("createTime", -1);
//				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort).limit(count);
//				if (docs != null) {
//					comments = new MongoMapper().parseList(docs);
//				}
//			} catch (Exception e) {
//				_logger.debug("getNewComment ", e);
//			}
//		}
//		
//		return comments;
//	}
//	
//	public FeedComment getById(long id) {
//		FeedComment comment = null;
//		if (dbSource != null) {
//			try {
//				Document objFinder = new Document("_id", id).append("status", new Document("$gt", 0));
//				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
//				if (doc != null) {
//					comment = new MongoMapper().parseObject(doc);
//				}
//			} catch (final Exception e) {
//				_logger.error("getById ", e);
//			}
//		}
//
//		return comment;
//	}
//	
//	public boolean commentExist(long feedId, long commentId) {
//		boolean rs = false;
//		if (dbSource != null) {
//			try {
//				Document filter = new Document("_id", commentId)
//						.append("feedId", feedId);
//				Document doc = dbSource.getCollection(TABLE_NAME).find(filter).first();
//				rs = doc != null;
//			} catch (final Exception e) {
//				_logger.error("commentExist", e);
//			}
//		}
//		
//		return rs;
//	}
//
//	public int insert(FeedComment msg) {
//		int rs = MongoErrorCode.NOT_CONNECT;
//		if (dbSource != null) {
//			try {
//				MongoMapper mapper = new MongoMapper();
//				Document obj = mapper.toDocument(msg);
//				obj = mapper.buildInsertTime(obj);
//				dbSource.getCollection(TABLE_NAME).insertOne(obj);
//				rs = MongoErrorCode.SUCCESS;
//			} catch (final Exception e) {
//				_logger.error("insert ", e);
//			}
//		}
//
//		return rs;
//	}
//
//	public int update(FeedComment msg) {
//		int rs = MongoErrorCode.NOT_CONNECT;
//		if (dbSource != null) {
//			try {
//				MongoMapper mapper = new MongoMapper();
//				Document filter = new Document("_id", msg.getId());
//				Document obj = new Document("$set", mapper.toDocument(msg));
//				obj = mapper.buildUpdateTime(obj);
//				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
//				rs = (int) qRs.getModifiedCount();
//			} catch (final Exception e) {
//				_logger.error("update ", e);
//			}
//		}
//
//		return rs;
//	}
//	
//	public int delete(long id) {
//		int rs = MongoErrorCode.NOT_CONNECT;
//		if (dbSource != null) {
//			try {
//				MongoMapper mapper = new MongoMapper();
//				Document filter = new Document("_id", id);
//				Document obj = new Document("$set", new Document("status", 0));
//				obj = mapper.buildUpdateTime(obj);
//				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
//				rs = (int) qRs.getModifiedCount();
//			} catch (final Exception e) {
//				_logger.error("delete ", e);
//			}
//		}
//
//		return rs;
//	}
//	
//	public int hardDelete(long id) {
//		int rs = MongoErrorCode.NOT_CONNECT;
//		if (dbSource != null) {
//			try {
//				Document filter = new Document("_id", id);
//				DeleteResult qRs = dbSource.getCollection(TABLE_NAME).deleteOne(filter);
//				rs = (int) qRs.getDeletedCount();
//			} catch (final Exception e) {
//				_logger.error("hardDelete ", e);
//			}
//		}
//
//		return rs;
//	}
//	
//	public int addField(String field, Object defaultValue) {
//		int rs = MongoErrorCode.NOT_CONNECT;
//		if (dbSource != null) {
//			try {
//				Document filter = new Document();
//				Document obj = new Document("$set", new Document(field, defaultValue));
//				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateMany(filter, obj);
//				rs = (int) qRs.getModifiedCount();
//			} catch (final Exception e) {
//				_logger.error("addField ", e);
//			}
//		}
//
//		return rs;
//	}
//
//	private class MongoMapper extends MongoDBParse<FeedComment> {
//
//		@Override
//		public FeedComment parseObject(Document doc) {
//			List<Long> likeUserIds = doc.get("likeUserIds") != null ? (List<Long>) doc.get("likeUserIds") : null;
//			FeedComment comment = new FeedComment(doc.getLong("_id"), doc.getLong("commentParentId"), doc.getLong("feedId"), doc.getLong("feedItemId"),
//					doc.getLong("userId"), doc.getString("content"), doc.getString("sticker"), doc.getString("photoCmtId"), likeUserIds,
//					doc.getDate("createTime").getTime(), doc.getDate("updateTime").getTime(), doc.getInteger("status"));
//			return comment;
//		}
//
//		@Override
//		public Document toDocument(FeedComment obj) {
//			Document doc = new Document("_id", obj.getId())
//					.append("commentParentId", obj.getCommentParentId())
//					.append("feedId", obj.getFeedId())
//					.append("feedItemId", obj.getFeedItemId())
//					.append("userId", obj.getUserId())
//					.append("content", obj.getContent())
//					.append("sticker", obj.getSticker())
//					.append("photoCmtId", obj.getPhotoCmtId())
//					.append("status", obj.getStatus())
//					.append("likeUserIds", obj.getLikeUserIds());
//			return doc;
//		}
//
//	}
//	
//}
