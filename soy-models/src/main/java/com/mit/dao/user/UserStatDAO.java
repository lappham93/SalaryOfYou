package com.mit.dao.user;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.entities.user.UserStat;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class UserStatDAO extends CommonDAO {

	private final Logger _logger = LoggerFactory.getLogger(UserStatDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static UserStatDAO _instance;

	private String TABLE_NAME = "user_stat";

	public static UserStatDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new UserStatDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private UserStatDAO() {
	}

	public String getTableName() {
		return this.TABLE_NAME;
	}
	
	public List<Long> getList(long userId, int listType) {
		List<Long> rs = Collections.emptyList();
		if (dbSource != null) {
			try {
				Document filter = new Document("userId", userId);
				Document doc = dbSource.getCollection(TABLE_NAME).find(filter).first();
				if (doc != null) {
					rs = (List<Long>) doc.get(getListName(listType));
				}
			} catch (Exception e) {
				_logger.error("getList ", e);
			}
		}
		return rs;
	}
	
	public int getTotal(long userId, int listType) {
		return getList(userId, listType).size();
	}

	public UserStat getById(long userId) {
		UserStat unfollowUser = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("userId", userId);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					unfollowUser = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return unfollowUser;
	}
	
	public int addToList(long userId, int listType, long objectId) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				String field = getListName(listType);
				Document objFinder = new Document("userId", userId);
				Document delete = new Document("$pull", new Document(getListName(listType), objectId));
				dbSource.getCollection(TABLE_NAME).updateOne(objFinder, delete);
				Document update = new Document("$push", new Document(field, new Document("$each", Arrays.asList(objectId))
					.append("$position", 0)));
				UpdateResult doc = dbSource.getCollection(TABLE_NAME).updateOne(objFinder, update, new UpdateOptions().upsert(true));

				rs = (int)doc.getModifiedCount() + (doc.getUpsertedId() != null ? 1 : 0);
			} catch(final Exception e) {
				_logger.error("addToList ", e);
			}
		}
		return rs;	
	}
	
	public int removeFromList(long userId, int listType, long objectId) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("userId", userId);
				Document update = new Document("$pull", new Document(getListName(listType), objectId));
				UpdateResult doc = dbSource.getCollection(TABLE_NAME).updateOne(objFinder, update);

				rs = (int)doc.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("removeFromList ", e);
			}
		}
		return rs;
	}
	
	public int clearList(long userId, int listType) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("userId", userId);
				Document update = new Document("$set", new Document(getListName(listType), null));
				UpdateResult doc = dbSource.getCollection(TABLE_NAME).updateOne(objFinder, update);

				rs = (int)doc.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("clearList ", e);
			}
		}
		return rs;
	}
	
	public int insert(UserStat msg) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document obj = mapper.toDocument(msg);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch (final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}

	public int update(UserStat msg) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("userId", msg.getUserId());
				Document obj = new Document("$set", mapper.toDocument(msg));
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

	public int hardDelete(long userId) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document("userId", userId);
				DeleteResult qRs = dbSource.getCollection(TABLE_NAME).deleteOne(filter);
				rs = (int) qRs.getDeletedCount();
			} catch (final Exception e) {
				_logger.error("hardDelete ", e);
			}
		}

		return rs;
	}

	private class MongoMapper extends MongoDBParse<UserStat> {

		@Override
		public UserStat parseObject(Document doc) {
			UserStat unfollowUser = new UserStat(doc.getLong("userId"), (List<Long>) doc.get("hffuIds"),
					(List<Long>) doc.get("followUserIds"), (List<Long>) doc.get("userFollowIds"), (List<Long>) doc.get("hftuIds"), 
					(List<Long>) doc.get("feedHides"), (List<Long>) doc.get("eventIds"), (List<Long>) doc.get("boothIds"), (List<Long>) doc.get("productIds"));
			return unfollowUser;
		}

		@Override
		public Document toDocument(UserStat obj) {
			Document doc = new Document("userId", obj.getUserId())
					.append("hffuIds", obj.getHffuIds())
					.append("followUserIds", obj.getFollowUserIds())
					.append("userFollowIds", obj.getUserFollowIds())
					.append("hftuIds", obj.getHftuIds())
					.append("feedHides", obj.getFeedHides())
					.append("eventIds", obj.getEventIds())
					.append("boothIds", obj.getBoothIds())
					.append("productIds", obj.getProductIds());
			return doc;
		}

	}
	
	private String getListName(int listType) {
		String field = "";
		if (listType == UserStat.ListType.FOLLOWER.getValue()) {
			field = "userFollowIds";
		} else if (listType == UserStat.ListType.FOLLOWING.getValue()) {
			field = "followUserIds";
		} else if (listType == UserStat.ListType.HFFU.getValue()) {
			field = "hffuIds";
		} else if (listType == UserStat.ListType.HFTU.getValue()) {
			field = "hftuIds";
		} else if (listType == UserStat.ListType.EVENT_FAVORITE.getValue()) {
			field = "eventIds";
		} else if (listType == UserStat.ListType.BOOTH_FAVORITE.getValue()) {
			field = "boothIds";
		} else if (listType == UserStat.ListType.PRODUCT_FAVORITE.getValue()) {
			field = "productIds";
		} else if (listType == UserStat.ListType.HIDDEN_FEED.getValue()) {
			field = "feedHides";
		}
		
		return field;
	}
}
