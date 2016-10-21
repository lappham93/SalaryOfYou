package com.mit.dao.address;


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
import com.mit.entities.address.City;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;

public class CityDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(CityDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static CityDAO _instance;

	private String TABLE_NAME = "city";

	public static CityDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new CityDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}
	
	public List<City> getList() {
		List<City> countries = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("status", new Document("$gt", 0));
				Document sort = new Document("name", 1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort);
				if(doc != null) {
					countries = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getList ", e);
			}
		}

		return countries;
	}
	
	public List<City> getListByState(String stateName, int from, int count) {
		List<City> countries = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("stateName", new Document("$regex", "^" + stateName + "$").append("$options", "im"))
						.append("status", new Document("$gt", 0));
				Document sort = new Document("name", 1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort).skip(from).limit(count);
				if(doc != null) {
					countries = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getListByState ", e);
			}
		}

		return countries;
	}
	
	public List<City> getListByState(String stateName) {
		List<City> countries = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("stateName", new Document("$regex", "^" + stateName + "$").append("$options", "im"))
						.append("status", new Document("$gt", 0));
				Document sort = new Document("name", 1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort);
				if(doc != null) {
					countries = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getListByState ", e);
			}
		}

		return countries;
	}
	
	public List<String> getSuggest(String name, String countryCode, int count) {
		List<String> words = new ArrayList<String>();
		if(dbSource != null) {
			try {
				MongoCursor<Document> save = null;
				Document filter = new Document("stateName", new Document("$regex", name).append("$options", "i"))
						.append("status", new Document("$gt", 0));
				if (countryCode != null && !countryCode.isEmpty()) {
					filter.append("countryIsoCode", countryCode);
				}
				Document sort = new Document("name", 1);
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort).limit(count);
				if(docs != null) {
					save = docs.iterator();
					while (save.hasNext()) {
						Document doc = save.next();
						String key = doc.getString("stateName") + ", " + countryCode;
						if (!words.contains(key)) {
							words.add(key);
						}
					}
				}
				if (words.size() < count) {
					// search accord city
					filter = new Document("name", new Document("$regex", name).append("$options", "i"))
							.append("status", new Document("$gt", 0));
					if (countryCode != null && !countryCode.isEmpty()) {
						filter.append("countryIsoCode", countryCode);
					}
					sort = new Document("name", 1);
					docs = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort).limit(count);
					if(docs != null) {
						MongoCursor<Document> tmp = docs.iterator();
						while (words.size() < count && tmp.hasNext()) {
							Document doc = tmp.next();
							String key = doc.getString("name") + ", " + doc.getString("stateName") + ", " + countryCode;
							if (!words.contains(key)) {
								words.add(key);
							}
						}
					}
				}
				if (words.size() < count && save != null) {
					while (words.size() < count && save.hasNext()) {
						Document doc = save.next();
						String key = doc.getString("name") + ", " + doc.getString("stateName") + ", " + countryCode;
						if (!words.contains(key)) {
							words.add(key);
							words.add(key);
						}
					}
				}
			} catch(final Exception e) {
				_logger.error("getSuggest ", e);
			}
		}

		return words;
	}
	
	public int insert(City country) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document obj = mapper.toDocument(country);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}
	
	public int insertBatch(List<City> states) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				List<Document> objs = new LinkedList<Document>();
				for (City state: states) {
					Document obj = mapper.toDocument(state);
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

	public int update(City country) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", country.getIsoCode());
				Document obj = new Document("$set", mapper.toDocument(country));
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
	
	public void createIndex() {
		if (dbSource != null) {
			try {
				dbSource.getCollection(TABLE_NAME).createIndex(new Document("name", 1).append("stateName", 1));
			} catch (Exception e) {
				_logger.error("createIndex ", e);
			}
		}
	}

	private class MongoMapper extends MongoDBParse<City> {

		@Override
		public City parseObject(Document doc) {
			City account = new City(doc.getString("_id"), doc.getString("countryIsoCode"), doc.getString("stateIsoCode"), doc.getString("stateName"),
					doc.getString("name"), doc.getInteger("status"));
			return account;
		}

		@Override
		public Document toDocument(City obj) {
			Document doc = new Document("_id", obj.getIsoCode())
					.append("countryIsoCode", obj.getCountryIsoCode())
					.append("stateIsoCode", obj.getStateIsoCode())
					.append("stateName", obj.getStateName())
					.append("name", obj.getName())
					.append("status", obj.getStatus());
			return doc;
		}

	}
}
