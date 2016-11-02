package com.mit.dao.salary;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.dao.mid.MIdGenLongDAO;
import com.mit.entities.salary.JobShare;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class JobShareDAO extends CommonDAO {

	private final Logger _logger = LoggerFactory.getLogger(JobShareDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static JobShareDAO _instance;

	private String TABLE_NAME = "job_share";

	public static JobShareDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new JobShareDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private JobShareDAO() {
	}

	public String getTableName() {
		return this.TABLE_NAME;
	}

	public JobShare getById(long id) {
		JobShare comment = null;
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

	public int insert(JobShare msg) {
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

	public int update(JobShare msg) {
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

	private class MongoMapper extends MongoDBParse<JobShare> {

		@Override
		public JobShare parseObject(Document doc) {
			JobShare ss = new JobShare(doc.getLong("_id"), doc.getLong("jobCategoryId"), doc.getLong("jobId"), doc.getInteger("yearExperience"), 
					doc.getString("skill"), doc.getInteger("skillLevel"), doc.getString("city"), doc.getString("country"), doc.getString("companyCountry"), 
					doc.getDouble("monthlySalary"), doc.getInteger("status"), doc.getDate("createTime").getTime(), doc.getDate("updateTime").getTime());
			return ss;
		}

		@Override
		public Document toDocument(JobShare obj) {
			Document doc = new Document("_id", obj.getId())
					.append("jobCategoryId", obj.getJobCategoryId())
					.append("jobId", obj.getJobId())
					.append("yearExperience", obj.getYearExperience())
					.append("skill", obj.getSkill())
					.append("skillLevel", obj.getSkillLevel())
					.append("city", obj.getCity())
					.append("country", obj.getCountry())
					.append("companyCountry", obj.getCompanyCountry())
					.append("monthlySalary", obj.getMonthlySalary())
					.append("status", obj.getStatus());

			return doc;
		}

	}

}
