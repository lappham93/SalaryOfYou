package com.mit.dao;


import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.AppConstant;
import com.mit.constants.MongoErrorCode;
import com.mit.dao.mid.MIdGenLongDAO;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class AppConstantDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(AppConstantDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static AppConstantDAO _instance;

	private String TABLE_NAME = "app_constant";

	public static AppConstantDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new AppConstantDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}
    
    public List<AppConstant> listAll() {
		List<AppConstant> products = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document();
				Document sort = new Document("updateTime", -1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort);
				if (doc != null) {
					products = new MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("listAll ", e);
			}
		}
		return products;
	}

	public AppConstant getByKey(String key) {
		AppConstant product= null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", key);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					product= new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return product;
	}
    
	public int insert(AppConstant appConstant) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document obj = mapper.toDocument(appConstant);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}

	public int update(AppConstant appConstant) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", appConstant.getKey());
				Document obj = new Document("$set", mapper.toDocument(appConstant));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("update ", e);
			}
		}

		return rs;
	}
	
	public int upsert(AppConstant appConstant) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", appConstant.getKey());
				Document obj = mapper.toDocument(appConstant);
				if (appConstant.getCreateTime() == 0) {
					obj = mapper.buildInsertTime(obj);
				} else {
					obj = mapper.buildUpdateTime(obj);
				}
				Document update = new Document("$set", obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, update, new UpdateOptions().upsert(true));
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("upsert ", e);
			}
		}

		return rs;
	}
	
	public int hardDelete(String key) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document("_id", key);
				DeleteResult updateRs = dbSource.getCollection(TABLE_NAME).deleteOne(filter);
				rs = (int)updateRs.getDeletedCount();
			} catch(Exception e) {
				_logger.error("hardDelete ", e);
			}
		}

		return rs;
	}
	
	public int truncate() {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document();
				DeleteResult qRs = dbSource.getCollection(TABLE_NAME).deleteMany(filter);
				rs = (int) qRs.getDeletedCount();
				
				MIdGenLongDAO.getInstance(TABLE_NAME).reset();
			} catch(final Exception e) {
				_logger.error("truncate ", e);
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

	private class MongoMapper extends MongoDBParse<AppConstant> {
		@Override
		public AppConstant parseObject(Document doc) {
			AppConstant account = new AppConstant(doc.getString("_id"), doc.getString("value"), doc.getString("desc"),
					doc.getDate("createTime").getTime(), doc.getDate("updateTime").getTime());
			return account;
		}

		@Override
		public Document toDocument(AppConstant obj) {
			Document doc = new Document("_id", obj.getKey())
					.append("value", obj.getValue())
					.append("desc", obj.getDesc());
			return doc;
		}

	}
}
