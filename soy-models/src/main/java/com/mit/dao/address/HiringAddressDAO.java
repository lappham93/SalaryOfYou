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
import com.mit.entities.address.HiringAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.UpdateResult;

public class HiringAddressDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(HiringAddressDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static HiringAddressDAO _instance;

	private String TABLE_NAME = "hiring_address";

	public static HiringAddressDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new HiringAddressDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}
	
	private HiringAddressDAO(){}
	
	public String getTableName() {
		return TABLE_NAME;
	}
	
	public HiringAddress getById(long id) {
		HiringAddress productOption = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
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
	
	public HiringAddress getByIdRef(String idRef) {
		HiringAddress productOption = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("idRef", idRef);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					productOption = new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getByIdRef ", e);
			}
		}

		return productOption;
	}
	
	public int insert(HiringAddress billingAddress) {
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

	public int update(HiringAddress billingAddress) {
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
	
	public void changeIsHiring(long id, boolean isHiring) {
		if (dbSource != null) {
			try {
				Document filter = new Document("_id", id);
				Document obj = new Document("$set", new Document("isHiring", isHiring));
				obj = new MongoMapper().buildUpdateTime(obj);
				dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
			} catch (final Exception e) {
				_logger.error("changeIsHiring ", e);
			}
		}
	}
	
	public List<HiringAddress> getListHiringAddress(List<Long> ids, int count, int from) {
		List<HiringAddress> rs = null;
		if (dbSource != null) {
			try {
				Document filter = new Document("_id", new Document("$in", ids))
						.append("isHiring", true);
				Document sort = new Document("updateTime", -1);
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort).skip(from).limit(count);
				if (docs != null) {
					rs = new MongoMapper().parseList(docs);
				}
			} catch (final Exception e) {
				_logger.error("getListHiringAddress ", e);
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

	private class MongoMapper extends MongoDBParse<HiringAddress> {

		@Override
		public HiringAddress parseObject(Document doc) {
			HiringAddress account = new HiringAddress(doc.getLong("_id"), doc.getString("idRef"), doc.getString("name"), doc.getString("address"), 
					doc.getString("city"), doc.getString("state"), doc.getString("country"), doc.getString("zipCode"), doc.getString("phone"), 
					doc.getDouble("lon"), doc.getDouble("lat"), doc.getBoolean("isVerified"), doc.getBoolean("isHiring"));
			return account;
		}
		@Override
		public Document toDocument(HiringAddress obj) {
			Document doc = new Document("_id", obj.getId())
					.append("idRef", obj.getIdRef())
					.append("name", obj.getName())
					.append("address", obj.getAddress())
					.append("city", obj.getCity())
					.append("state", obj.getState())
					.append("country", obj.getCountry())
					.append("zipCode", obj.getZipCode())
					.append("phone", obj.getPhone())
					.append("lon", obj.getLon())
					.append("lat", obj.getLat())
					.append("isVerified", obj.getIsVerified())
					.append("isHiring", obj.getIsHiring());
					
			return doc;
		}

	}
}
