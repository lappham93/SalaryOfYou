package com.mit.dao.address;


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
import com.mit.entities.address.BillingAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.UpdateResult;

public class BillingAddressDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(BillingAddressDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static BillingAddressDAO _instance;

	private String TABLE_NAME = "billing_address";

	public static BillingAddressDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new BillingAddressDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}
	
	public List<BillingAddress> getByUserId(int userId) {
		List<BillingAddress> userBillingAddresses = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("userId", userId).append("status", new Document("$gt", 0));
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if(doc != null) {
					userBillingAddresses = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getByUserId ", e);
			}
		}

		return userBillingAddresses;
	}

	public long getFirstAddressByUserId(int userId) {
		long addressId = 0;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("userId", userId).append("status", new Document("$gt", 0));
				Document projection = new Document("_id", 1);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection).first();
				if(doc != null) {
					addressId = doc.getLong("_id");
				}
			} catch(final Exception e) {
				_logger.error("getFirstAddressByUserId ", e);
			}
		}

		return addressId;
	}

	public BillingAddress getById(long id) {
		BillingAddress productOption = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", id).append("status", new Document("$gt", 0));
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					productOption = new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return productOption;
	}
	
	public int countAddress(int userId) {
		int count = 0;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("userId", userId).append("status", new Document("$gt", 0));
				count = (int)dbSource.getCollection(TABLE_NAME).count(objFinder);
			} catch(final Exception e) {
				_logger.error("countAddress ", e);
			}
		}

		return count;
	}
    
	public int insert(BillingAddress billingAddress) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				if(billingAddress.getId() <= 0) {
					billingAddress.setId(MIdGenLongDAO.getInstance(TABLE_NAME).getNext());
				}
				Document obj = mapper.toDocument(billingAddress);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}

	public int update(BillingAddress billingAddress) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", billingAddress.getId());
				Document obj = new Document("$set", mapper.toDocument(billingAddress));
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

	private class MongoMapper extends MongoDBParse<BillingAddress> {

		@Override
		public BillingAddress parseObject(Document doc) {
			BillingAddress account = new BillingAddress(doc.getLong("_id"), doc.getInteger("userId"),
					doc.getString("firstName"), doc.getString("lastName"), doc.getString("addressLine1"), 
					doc.getString("addressLine2"), doc.getString("city"), doc.getString("state"), 
					doc.getString("country"), doc.getString("zipCode"), doc.getString("phone"), doc.getString("countryCode"), 
					doc.getInteger("status"), doc.getDate("createTime").getTime(), doc.getDate("updateTime").getTime());
			return account;
		}

		@Override
		public Document toDocument(BillingAddress obj) {
			Document doc = new Document("_id", obj.getId())
					.append("userId", obj.getUserId())
					.append("firstName", obj.getFirstName())
					.append("lastName", obj.getLastName())
					.append("addressLine1", obj.getAddressLine1())
					.append("addressLine2", obj.getAddressLine2())
					.append("city", obj.getCity())
					.append("state", obj.getState())
					.append("country", obj.getCountry())
					.append("zipCode", obj.getZipCode())
					.append("phone", obj.getPhone())
					.append("countryCode", obj.getCountryCode())
					.append("status", obj.getStatus());
			return doc;
		}

	}
}
