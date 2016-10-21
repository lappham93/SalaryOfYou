package com.mit.dao.social;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.entities.social.FeedNotification;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class FeedNotificationDAO extends CommonDAO {

	private final Logger _logger = LoggerFactory.getLogger(FeedNotificationDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static FeedNotificationDAO _instance;

	private String TABLE_NAME = "feed_notification";

	public static FeedNotificationDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new FeedNotificationDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private FeedNotificationDAO() {
	}

	public String getTableName() {
		return this.TABLE_NAME;
	}

	public FeedNotification getById(int objectType, long objectId) {
		FeedNotification unfollowUser = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("objectType", objectType)
						.append("objectId", objectId);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					unfollowUser = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return unfollowUser;
	}
	

	public int insert(FeedNotification msg) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
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

	public int update(FeedNotification msg) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("objectType", msg.getObjectType())
						.append("objectId", msg.getObjectId());
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

	public int addField(String field, Object defaultValue) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document();
				Document obj = new Document("$set", new Document(field, defaultValue));
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateMany(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch (final Exception e) {
				_logger.error("addField ", e);
			}
		}

		return rs;
	}

	public int hardDelete(int objectType, long objectId) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document("objectType", objectType)
						.append("objectId", objectId);
				DeleteResult qRs = dbSource.getCollection(TABLE_NAME).deleteOne(filter);
				rs = (int) qRs.getDeletedCount();
			} catch (final Exception e) {
				_logger.error("hardDelete ", e);
			}
		}

		return rs;
	}

	private class MongoMapper extends MongoDBParse<FeedNotification> {

		@Override
		public FeedNotification parseObject(Document doc) {
			FeedNotification unfollowUser = new FeedNotification(doc.getInteger("objectType"), doc.getLong("objectId"), doc.getLong("userId"), (List<Long>) doc.get("otherUserIds"));
			return unfollowUser;
		}

		@Override
		public Document toDocument(FeedNotification obj) {
			Document doc = new Document("objectType", obj.getObjectType())
					.append("objectId", obj.getObjectId())
					.append("userId", obj.getUserId())
					.append("otherUserIds", obj.getOtherUserIds());
			return doc;
		}

	}
}
