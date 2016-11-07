package com.mit.dao.salary;

import java.util.Calendar;
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
import com.mit.dao.mid.MIdGenLongDAO;
import com.mit.entities.salary.SalaryDistributor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class SalaryDistributorDAO extends CommonDAO {

	private final Logger _logger = LoggerFactory.getLogger(SalaryDistributorDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static SalaryDistributorDAO _instance;

	private String TABLE_NAME = "salary_distributor";

	public static SalaryDistributorDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new SalaryDistributorDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private SalaryDistributorDAO() {
	}

	public String getTableName() {
		return this.TABLE_NAME;
	}
	
	public SalaryDistributor getById(long id) {
		SalaryDistributor comment = null;
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
	
	public Map<Integer, SalaryDistributor> getByStatisticsId(long salaryStatisticsId) {
		Map<Integer, SalaryDistributor> comment = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("salaryStatisticsId", salaryStatisticsId);
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if (docs != null) {
					comment = new HashMap<Integer, SalaryDistributor>();
					MongoCursor<Document> tmps = docs.iterator();
					MongoMapper mapper = new MongoMapper();
					while (tmps.hasNext()) {
						Document tmp = tmps.next();
						comment.put(tmp.getInteger("level"), mapper.parseObject(tmp));
					}
				}
			} catch (final Exception e) {
				_logger.error("getByStatisticsId ", e);
			}
		}

		return comment;
	}
	
	public int getUpdateStatus(long salaryStatisticsId) {
		int needUpdate = -1;
		if (dbSource != null) {
			try {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 7);
				Document objFinder = new Document("salaryStatisticsId", salaryStatisticsId);
				Document project =new Document("updateTime", 1);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(project).first();
				if (doc != null) {
					needUpdate = doc.getDate("updateTime").getTime() > cal.getTimeInMillis() ? 1 : 0;
				} else {
					needUpdate = -1;
				}
			} catch (final Exception e) {
				_logger.error("getUpdateStatus ", e);
			}
		}

		return needUpdate;
	}

	public int insert(SalaryDistributor msg) {
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

	public int update(SalaryDistributor msg) {
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

	private class MongoMapper extends MongoDBParse<SalaryDistributor> {

		@Override
		public SalaryDistributor parseObject(Document doc) {
			SalaryDistributor ss = new SalaryDistributor(doc.getLong("_id"), doc.getLong("salaryStatisticsId"), doc.getInteger("type"), doc.getInteger("level"),
					doc.getDouble("maxRagne"), doc.getDouble("minRange"), doc.getDouble("max"), doc.getDouble("min"), doc.getDouble("mean"), doc.getLong("eleCount"),
					doc.getDate("createTime").getTime(), doc.getDate("updateTime").getTime());
			
			return ss;
		}

		@Override
		public Document toDocument(SalaryDistributor obj) {
			Document doc = new Document("_id", obj.getId())
					.append("salaryStatisticsId", obj.getSalaryStatisticsId())
					.append("type", obj.getType())
					.append("level", obj.getLevel())
					.append("minRange", obj.getMinRange())
					.append("maxRange", obj.getMaxRange())
					.append("min", obj.getMin())
					.append("max", obj.getMax())
					.append("mean", obj.getMean())
					.append("eleCount", obj.getEleCount());
			return doc;
		}

	}

}
