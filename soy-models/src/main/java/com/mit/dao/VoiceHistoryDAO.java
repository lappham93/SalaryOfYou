package com.mit.dao;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.entities.VoiceHistory;
import com.mit.utils.IDGeneration;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.UpdateResult;

public class VoiceHistoryDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(VoiceHistoryDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static VoiceHistoryDAO _instance;

	private final String TABLE_NAME = "voice_history";

	public static VoiceHistoryDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new VoiceHistoryDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private VoiceHistoryDAO() {
	}

	public VoiceHistory getByCallId(String callId) {
		VoiceHistory item = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("callId", callId);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					item = new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getByCallId ", e);
			}
		}
		return item;
	}
	
	public List<VoiceHistory> getLast(int count) {
		List<VoiceHistory> item = Collections.emptyList();
		if(dbSource != null) {
			try {
				Document objFinder = new Document();
				Document sortBy = new Document("createTime", -1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sortBy).limit(count);
				if(doc != null) {
					item = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getLast ", e);
			}
		}
		return item;
	}

	public long insert(VoiceHistory voiceHistory) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				if(voiceHistory.getId() <= 0) {
					voiceHistory.setId(IDGeneration.Instance.generateId(TABLE_NAME));
				}
				MongoMapper mapper = new MongoMapper();
				Document obj = mapper.toDocument(voiceHistory);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}
	
	public int update(VoiceHistory voiceHistory) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", voiceHistory.getId());
				Document obj = new Document("$set", mapper.toDocument(voiceHistory));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("update ", e);
			}
		}

		return rs;
	}

	private class MongoMapper extends MongoDBParse<VoiceHistory> {
		@Override
		public VoiceHistory parseObject(Document doc) {
			VoiceHistory deviceToken = new VoiceHistory(doc.getLong("_id"), 
					doc.getString("provider"), 
					doc.getString("from"), 
					doc.getString ("to"), 
					doc.getString("type"), 
					doc.getString("callId"),
					BigDecimal.valueOf(doc.getDouble("price")), 
					doc.getInteger("duration"),
					doc.getDate("requestTime").getTime(),
					doc.getDate("startTime").getTime(),
					doc.getDate("endTime").getTime(), 
					doc.getString("callStatus"),
					doc.getDate("createTime").getTime(),
					doc.getDate("updateTime").getTime());
			return deviceToken;
		}

		@Override
		public Document toDocument(VoiceHistory obj) {
			Document doc = new Document("_id", obj.getId())
				.append("provider", obj.getProvider())
				.append("from", obj.getFrom())
				.append("to", obj.getTo())
				.append("type", obj.getType())
				.append("callId", obj.getCallId())
				.append("price", obj.getPrice().doubleValue())
				.append("duration", obj.getDuration())
				.append("requestTime", new Date(obj.getRequestTime()))
				.append("startTime", new Date(obj.getStartTime()))
				.append("endTime", new Date(obj.getEndTime()))
				.append("callStatus", obj.getCallStatus());
			return doc;
		}

	}
}
