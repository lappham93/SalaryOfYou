package com.mit.dao.link;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.entities.link.Link;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class LinkDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(LinkDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static LinkDAO _instance;

	private String TABLE_NAME = "link";

	public static LinkDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new LinkDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private LinkDAO() {
	}

	public String getTableName() {
		return TABLE_NAME;
	}

	public Link getById(long id) {
		Link feedLink = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					feedLink = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return feedLink;
	}
	
	public int insert(Link msg) {
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

	public int update(Link msg) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", msg.getId());
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

	public int hardDelete(String id) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document("_id", id);
				DeleteResult qRs = dbSource.getCollection(TABLE_NAME).deleteOne(filter);
				rs = (int) qRs.getDeletedCount();
			} catch (final Exception e) {
				_logger.error("hardDelete ", e);
			}
		}

		return rs;
	}

	private class MongoMapper extends MongoDBParse<Link> {

		@Override
		public Link parseObject(Document doc) {
			Link feedLink = new Link(doc.getLong("_id"), doc.getString("link"), doc.getString("site"), doc.getString("title"), doc.getString("desc"),
					doc.getLong("thumbnail"), doc.getInteger("status"),
					doc.getDate("createTime").getTime(), doc.getDate("updateTime").getTime());

			return feedLink;
		}

		@Override
		public Document toDocument(Link obj) {
			Document doc = new Document("_id", obj.getId())
					.append("link", obj.getLink())
					.append("title", obj.getTitle())
					.append("description", obj.getDesc())
					.append("site", obj.getSite())
					.append("thumbnail", obj.getThumbnail())
					.append("status", obj.getStatus());
			
			return doc;
		}

	}
	
	public static void main(String[] args) {
	}

}
