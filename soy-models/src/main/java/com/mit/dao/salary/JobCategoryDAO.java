package com.mit.dao.salary;

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
import com.mit.entities.react.ReactStat;
import com.mit.entities.salary.JobCategory;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class JobCategoryDAO extends CommonDAO {

	private final Logger _logger = LoggerFactory.getLogger(JobCategoryDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static JobCategoryDAO _instance;

	private String TABLE_NAME = "job_category";

	public static JobCategoryDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new JobCategoryDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private JobCategoryDAO() {
	}

	public String getTableName() {
		return this.TABLE_NAME;
	}

	public JobCategory getById(long id) {
		JobCategory comment = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					comment = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return comment;
	}

	public int countTotal(int objectType, long objectId, int socialType) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("objectType", objectType).append("objectId", objectId)
						.append("socialType", socialType);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					rs = doc.getInteger("total");
				}
			} catch (final Exception e) {
				_logger.error("countTotal ", e);
			}
		}

		return rs;
	}

	public List<ReactStat> getByObject(int objectType, long objectId) {
		List<ReactStat> stats = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("objectType", objectType).append("objectId", objectId);
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if (docs != null) {
					stats = new MongoMapper().parseList(docs);
				}
			} catch (final Exception e) {
				_logger.error("getByObject ", e);
			}
		}

		return stats;
	}
	
	public boolean hasReact(int objectType, long objectId, int socialType, long userId) {
		boolean rs = false;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("objectType", objectType)
						.append("objectId", objectId)
						.append("socialType", socialType)
						.append("userIds", userId);
				int count = (int) dbSource.getCollection(TABLE_NAME).count(objFinder);
				rs = count > 0;
			} catch (final Exception e) {
				_logger.error("hasReact ", e);
			}
		}

		return rs;
	}
	
	public int insert(ReactStat msg) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				if (msg.getId() <= 0) {
					msg.setId(MIdGenLongDAO.getInstance(TABLE_NAME).getNext());
				}
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

	public ReactStat insertGet(ReactStat msg) {
		ReactStat rs = null;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				if (msg.getId() <= 0) {
					msg.setId(MIdGenLongDAO.getInstance(TABLE_NAME).getNext());
				}
				Document obj = mapper.toDocument(msg);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = mapper.parseObject(obj);
			} catch (final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}

	public int update(ReactStat msg) {
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

	public ReactStat updateGet(ReactStat msg) {
		ReactStat rs = null;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", msg.getId());
				Document obj = new Document("$set", mapper.toDocument(msg));
				obj = mapper.buildUpdateTime(obj);
				dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = mapper.parseObject(obj);
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

	public int delete(List<Long> ids) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", new Document("$in", ids));
				Document obj = new Document("$set", new Document("status", 0));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateMany(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch (final Exception e) {
				_logger.error("delete ", e);
			}
		}

		return rs;
	}

	public int hardDelete(long id) {
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

	private class MongoMapper extends MongoDBParse<JobCategory> {

		@Override
		public JobCategory parseObject(Document doc) {
			JobCategory ss = new JobCategory(doc.getLong("_id"), doc.getString("name"), doc.getString("desc"), 
					doc.getDate("createTime").getTime(), doc.getDate("updateTime").getTime());

			return ss;
		}

		@Override
		public Document toDocument(JobCategory obj) {
			Document doc = new Document("_id", obj.getId())
					.append("name", obj.getName())
					.append("desc", obj.getDesc());

			return doc;
		}

	}

}
