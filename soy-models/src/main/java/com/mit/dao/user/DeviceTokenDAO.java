package com.mit.dao.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.entities.user.DeviceToken;
import com.mit.utils.JsonUtils;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class DeviceTokenDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(DeviceTokenDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static DeviceTokenDAO _instance;

	private final String TABLE_NAME = "account_device_pns";

	public static DeviceTokenDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new DeviceTokenDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private DeviceTokenDAO() {}

	public List<DeviceToken> getByUserAndDevice(int userId, int appId, int deviceId) {
		List<DeviceToken> deviceToken = new LinkedList<DeviceToken>();
		if(dbSource != null) {
			try {
//				Document objFinder = new Document("_id", userId).append("tokens", new Document("$elemMatch", new Document("deviceId", deviceId).append("appId", appId)));
//				Document projection = new Document("tokens.$", 1);
//				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection);
				Document match = new Document("_id", userId).append("tokens", new Document("$elemMatch", new Document("deviceId", deviceId).append("appId", appId)));
				Document redact = new Document("$cond", new Document("if", new Document("$eq", Arrays.asList("$tokens.appId", appId))).append("then", "$$DESCEND").append("else", "$$PRUNE"));
				Document project = new Document("tokens", new Document("$setDifference", 
						Arrays.asList(new Document("$map", new Document("input", "$tokens")
						.append("as", "el").append("in", new Document("$cond", 
								new Document("if", new Document("$and", Arrays.asList(new Document("$eq", Arrays.asList("$$el.deviceId", deviceId)), 
										new Document("$eq", Arrays.asList("$$el.appId", appId))))).append("then", "$$el")
										.append("else", false)))), Arrays.asList(false))));
				
				List<Document> pipeline = new ArrayList<Document>(2);
				pipeline.add(new Document("$match", match));
//				pipeline.add(new Document("$redact", redact));
				pipeline.add(new Document("$project", project));
	
				AggregateIterable<Document> docs = dbSource.getCollection(TABLE_NAME).aggregate(pipeline);
				if(docs != null) {
					Document doc = null;
					MongoCursor<Document> cursor = docs.iterator();
					while(cursor != null && cursor.hasNext() && (doc = cursor.next()) != null) {
						int id = doc.getInteger("_id");
						if (doc.get("tokens") != null) {
							List<Document> tokens = doc.get("tokens", List.class);
							
							for (Document token: tokens) {
								deviceToken.add(new DeviceToken(id, token.getInteger("appId"), token.getInteger("deviceId"), token.getString("imei"), token.getString("deviceToken")));
							}
						}
					}
				}
			} catch(final Exception e) {
				_logger.error("getByUserAndDevice ", e);
			}
		}
		return deviceToken;
	}
	
	public List<DeviceToken> getByUserListAndDevice(List<Integer> userIds, int appId, int deviceId) {
		List<DeviceToken> deviceToken = new LinkedList<DeviceToken>();
		if(dbSource != null) {
			try {
//				Document objFinder = new Document("_id", userId).append("tokens", new Document("$elemMatch", new Document("deviceId", deviceId).append("appId", appId)));
//				Document projection = new Document("tokens.$", 1);
//				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection);
				Document match = new Document("_id", new Document("$in", userIds)).append("tokens", new Document("$elemMatch", new Document("deviceId", deviceId).append("appId", appId)));
				Document redact = new Document("$cond", new Document("if", new Document("$eq", Arrays.asList("$tokens.appId", appId))).append("then", "$$DESCEND").append("else", "$$PRUNE"));
				Document project = new Document("tokens", new Document("$setDifference", 
						Arrays.asList(new Document("$map", new Document("input", "$tokens")
						.append("as", "el").append("in", new Document("$cond", 
								new Document("if", new Document("$and", Arrays.asList(new Document("$eq", Arrays.asList("$$el.deviceId", deviceId)), 
										new Document("$eq", Arrays.asList("$$el.appId", appId))))).append("then", "$$el")
										.append("else", false)))), Arrays.asList(false))));
				
				List<Document> pipeline = new ArrayList<Document>(2);
				pipeline.add(new Document("$match", match));
//				pipeline.add(new Document("$redact", redact));
				pipeline.add(new Document("$project", project));
	
				AggregateIterable<Document> docs = dbSource.getCollection(TABLE_NAME).aggregate(pipeline);
				if(docs != null) {
					Document doc = null;
					MongoCursor<Document> cursor = docs.iterator();
					while(cursor != null && cursor.hasNext() && (doc = cursor.next()) != null) {
						int id = doc.getInteger("_id");
						if (doc.get("tokens") != null) {
							List<Document> tokens = doc.get("tokens", List.class);
							
							for (Document token: tokens) {
								deviceToken.add(new DeviceToken(id, token.getInteger("appId"), token.getInteger("deviceId"), token.getString("imei"), token.getString("deviceToken")));
							}
						}
					}
				}
			} catch(final Exception e) {
				_logger.error("getByUserListAndDevice ", e);
			}
		}
		return deviceToken;
	}

//	public List<DeviceToken> getByListUserId(List<Integer> uids) {
//		List<DeviceToken> deviceToken = null;
//		if(dbSource != null) {
//			try {
//				Document objFinder = new Document("_id", new Document("$in", uids));
//				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
//				if(doc != null) {
//					deviceToken = new MongoMapper().parseList(doc);
//				}
//			} catch(final Exception e) {
//				_logger.error("getByListUserId ", e);
//			}
//		}
//		return deviceToken;
//	}

//	public List<DeviceToken> getByListUserIdAndDevice(List<Integer> uids, int deviceId) {
//		List<DeviceToken> deviceToken = null;
//		if(dbSource != null) {
//			try {
//				Document objFinder = new Document("deviceId", deviceId).append("_id", new Document("$in", uids));
//				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
//				if(doc != null) {
//					deviceToken = new MongoMapper().parseList(doc);
//				}
//			} catch(final Exception e) {
//				_logger.error("getByListUserIdAndDevice ", e);
//			}
//		}
//		return deviceToken;
//	}

//	public List<DeviceToken> getByDevice(int deviceId) {
//		List<DeviceToken> deviceToken = null;
//		if(dbSource != null) {
//			try {
//				Document objFinder = new Document("deviceId", deviceId);
//				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
//				if(doc != null) {
//					deviceToken = new MongoMapper().parseList(doc);
//				}
//			} catch(final Exception e) {
//				_logger.error("getByDevice ", e);
//			}
//		}
//		return deviceToken;
//	}

//	public int insert(DeviceToken token) {
//		int rs = MongoErrorCode.NOT_CONNECT;
//		if(dbSource != null) {
//			try {
//				MongoMapper mapper = new MongoMapper();
//				Document obj = mapper.toDocument(token);
//				obj = mapper.buildInsertTime(obj);
//				dbSource.getCollection(TABLE_NAME).insertOne(obj);
//				rs = MongoErrorCode.SUCCESS;
//			} catch(final Exception e) {
//				_logger.error("insert ", e);
//			}
//		}
//
//		return rs;
//	}

//	public int update(DeviceToken token) {
//		int rs = MongoErrorCode.NOT_CONNECT;
//		if(dbSource != null) {
//			try {
//				Document filter = new Document("_id", token.getAccountId());
//				Document obj = new Document("$set", new Document("deviceToken", token.getDeviceToken()).append("deviceId", token.getDeviceId()).append("imei", token.getImei()));
//				UpdateResult updateRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj, new UpdateOptions().upsert(true));
//				rs = (int)updateRs.getModifiedCount();// MongoErrorCode.SUCCESS;
//			} catch(Exception e) {
//				_logger.error("update ", e);
//			}
//		}
//
//		return rs;
//	}
	
	public int update(DeviceToken token) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document("_id", token.getAccountId()).append("tokens", new Document("$elemMatch", new Document("imei", token.getImei()).append("appId", token.getAppId())));
				Document obj = new Document("$set", new Document("tokens.$.deviceToken", token.getDeviceToken()));
				Document projection = new Document("tokens", new Document("$elemMatch", new Document("imei", token.getImei()).append("appId", token.getAppId())));
				Document qRs = dbSource.getCollection(TABLE_NAME).findOneAndUpdate(filter, obj, new FindOneAndUpdateOptions().projection(projection));
				if (qRs == null) {
					filter = new Document("_id", token.getAccountId());
					obj = new Document("$push", new Document("tokens", new Document("imei", token.getImei())
						.append("appId", token.getAppId())
						.append("deviceId", token.getDeviceId())
						.append("deviceToken", token.getDeviceToken())));
					UpdateResult updateResult = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj, new UpdateOptions().upsert(true));
					rs = (int)updateResult.getModifiedCount();
				} else {
					rs = 1;
				}
			} catch (final Exception e) {
				_logger.error("upsert ", e);
			}
		}

		return rs;
	}

	public int delete(int userId, int appId, String imei) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", userId);
				Document update = new Document("$pull", new Document("tokens", new Document("appId", appId).append("imei", imei)));
				UpdateResult doc = dbSource.getCollection(TABLE_NAME).updateOne(objFinder, update);
				rs = (int)doc.getModifiedCount();
			} catch(Exception e) {
				_logger.error("delete ", e);
			}
		}

		return rs;
	}
	
	public int deleteByToken(String token) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document objFinder = new Document();
				Document update = new Document("$pull", new Document("tokens", new Document("deviceToken", token)));
				UpdateResult doc = dbSource.getCollection(TABLE_NAME).updateOne(objFinder, update);
				rs = (int)doc.getModifiedCount();
			} catch(Exception e) {
				_logger.error("deleteByToken ", e);
			}
		}

		return rs;
	}

