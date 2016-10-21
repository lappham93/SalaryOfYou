package com.mit.dao.user;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.entities.user.WhiteList;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;

public class WhiteListDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory
			.getLogger(WhiteListDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static WhiteListDAO _instance;

	private final String TABLE_NAME = "white_list";

	public static WhiteListDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new WhiteListDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private WhiteListDAO() {
	}
	
	public long totalAll() {
		long count = 0;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("status", new Document("$gt", 0));
				count = dbSource.getCollection(TABLE_NAME).count(objFinder);
			} catch(final Exception e) {
				_logger.error("totalAll ", e);
			}
		}

		return count;
	}
	
	public List<Integer> getAll(int status) {
		List<Integer> rs = new ArrayList<Integer>();
		if(dbSource != null) {
			try {
				Document filter = new Document("status", status);
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(filter);
				if (docs != null) {
					MongoCursor<Document> tmp = docs.iterator();
					while(tmp.hasNext()) {
						rs.add(tmp.next().getInteger("_id"));
					}
				}
			} catch(final Exception e) {
				_logger.error("getAll ", e);
			}
		}

		return rs;
	}
	
	public int insert(WhiteList wl) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document obj = mapper.toDocument(wl);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}
	
	public int insertBatch(List<WhiteList> wls) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				List<Document> docs = mapper.toDocumentList(wls);
				for (Document doc : docs) {
					doc = mapper.buildInsertTime(doc);
				}
				dbSource.getCollection(TABLE_NAME).insertMany(docs);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insertBatch ", e);
			}
		}

		return rs;
	}
	
	public int delete(int id) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document("_id", id);
				Document update = new Document("$set", new Document("$status", 0));
				UpdateResult updateRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, update);
				rs = (int)updateRs.getModifiedCount();
			} catch(Exception e) {
				_logger.error("delete ", e);
			}
		}

		return rs;
	}
	
	public int updateStatusBatch(List<Integer> ids, int status) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document("_id", new Document("$in", ids));
				Document update = new Document("$set", new Document("status", status));
				UpdateResult updateRs = dbSource.getCollection(TABLE_NAME).updateMany(filter, update);
				rs = (int)updateRs.getModifiedCount();
			} catch(Exception e) {
				_logger.error("updateStatusBatch ", e);
			}
		}

		return rs;
	}
	
	public int update(WhiteList wl) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", wl.getUserId());
				Document update = new Document("$set", mapper.toDocument(wl));
				update = mapper.buildUpdateTime(update);
				UpdateResult ur = dbSource.getCollection(TABLE_NAME).updateOne(filter, update);
				rs = (int)ur.getModifiedCount();
			} catch(Exception e) {
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

	private class MongoMapper extends MongoDBParse<WhiteList> {

		@Override
		public WhiteList parseObject(Document doc) {
			WhiteList wl = new WhiteList(doc.getInteger("_id"), doc.getInteger("status"));
			return wl;
		}

		@Override
		public Document toDocument(WhiteList obj) {
			Document doc = new Document("_id", obj.getUserId())
					.append("status", obj.getStatus());
			return doc;
		}

	}
	
}