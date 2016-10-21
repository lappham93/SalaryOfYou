package com.mit.dao.zipcode;

import java.util.ArrayList;
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
import com.mit.dao.address.CountryDAO;
import com.mit.dao.mid.MIdGenDAO;
import com.mit.entities.zipcode.CountryZipCode;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class CountryZipCodeDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(CountryDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static CountryZipCodeDAO _instance;

	private String TABLE_NAME = "country_zipcode";

	public static CountryZipCodeDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new CountryZipCodeDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}
	
	public List<CountryZipCode> getAll() {
		List<CountryZipCode> countryZipcodes = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("status", new Document("$gt", 0));
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if(doc != null) {
					countryZipcodes = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getList ", e);
			}
		}

		return countryZipcodes;
	}
	
	public List<CountryZipCode> getListOfCountry(String countryCode) {
		List<CountryZipCode> countryZCs = null;
		if(dbSource != null && countryCode != null) {
			try {
				Document objFinder = new Document("countryCode", countryCode).append("status", new Document("$gt", 0));
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if(doc != null) {
					countryZCs = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getList ", e);
			}
		}
		return countryZCs;
	}
	
	public List<String> getListZipCode(String countryCode) {
		List<CountryZipCode> countryZCs = getListOfCountry(countryCode);
		if (countryZCs == null || countryZCs.isEmpty()) {
			return null;
		}
		List<String> zipCodes = new ArrayList<>();
		for (CountryZipCode czc : countryZCs) {
			zipCodes.add(czc.getZipCode().toUpperCase());
		}
		return zipCodes;
	}
	
	public boolean validateZC(String countryCode, String zipcode, boolean prefix) {
		if (dbSource != null) {
			try {
				Document objFinder = new Document("countryCode", countryCode.toUpperCase()).append("status", new Document("$gt", 0));
				Document tmp = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (tmp == null) {
					return true;
				}
				if (prefix) {
					int len = tmp.getString("zipCode").length();
					if (zipcode == null || zipcode.length() < len) {
						return false;
					}
					zipcode = zipcode.substring(0, len);
				}
				objFinder.append("zipCode", zipcode.toUpperCase());
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				return doc != null;
			} catch(final Exception e) {
				_logger.error("getList ", e);
			}
		}
		return false;
	}
	
	public int insert(CountryZipCode countryZC) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document obj = mapper.toDocument(countryZC);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}
	
	public int insertBatch(List<CountryZipCode> countryZCs) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				List<Document> objs = new LinkedList<Document>();
				for (CountryZipCode countryZC: countryZCs) {
					Document obj = mapper.toDocument(countryZC);
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

	public int update(CountryZipCode countryZC) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", countryZC.getId());
				Document obj = new Document("$set", mapper.toDocument(countryZC));
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
				
				MIdGenDAO.getInstance(TABLE_NAME).reset();
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

	private class MongoMapper extends MongoDBParse<CountryZipCode> {

		@Override
		public CountryZipCode parseObject(Document doc) {
			CountryZipCode account = new CountryZipCode(doc.getLong("_id"), doc.getString("countryCode"), doc.getString("zipCode"), doc.getInteger("status"), doc.getInteger("type"));
			return account;
		}

		@Override
		public Document toDocument(CountryZipCode obj) {
			Document doc = new Document("_id", obj.getId())
					.append("countryCode", obj.getCountryCode())
					.append("zipCode", obj.getZipCode())
					.append("status", obj.getStatus())
					.append("type", obj.getType());
			return doc;
		}

	}
	
	public static void main(String[] args) {
		List<String> zcs = CountryZipCodeDAO.getInstance().getListZipCode("US");
		System.out.println(zcs.size());
		//System.out.println(JsonUtils.Instance.toJson(CountryZipCodeDAO.getInstance().getListOfCountry("US").size()));
	}
}
