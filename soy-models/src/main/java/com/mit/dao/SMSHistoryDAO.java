package com.mit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.mid.MIdGenDAO;
import com.mit.entities.SMSHistory;
import com.mit.utils.IDGeneration;

public class SMSHistoryDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(SMSHistoryDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static SMSHistoryDAO _instance;

	private final String TABLE_NAME = "sms_history";

	public static SMSHistoryDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new SMSHistoryDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private SMSHistoryDAO() {
	}

	public SMSHistory getByMsgId(String msgId) {
		SMSHistory item = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("msgId", msgId);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					item = new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getByMsgId ", e);
			}
		}
		return item;
	}

	public long insert(SMSHistory smsHistory) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				if(smsHistory.getId() <= 0) {
					smsHistory.setId(MIdGenDAO.getInstance(TABLE_NAME).getNext());
				}
				MongoMapper mapper = new MongoMapper();
				Document obj = mapper.toDocument(smsHistory);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}

	public long updateDeliver(String msgId, int status) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("msgId", msgId);
				Document obj = new Document("$set", new Document("deliverStatus", status).append("deliverTime", new Date()));
				dbSource.getCollection(TABLE_NAME).updateOne(objFinder, obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("updateDeliver ", e);
			}
		}

		return rs;
	}

	private class MongoMapper extends MongoDBParse<SMSHistory> {
//long id, String smsFrom, String smsTo, String content, String type, String msgId, BigDecimal price, long createTime, long deliverTime, int deliverStatus
		@Override
		public SMSHistory parseObject(Document doc) {
			SMSHistory deviceToken = new SMSHistory(doc.getLong("_id"), doc.getString("provider"), doc.getString("smsFrom"), doc.getString ("smsTo"), doc.getString("content"), doc.getString("type"), doc.getString("msgId"),
					new BigDecimal(doc.getDouble("price")), doc.getDate("createTime").getTime(), doc.getDate("deliverTime").getTime(),doc.getInteger("deliverStatus"));
			return deviceToken;
		}

		@Override
		public Document toDocument(SMSHistory obj) {
			Document doc = new Document("_id", obj.getId()).append("provider", obj.getProvider()).append("smsFrom", obj.getSmsFrom()).append("smsTo", obj.getSmsTo()).append("content", obj.getContent())
					.append("type", obj.getType()).append("msgId", obj.getMsgId()).append("price", obj.getPrice().doubleValue()).append("createTime", obj.getCreateTime() == 0 ? new Date() : new Date(obj.getCreateTime()))
					.append("deliverTime", obj.getDeliverTime() == 0 ? new Date() : new Date(obj.getDeliverTime())).append("deliverStatus", obj.getDeliverStatus());
			return doc;
		}

	}
}
