package com.mit.dao.upload;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.entities.upload.UploadTemp;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

public class ChatPhotoDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(ChatPhotoDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static ChatPhotoDAO _instance;

	private final String TABLE_NAME = "upload_chats";

	public static ChatPhotoDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new ChatPhotoDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private ChatPhotoDAO() {
		super("upload");
//		if(dbSource != null) {
//			ListIndexesIterable<Document> listIndex = dbSource.getCollection(TABLE_NAME).listIndexes();
//			if(listIndex != null && listIndex.first() != null) {
//				dbSource.getCollection(TABLE_NAME).createIndex(new Document("_id", "-1"), new IndexOptions().expireAfter(3600l, TimeUnit.SECONDS));
//			}
//		}
	}

	public Map<String, Object> getById(long id) {
		Map<String, Object> data = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
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

	public int updateData(long id, int userId, byte[] data) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", id);
				Document obj = new Document("$set", new Document("data", data).append("userId", userId));
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
