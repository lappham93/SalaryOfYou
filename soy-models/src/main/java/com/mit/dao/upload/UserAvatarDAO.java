package com.mit.dao.upload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.constants.MongoErrorCode;
import com.mit.entities.upload.UploadTemp;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

public class UserAvatarDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(UserAvatarDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static UserAvatarDAO _instance;

	private final String TABLE_NAME = "users_avatar";

	public static UserAvatarDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new UserAvatarDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private UserAvatarDAO() {
		super("upload");
//		if(dbSource != null) {
//			ListIndexesIterable<Document> listIndex = dbSource.getCollection(TABLE_NAME).listIndexes();
//			if(listIndex != null && listIndex.first() != null) {
//				dbSource.getCollection(TABLE_NAME).createIndex(new Document("_id", "-1"), new IndexOptions().expireAfter(3600l, TimeUnit.SECONDS));
//			}
//		}
	}

	public Map<String, Object> getByUserId(int id) {
		Map<String, Object> data = null;
		if(dbSource != null) {
			try {
				List<Document> orGroup = new ArrayList<Document>();
				orGroup.add(new Document("_id", id));
				orGroup.add(new Document("_id", 0));
				Document objFinder = new Document("$or", orGroup);
				Document sortBy = new Document("_id", -1);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sortBy).first();
				if(doc != null) {
					data = new HashMap<String, Object>();
					data.putAll(doc);
				}
			} catch(final Exception e) {
				_logger.error("getByUserId ", e);
			}
		}

		return data;
	}

	public boolean isSameVersion(int id, int avtVer) {
		Map<String, Object> data = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", id).append("avtVer", avtVer);
				Document projection = new Document("avtVer", 1);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection).first();
				if(doc != null && !doc.isEmpty()) {
					return true;
				}
			} catch(final Exception e) {
				_logger.error("getByUserId ", e);
			}
		}

		return false;
	}

	public int updateData(int userId, int avtVer, byte[] data) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", userId);
				Document obj = new Document("$set", new Document("data", data).append("avtVer", avtVer));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj, new UpdateOptions().upsert(true));
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("updateData ", e);
			}
		}

		return rs;
	}

	private class MongoMapper extends MongoDBParse<UploadTemp> {

		@Override
		public UploadTemp parseObject(Document doc) {
			return null;
		}

		@Override
		public Document toDocument(UploadTemp obj) {
			return null;
		}
	}

}
