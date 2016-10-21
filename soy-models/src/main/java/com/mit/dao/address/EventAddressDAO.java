package com.mit.dao.address;

import java.util.ArrayList;
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
import com.mit.entities.address.EventAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;

public class EventAddressDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(EventAddressDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static EventAddressDAO _instance;

	private String TABLE_NAME = "event_address";

	public static EventAddressDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new EventAddressDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	public EventAddress getById(long id) {
		EventAddress productOption = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					productOption = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return productOption;
	}
	
	public int totalAll() {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document();
				rs = (int)dbSource.getCollection(TABLE_NAME).count(filter);
			} catch (Exception e) {
				_logger.error("totalAll ", e);
			}
		}
		return rs;
	}
	
	public List<EventAddress> getList() {
		List<EventAddress> rs = null;
		if (dbSource != null) {
			try {
				Document filter = new Document("status", new Document("$gt", 0));
				Document sort = new Document("name", 1);
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort);
				if (docs != null) {
					rs = new MongoMapper().parseList(docs);
				}
			} catch (Exception e) {
				_logger.error("getList ", e);
			}
		}
		
		return rs;
	}
	
	public List<EventAddress> getSlice(int from, int count, String fieldSort, boolean isAsc) {
		List<EventAddress> rs = null;
		if (dbSource != null) {
			try {
				Document filter = new Document();
				Document sort = new Document(fieldSort, isAsc ? 1 : -1);
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort).skip(from).limit(count);
				if (docs != null) {
					rs = new MongoMapper().parseList(docs);
				}
			} catch (Exception e) {
				_logger.error("getSlice ", e);
			}
		}
		
		return rs;
	}
	
	public List<Long> getListId(String country, String state, String city, boolean isAndCondition) {
		List<Long> rs = new ArrayList<Long>();
		if (dbSource != null) {
			try {
				Document objFinder = new Document("status", new Document("$gt", 0));
				List<Document> constraint = new ArrayList<>();
				if (country != null && !country.isEmpty()) {
					constraint.add(new Document("countryCode", new Document("$regex", "^" + country + "$").append("$options", "im")));
				}
				if (state != null && !state.isEmpty()) {
					constraint.add(new Document("state", new Document("$regex", "^" + state + "$").append("$options", "im")));
				}
				if (city != null && !city.isEmpty()) {
					constraint.add(new Document("city", new Document("$regex", "^" + city + "$").append("$options", "im")));
				}
				if (!constraint.isEmpty()) {
					if (isAndCondition) {
						objFinder.append("$and", constraint);
					} else {
						objFinder.append("$or", constraint);
					}
				}
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if (docs != null) {
					MongoCursor<Document> tmp = docs.iterator();
					while (tmp.hasNext()) {
						rs.add(tmp.next().getLong("_id"));
					}
				}
			} catch (final Exception e) {
				_logger.error("getListId ", e);
			}
		}

		return rs;
	}

	public int insert(EventAddress billingAddress) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				if (billingAddress.getId() <= 0) {
					billingAddress.setId(MIdGenLongDAO.getInstance(TABLE_NAME).getNext());
				}
				Document obj = mapper.toDocument(billingAddress);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch (final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}

	public int update(EventAddress billingAddress) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", billingAddress.getId());
				Document obj = new Document("$set", mapper.toDocument(billingAddress));
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

	private class MongoMapper extends MongoDBParse<EventAddress> {

		@Override
		public EventAddress parseObject(Document doc) {
			EventAddress account = new EventAddress(doc.getLong("_id"), doc.getString("name"),
					doc.getString("addressLine1"), doc.getString("addressLine2"), doc.getString("city"),
					doc.getString("state"), doc.getString("country"), doc.getString("zipCode"), doc.getString("phone"),
					doc.getString("countryCode"), doc.getDouble("lon"), doc.getDouble("lat"), doc.getInteger("status"),
					doc.getDate("createTime").getTime(), doc.getDate("updateTime").getTime());
			return account;
		}

		@Override
		public Document toDocument(EventAddress obj) {
			Document doc = new Document("_id", obj.getId())
					.append("name", obj.getName())
					.append("addressLine1", obj.getAddressLine1())
					.append("addressLine2", obj.getAddressLine2())
					.append("city", obj.getCity())
					.append("state", obj.getState())
					.append("country", obj.getCountry())
					.append("zipCode", obj.getZipCode())
					.append("phone", obj.getPhone())
					.append("countryCode", obj.getCountryCode())
					.append("lon", obj.getLon())
					.append("lat", obj.getLat())
					.append("status", obj.getStatus());
			
			return doc;
		}

	}
}
