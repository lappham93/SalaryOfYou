package com.mit.dao.react;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.entities.react.CommentNotification;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class CommentNotificationDAO extends CommonDAO {

	private final Logger _logger = LoggerFactory.getLogger(CommentNotificationDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static CommentNotificationDAO _instance;

	private String TABLE_NAME = "comment_notification";

	public static CommentNotificationDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new CommentNotificationDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private CommentNotificationDAO() {
	}

	public String getTableName() {
		return this.TABLE_NAME;
	}

	public CommentNotification getById(long commentId) {
		CommentNotification cmtNot = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", commentId);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					cmtNot = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return cmtNot;
	}

	public int insert(CommentNotification msg) {
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

	public int update(CommentNotification msg) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", msg.getCommentId());
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

	public int hardDelete(long commentId) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document("_id", commentId);
				DeleteResult qRs = dbSource.getCollection(TABLE_NAME).deleteOne(filter);
				rs = (int) qRs.getDeletedCount();
			} catch (final Exception e) {
				_logger.error("hardDelete ", e);
			}
		}

		return rs;
	}

	private class MongoMapper extends MongoDBParse<CommentNotification> {

		@Override
		public CommentNotification parseObject(Document doc) {
			CommentNotification cmtNot = new CommentNotification(doc.getLong("_id"), doc.getInteger("cmtUserId"),
					doc.getLong("feedId"), (List<Integer>) doc.get("userIds"));
			return cmtNot;
		}

		@Override
		public Document toDocument(CommentNotification obj) {
			Document doc = new Document("_id", obj.getCommentId()).append("cmtUserId", obj.getCmtUserId())
					.append("feedId", obj.getFeedId()).append("userIds", obj.getUserIds());
			return doc;
		}

	}
}
