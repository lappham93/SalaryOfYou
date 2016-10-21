package com.mit.dao.user;


import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.entities.user.LicenseCode;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class LicenseCodeDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(LicenseCodeDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static LicenseCodeDAO _instance;

	private String TABLE_NAME = "license_code";

	public static LicenseCodeDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new LicenseCodeDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}
	
	public LicenseCode getByCode(String code) {
		LicenseCode productOption = null;
		if(dbSource != null) {
			try {
				code = code.toUpperCase();
				Document objFinder = new Document("_id", code.toUpperCase());
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					productOption = new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getByCode ", e);
			}
		}

		return productOption;
	}
    
	public int insert(LicenseCode licenseCode) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				licenseCode.setCode(licenseCode.getCode().toUpperCase());
				Document obj = mapper.toDocument(licenseCode);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}
	
	public int insertBatch(List<LicenseCode> licenseCodes) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				List<Document> objs = new LinkedList<Document>();
				for (LicenseCode licenseCode: licenseCodes) {
					licenseCode.setCode(licenseCode.getCode().toUpperCase());
					Document obj = mapper.toDocument(licenseCode);
					obj = mapper.buildInsertTime(obj);
					objs.add(obj);
				}
				dbSource.getCollection(TABLE_NAME).insertMany(objs);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insertBatch ", e);
			}
		}

		return rs;
	}

	public int update(LicenseCode licenseCode) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", licenseCode.getCode());
				licenseCode.setCode(licenseCode.getCode().toUpperCase());
				Document obj = new Document("$set", mapper.toDocument(licenseCode));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("update ", e);
			}
		}

		return rs;
	}

	public int delete(long id) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", id);
				Document obj = new Document("$set", new Document("status", 0));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("update ", e);
			}
		}

		return rs;
	}
	
	public int truncate() {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document();
				DeleteResult qRs = dbSource.getCollection(TABLE_NAME).deleteMany(filter);
				rs = (int) qRs.getDeletedCount();
			} catch(final Exception e) {
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

	private class MongoMapper extends MongoDBParse<LicenseCode> {

		@Override
		public LicenseCode parseObject(Document doc) {
			LicenseCode account = new LicenseCode(doc.getString("_id"), doc.getString("alternativeCode"),
					doc.getString("licensee"), doc.getString("addressLine1"), 
					doc.getString("addressLine2"), doc.getString("city"), doc.getString("state"), 
					doc.getString("country"), doc.getString("zipCode"), doc.getString("countryCode"), 
					doc.getLong("licenseDate"), doc.getLong("effectiveDate"), doc.getLong("expirationDate"), 
					doc.getInteger("status"), doc.getDate("createTime").getTime(), doc.getDate("updateTime").getTime());
			return account;
		}

		@Override
		public Document toDocument(LicenseCode obj) {
			Document doc = new Document("_id", obj.getCode())
					.append("alternativeCode", obj.getAlternativeCode())
					.append("licensee", obj.getLicensee())
					.append("addressLine1", obj.getAddressLine1())
					.append("addressLine2", obj.getAddressLine2())
					.append("city", obj.getCity())
					.append("state", obj.getState())
					.append("country", obj.getCountry())
					.append("zipCode", obj.getZipCode())
					.append("countryCode", obj.getCountryCode())
					.append("licenseDate", obj.getLicenseDate())
					.append("effectiveDate", obj.getEffectiveDate())
					.append("expirationDate", obj.getExpirationDate())
					.append("status", obj.getStatus());
			return doc;
		}

	}
}
