package com.mit.dao.social;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.entities.social.FeedReport;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.UpdateResult;

public class FeedReportDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(FeedReportDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static FeedReportDAO _instance;

	private String TABLE_NAME = "feed_report";

	public static FeedReportDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new FeedReportDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private FeedReportDAO() {
	}

	public String getTableName() {
		return TABLE_NAME;
	}

	public List<FeedReport> getListfromIds(List<Long> ids) {
		List<FeedReport> freport = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", new Document("$in", ids));
				Document sort = new Document("createTime", 1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort);
				if (doc != null) {
					freport = new MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("getListfromIds ", e);
			}
		}

		return freport;
	}
	
	public List<FeedReport> getAll() {
		List<FeedReport> freport = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("status", new Document("$eq", FeedReport.ReportStatus.NEW.getValue()));
				Document sort = new Document("reports", -1).append("createTime", -1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort);
				if (doc != null) {
					freport = new MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("getAll ", e);
			}
		}

		return freport;
	}
	
	public List<FeedReport> getAllByStatus(int status) {
		List<FeedReport> freport = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("status", status);
				Document sort = new Document("createTime", 1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort);
				if (doc != null) {
					freport = new MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("getAllByStatus ", e);
			}
		}

		return freport;
	}

	public FeedReport getById(long id) {
		FeedReport freport = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					freport = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return freport;
	}
	
	public FeedReport getByFeedId(long feedId) {
		FeedReport freport = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("feedId", feedId);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					freport = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return freport;
	}

	public int insert(FeedReport msg) {
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

	public int update(FeedReport msg) {
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

	private class MongoMapper extends MongoDBParse<FeedReport> {

		@Override
		public FeedReport parseObject(Document doc) {
			FeedReport report = new FeedReport(doc.getLong("_id"), doc.getLong("feedId"), doc.getInteger("reports"),
					(List<Long>) doc.get("reportUserIds"), doc.getInteger("status"),
					doc.getDate("createTime").getTime(), doc.getDate("updateTime").getTime());
			return report;
		}

		@Override
		public Document toDocument(FeedReport obj) {
			Document doc = new Document("_id", obj.getId())
					.append("feedId", obj.getFeedId())
					.append("reports", obj.getReports())
					.append("reportUserIds", obj.getUserReportIds())
					.append("status", obj.getStatus());
			return doc;
		}

	}
}