//	public int deleteByToken(String token) {
//		int rs = MongoErrorCode.NOT_CONNECT;
//		if(dbSource != null) {
//			try {
//				Document filter = new Document("deviceId", token);
//				DeleteResult updateRs = dbSource.getCollection(TABLE_NAME).deleteOne(filter);
//				rs = (int)updateRs.getDeletedCount();// MongoErrorCode.SUCCESS;
//			} catch(Exception e) {
//				_logger.error("delete ", e);
//			}
//		}
//
//		return rs;
//	}
	
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

//	private class MongoMapper extends MongoDBParse<DeviceToken> {
//
//		@Override
//		public DeviceToken parseObject(Document doc) {
//			DeviceToken deviceToken = new DeviceToken(doc.getInteger("_id"), 
//					doc.getInteger("appId"), 
//					doc.getInteger("deviceId"), 
//					doc.getString ("imei"), 
//					doc.getString("deviceToken"), 
//					doc.getDate("updateTime").getTime());
//			return deviceToken;
//		}
//
//		@Override
//		public Document toDocument(DeviceToken obj) {
//			Document doc = new Document("_id", obj.getAccountId())
//				.append("appId", obj.getAppId())
//				.append("deviceId", obj.getDeviceId())
//				.append("imei", obj.getImei())
//				.append("deviceToken", obj.getDeviceToken());
//			return doc;
//		}
//
//	}
	public static void main(String[] args) {
		DeviceToken token = new DeviceToken(1, 2, 3, "imeiabc2", "token 353");
		int rs = DeviceTokenDAO.getInstance().update(token);
		System.out.println(JsonUtils.Instance.toJson(DeviceTokenDAO.getInstance().getByUserAndDevice(1, 2, 3)));
	}
}
