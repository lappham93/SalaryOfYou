package com.mit.dao.address;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.entities.address.State;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.UpdateResult;

public class StateDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(StateDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static StateDAO _instance;

	private String TABLE_NAME = "state";

	public static StateDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new StateDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}
	
	public List<State> getListByCountry(String countryIsoCode) {
		List<State> countries = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("countryIsoCode", countryIsoCode).append("status", new Document("$gt", 0));
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
	
	public boolean hasState(String countryIsoCode) {
		boolean countries = false;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("countryIsoCode", countryIsoCode).append("status", new Document("$gt", 0));
				Document projection = new Document("_id", 1);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection).first();
				if(doc != null) {
					countries = doc.getString("_id") != null;
				}
			} catch(final Exception e) {
				_logger.error("hasState ", e);
			}
		}

		return countries;
	}
    
	public int insert(State country) {
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
	
	public int insertBatch(List<State> states) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				List<Document> objs = new LinkedList<Document>();
				for (State state: states) {
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

	public int update(State country) {
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

	private class MongoMapper extends MongoDBParse<State> {

		@Override
		public State parseObject(Document doc) {
			State account = new State(doc.getString("_id"), doc.getString("countryIsoCode"), 
					doc.getString("name"), doc.getInteger("status"));
			return account;
		}

		@Override
		public Document toDocument(State obj) {
			Document doc = new Document("_id", obj.getIsoCode())
					.append("countryIsoCode", obj.getCountryIsoCode())
					.append("name", obj.getName())
					.append("status", obj.getStatus());
			return doc;
		}

	}
}
