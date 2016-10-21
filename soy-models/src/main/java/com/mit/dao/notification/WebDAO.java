package com.mit.dao.notification;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.dao.mid.MIdGenLongDAO;
import com.mit.entities.notification.WebNews;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class WebDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(WebDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static WebDAO _instance;

	private String TABLE_NAME = "web";

	public static WebDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new WebDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	public List<WebNews> listAll() {
		List<WebNews> news = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document();
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if (doc != null) {
					news = new MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("listAll ", e);
			}
		}
		return news;
	}

	public WebNews getById(long id) {
		WebNews product = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					product = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return product;
	}

	public int insert(WebNews product) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				if (product.getuId() <= 0) {
					product.setuId(MIdGenLongDAO.getInstance(TABLE_NAME).getNext());
				}
				Document obj = mapper.toDocument(product);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch (final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}

	public int update(WebNews product) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", product.getuId());
				Document obj = new Document("$set", mapper.toDocument(product));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch (final Exception e) {
				_logger.error("update ", e);
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

	public int truncate() {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document();
				DeleteResult qRs = dbSource.getCollection(TABLE_NAME).deleteMany(filter);
				rs = (int) qRs.getDeletedCount();
			} catch (final Exception e) {
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
	
	private class MongoMapper extends MongoDBParse<WebNews> {

		@Override
		public WebNews parseObject(Document doc) {
			WebNews news = new WebNews();
			news.setuId(doc.getLong("_id"));
			news.setId(doc.getString("id"));
			news.setMsg(doc.getString("message"));
			news.setThumb(doc.getLong("thumb"));
			news.setStatus(doc.getInteger("status"));

			return news;
		}

		@Override
		public Document toDocument(WebNews obj) {
			Document doc = new Document("_id", obj.getuId())
					.append("id", obj.getId())
					.append("message", obj.getMsg())
					.append("thumb", obj.getThumb())
					.append("status", obj.getStatus());

			return doc;
		}

	}
}
