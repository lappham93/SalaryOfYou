package com.mit.dao.salary;

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
import com.mit.entities.salary.AllSalaryStatistics;
import com.mit.entities.salary.ExperienceSalaryStatistics;
import com.mit.entities.salary.JobSalaryStatistics;
import com.mit.entities.salary.PlaceSalaryStatistics;
import com.mit.entities.salary.SalaryStatistics;
import com.mit.entities.salary.SalaryStatisticsType;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class SalaryStatisticsDAO extends CommonDAO {

	private final Logger _logger = LoggerFactory.getLogger(SalaryStatisticsDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static SalaryStatisticsDAO _instance;

	private String TABLE_NAME = "salary_statistics";

	public static SalaryStatisticsDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new SalaryStatisticsDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private SalaryStatisticsDAO() {
	}

	public String getTableName() {
		return this.TABLE_NAME;
	}
	
	public SalaryStatistics getByAttr(int type, Map<String, Object> params) {
		SalaryStatistics comment = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("type", type);
				if (type == SalaryStatisticsType.ALL.getValue()) {
					objFinder.append("jobId", params.get("jobId"))
						.append("yearExperience", params.get("yearExperience"))
						.append("skillLevel", params.get("skillLevel"));
				} else if (type == SalaryStatisticsType.JOB.getValue()) {
					
				} else if (type == SalaryStatisticsType.EXPERIENCE.getValue()) {
					
				} else if (type == SalaryStatisticsType.PLACE.getValue()) {
					
				}
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					comment = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getByAttr ", e);
			}
		}

		return comment;
	}

	public SalaryStatistics getById(long id) {
		SalaryStatistics comment = null;
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

	public int insert(SalaryStatistics msg) {
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

	public int update(SalaryStatistics msg) {
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

	private class MongoMapper extends MongoDBParse<SalaryStatistics> {

		@Override
		public SalaryStatistics parseObject(Document doc) {
			SalaryStatistics ss = null;
			long id = doc.getLong("_id");
			int type = doc.getInteger("type");
			double mean = doc.getDouble("mean");
			long shareCount = doc.getLong("shareCount");
			int status = doc.getInteger("status");
			long createTime = doc.getDate("createTime").getTime();
			long updateTime = doc.getDate("updateTime").getTime();
			if (type == SalaryStatisticsType.ALL.getValue()) {
				ss = new AllSalaryStatistics(id, doc.getLong("jobId"), doc.getInteger("yearExperience"), doc.getInteger("skillLevel"),
						mean, shareCount, status, createTime, updateTime);
			} else if (type == SalaryStatisticsType.JOB.getValue()) {
				ss = new JobSalaryStatistics(id, doc.getLong("jobId"),  mean, shareCount, status, createTime, updateTime);
			} else if (type == SalaryStatisticsType.EXPERIENCE.getValue()) {
				ss = new ExperienceSalaryStatistics(id, doc.getInteger("yearExperience"), mean, shareCount, status, createTime, updateTime);
			} else if (type == SalaryStatisticsType.PLACE.getValue()) {
				ss = new PlaceSalaryStatistics(id, doc.getString("city"), doc.getString("country"), mean, shareCount, status, createTime, updateTime);
			}
			
			return ss;
		}

		@Override
		public Document toDocument(SalaryStatistics obj) {
			Document doc = new Document("_id", obj.getId())
					.append("type", obj.getType())
					.append("mean", obj.getMean())
					.append("shareCount", obj.getShareCount())
					.append("status", obj.getStatus());
			if (obj instanceof AllSalaryStatistics) {
				AllSalaryStatistics allst = (AllSalaryStatistics)obj;
				doc.append("jobId", allst.getJobId())
					.append("yearExperience", allst.getYearExperience())
					.append("skillLevel", allst.getSkillLevel());
			} else if (obj instanceof JobSalaryStatistics) {
				JobSalaryStatistics jobst = (JobSalaryStatistics)obj;
				doc.append("jobId", jobst.getJobId());
			} else if (obj instanceof ExperienceSalaryStatistics) {
				ExperienceSalaryStatistics expst = (ExperienceSalaryStatistics)obj;
				doc.append("yearExperience", expst.getYearExperience());
			} else if (obj instanceof PlaceSalaryStatistics) {
				PlaceSalaryStatistics placest = (PlaceSalaryStatistics)obj;
				doc.append("city", placest.getCity())
					.append("country", placest.getCountry());
			}

			return doc;
		}

	}

}
