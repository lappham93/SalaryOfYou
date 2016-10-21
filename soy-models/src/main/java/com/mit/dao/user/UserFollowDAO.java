package com.mit.dao.user;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

public class UserFollowDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(UserFollowDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static UserFollowDAO _instance;

	private final String TABLE_NAME = "user_follow";

	public static UserFollowDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new UserFollowDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private UserFollowDAO() {}

	public long addFollow(int uid, int bizId) {
		long rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", uid).append("followIds", new Document("$ne", bizId));
				Document update = new Document("$push", new Document("followIds", new Document("$each", Arrays.asList(bizId))
					.append("$position", 0)));
				UpdateResult doc = dbSource.getCollection(TABLE_NAME).updateOne(objFinder, update, new UpdateOptions().upsert(true));

				rs = doc.getModifiedCount() + (doc.getUpsertedId() != null ? 1 : 0);
				if(rs > 0) {
					update = new Document("$inc", new Document("total", 1));
					objFinder = new Document("_id", uid);
					doc = dbSource.getCollection(TABLE_NAME).updateOne(objFinder, update);
				}
			} catch(final Exception e) {
				_logger.error("addFollow ", e);
			}
		}
		return rs;
	}

	public long removeFollow(int uid, int bizId) {
		long rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", uid);
				Document update = new Document("$pull", new Document("followIds", bizId));
				UpdateResult doc = dbSource.getCollection(TABLE_NAME).updateOne(objFinder, update);

				rs = doc.getModifiedCount();
				if(rs > 0) {
					update = new Document("$inc", new Document("total", -1));
					doc = dbSource.getCollection(TABLE_NAME).updateOne(objFinder, update);
				}
			} catch(final Exception e) {
				_logger.error("removeFollow ", e);
			}
		}
		return rs;
	}

	public List<Integer> getUserFollowed(int userId) {
		List<Integer> listBiz = Collections.emptyList();
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", userId);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null && !doc.isEmpty()) {
					listBiz = (List)doc.get("followIds");
				}
			} catch(final Exception e) {
				_logger.error("getUserFollowed ", e);
			}
		}
		return listBiz;
	}
	
	public boolean isFollowed(int userId, int bizId) {
		boolean followed = false;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", userId).append("followIds", bizId);
				Document projection = new Document("followIds", bizId);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection).first();
				if(doc != null && !doc.isEmpty()) {
					List<Integer> listBiz = (List)doc.get("followIds");
					
					if (listBiz != null && !listBiz.isEmpty()) {
						followed = true;
					}
				}
			} catch(final Exception e) {
				_logger.error("isFollowed ", e);
			}
		}
		return followed;
	}

	public List<Integer> getUserFollowed(int userId, int from, int count) {
		List<Integer> listBiz = Collections.emptyList();
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", userId);
				Document projection = new Document("followIds", new Document("$slice", Arrays.asList(from, count)));
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection).first();
				if(doc != null && !doc.isEmpty()) {
					listBiz = (List)doc.get("followIds");
				}
			} catch(final Exception e) {
				_logger.error("getUserFollowed ", e);
			}
		}
		return listBiz;
	}
	
	public int getTotalFollowed(int userId) {
		int total = 0;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", userId);
				Document projection = new Document("total", 1);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection).first();
				if(doc != null && !doc.isEmpty()) {
					total = doc.getInteger("total", 0);
				}
			} catch(final Exception e) {
				_logger.error("getTotalFollow ", e);
			}
		}
		return total;
	}
	
	public Map<Integer, Integer> getTotalFollowedByListId(List<Integer> userIds) {
		Map<Integer, Integer> total = new HashMap<Integer, Integer>();
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", new Document("$in", userIds));
				Document projection = new Document("total", 1);
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection);
				if(docs != null) {
					Document doc = null;
					MongoCursor<Document> cursor = docs.iterator();
					while(cursor != null && cursor.hasNext() && (doc = cursor.next()) != null) {
						total.put(doc.getInteger("_id"), doc.getInteger("total", 0));
					}
				}
			} catch(final Exception e) {
				_logger.error("getTotalFollowedByListId ", e);
			}
		}
		return total;
	}
}
