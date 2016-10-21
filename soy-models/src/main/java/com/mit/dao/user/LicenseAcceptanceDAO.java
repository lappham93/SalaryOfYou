package com.mit.dao.user;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.dao.mid.MIdGenDAO;
import com.mit.entities.user.LicenseAcceptance;
import com.mit.utils.IDGeneration;

public class LicenseAcceptanceDAO extends CommonDAO{
	private final Logger _logger = LoggerFactory.getLogger(LicenseAcceptanceDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static LicenseAcceptanceDAO _instance;

	private final String TABLE_NAME = "license_acceptance";

	public static LicenseAcceptanceDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new LicenseAcceptanceDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private LicenseAcceptanceDAO() {
	}

	public int insert(LicenseAcceptance acceptance) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				acceptance.setId(MIdGenDAO.getInstance(TABLE_NAME).getNext());
				MongoMapper mapper = new MongoMapper();
				Document obj = mapper.toDocument(acceptance);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}

	public int updateAccount(int licenseId, int accountId) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document obj = new Document("$set", new Document("accountId", accountId));
				Document filter = new Document("id", licenseId);
				dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("updateAccount ", e);
			}
		}

		return rs;
	}

	private class MongoMapper extends MongoDBParse<LicenseAcceptance> {

		@Override
		public LicenseAcceptance parseObject(Document doc) {
			LicenseAcceptance deviceToken = new LicenseAcceptance(doc.getLong("id"), doc.getLong("accountId"), doc.getString ("requestInfo"), doc.getBoolean("isAccepted"), doc.getDate("acceptTime").getTime());
			return deviceToken;
		}

		@Override
		public Document toDocument(LicenseAcceptance obj) {
			Document doc = new Document("_id", obj.getId()).append("accountId", obj.getAccountId()).append("requestInfo", obj.getRequestInfo())
					.append("isAccepted", obj.isAccepted()).append("acceptTime", obj.getAcceptTime());
			return doc;
		}

	}
}
