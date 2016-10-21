package com.mit.dao.facebook;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.entities.facebook.FBUser;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class FacebookUserDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(FacebookUserDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static FacebookUserDAO _instance;

	private final String TABLE_NAME = "facebook_user";

	public static FacebookUserDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new FacebookUserDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private FacebookUserDAO() {
	}

	public FBUser getByUserId(int userId) {
		FBUser fbUser = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", userId);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					fbUser = new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getByUserId ", e);
			}
		}

		return fbUser;
	}

	public int insert(FBUser fbUser) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document obj = mapper.toDocument(fbUser);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}
	
	public int upsert(FBUser fbUser) {		
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", fbUser.getUserId());
				Document obj = mapper.toDocument(fbUser);
				obj = mapper.buildInsertTime(obj);
				Document set = new Document("$set", obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, set, new UpdateOptions().upsert(true));
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("upsert ", e);
			}
		}

		return rs;
	}

	public int update(FBUser fbUser) {		
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", fbUser.getUserId());
				Document obj = new Document("$set", mapper.toDocument(fbUser));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("update ", e);
			}
		}

		return rs;
	}

	public int delete(int userId) {		
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document("_id", userId);
				DeleteResult qRs = dbSource.getCollection(TABLE_NAME).deleteOne(filter);
				rs = (int) qRs.getDeletedCount();
			} catch(final Exception e) {
				_logger.error("delete ", e);
			}
		}

		return rs;
	}
	
	private class MongoMapper extends MongoDBParse<FBUser> {

		@Override
		public FBUser parseObject(Document doc) {
			FBUser fbUser = new FBUser(doc.getInteger("_id"), doc.getString ("facebookId"),
					doc.getDate("createTime").getTime());

			return fbUser;
		}

		@Override
		public Document toDocument(FBUser obj) {
			Document doc = new Document("_id", obj.getUserId())
				.append("facebookId", obj.getFacebookId());
			return doc;
		}

	}
}
