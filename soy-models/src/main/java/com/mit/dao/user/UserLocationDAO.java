package com.mit.dao.user;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
import com.mit.entities.user.UserLocation;
import com.mit.utils.DateTimeUtils;
import com.mit.utils.JsonUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class UserLocationDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory
			.getLogger(UserInfoDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static UserLocationDAO _instance;

	private final String TABLE_NAME = "user_location";

	public static UserLocationDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new UserLocationDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private UserLocationDAO() {
	}
	
	public UserLocation getById(int id) {
		UserLocation userLoc = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					userLoc = new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return userLoc;
	}
	
	public List<UserLocation> getList(int from, int count) {
		List<UserLocation> userLocs = Collections.emptyList();
		if(dbSource != null) {
			try {
				Document objFinder = new Document("updateTime", new Document("$gt", DateTimeUtils.addDays(new Date(), -90)));
				Document sort = new Document("updateTime", -1);
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort).skip(from).limit(count);
				if (docs != null) {
					userLocs = new MongoMapper().parseList(docs);
				}
			} catch(final Exception e) {
				_logger.error("getList ", e);
			}
		}
		return userLocs;
	}
	
	public int updateLocation(UserLocation userLoc) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", userLoc.getId());
				
				Document obj = new Document("$set", new Document("location", new Document("type", "Point").append("coordinates", Arrays.asList(userLoc.getLat(), userLoc.getLon()))));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj, new UpdateOptions().upsert(true));
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("updateLocation ", e);
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

	private class MongoMapper extends MongoDBParse<UserLocation> {

		@Override
		public UserLocation parseObject(Document doc) {
			double lat = 0;
			double lon = 0;
			if (doc.get("location") instanceof Map) {
				Map<String, Object> location = (Map)doc.get("location");
				if (location.get("coordinates") instanceof List) {
					List<Double> ll = (List)location.get("coordinates");
					if (ll.size() == 2) {
						lat = ll.get(0);
						lon = ll.get(1);
					}
				}
			}
			
			UserLocation userLoc = new UserLocation(doc.getInteger("_id"), lat, lon);
			return userLoc;
		}

		@Override
		public Document toDocument(UserLocation obj) {
			Document doc = new Document("_id", obj.getId()).append("location", new Document("type", "Point").append("coordinates", Arrays.asList(obj.getLat(), obj.getLon())));
			return doc;
		}
	}
	
	public static void main(String[] args) {
		UserLocationDAO.getInstance().truncate();
		UserLocation userLoc = new UserLocation(1, 10.8679978, 106.6414322);
		UserLocationDAO.getInstance().updateLocation(userLoc);
		int from = 0;
		int count = 10;
		List<UserLocation> userLocs = UserLocationDAO.getInstance().getList(from, count);
		System.out.println(JsonUtils.Instance.toJson(userLocs));
	}
}
