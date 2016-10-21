package com.mit.dao.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.math.NumberUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.api.ApiError;
import com.mit.api.ApiMessage;
import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.dao.mid.MIdGenLongDAO;
import com.mit.entities.chat.LockUserRoom;
import com.mit.entities.user.UserBanning;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

public class UserBanningDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(UserBanningDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static UserBanningDAO _instance;

	private String TABLE_NAME = "user_banning";

	public static UserBanningDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new UserBanningDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private UserBanningDAO() {
	}

	public String getTableName() {
		return TABLE_NAME;
	}

	// public boolean isBanInRoom(int userId, int roomId) {
	// boolean rs = false;
	// if (dbSource != null) {
	// try {
	// Document objFinder = new Document("_id", userId).append("Items.roomId",
	// roomId);
	// Document doc =
	// dbSource.getCollection(TABLE_NAME).find(objFinder).first();
	// if (doc != null) {
	// List<UserBanning> ubs = new
	// MongoMapper().parseList((List)doc.get("Items"));
	// for (UserBanning ub : ubs) {
	// if (ub instanceof LockUserRoom) {
	// LockUserRoom lur = (LockUserRoom) ub;
	// if (lur.getRoomId() == roomId && lur.getStatus() > 0) {
	// rs = true;
	// break;
	// }
	// }
	// }
	// }
	// } catch (final Exception e) {
	// _logger.error("getByUserId ", e);
	// }
	// }
	// return rs;
	// }

	public boolean isBanInRoom(int userId, int roomId) {
		boolean rs = false;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", userId).append("Items", new Document("$elemMatch",
						new Document("roomId", roomId).append("status", new Document("$gt", 0))));
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					rs = true;
				}
			} catch (final Exception e) {
				_logger.error("isBanInRoom ", e);
			}
		}
		return rs;
	}

	public List<UserBanning> getByUserId(int userId) {
		List<UserBanning> ubs = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", userId);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					ubs = new MongoMapper().parseList((List) doc.get("Items"));
				}
			} catch (final Exception e) {
				_logger.error("getByUserId ", e);
			}
		}
		return ubs;
	}

	public List<List<UserBanning>> getWithPaging(int count, int from) {
		List<List<UserBanning>> trus = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("count", new Document("$gt", 0));
				Document sort = new Document("updateTime", -1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort).skip(from)
						.limit(count);
				if (doc != null) {
					trus = new ArrayList<>();
					MongoCursor<Document> docIter = doc.iterator();
					while (docIter.hasNext()) {
						trus.add(new MongoMapper().parseList((List) docIter.next().get("Items")));
					}
				}
			} catch (final Exception e) {
				_logger.error("getByUserId ", e);
			}
		}
		return trus;
	}

	public List<List<UserBanning>> getWithPaging(int type, int count, int from) {
		List<List<UserBanning>> trus = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("Items", new Document("$elemMatch",
						new Document("type", type).append("status", new Document("$gt", 0))));
				Document sort = new Document("updateTime", -1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort).skip(from)
						.limit(count);
				if (doc != null) {
					trus = new ArrayList<>();
					MongoCursor<Document> docIter = doc.iterator();
					while (docIter.hasNext()) {
						trus.add(new MongoMapper().parseList((List) docIter.next().get("Items")));
					}
				}
			} catch (final Exception e) {
				_logger.error("getWithPaging ", e);
			}
		}
		return trus;
	}

	public int countUser() {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document("count", new Document("$gt", 0));
				rs = (int) dbSource.getCollection(TABLE_NAME).count(filter);
			} catch (final Exception e) {
				_logger.debug("countUser ", e);
			}
		}

		return rs;
	}

	public int getCount(int userId) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document("_id", userId);
				Document doc = dbSource.getCollection(TABLE_NAME).find(filter).first();
				if (doc != null) {
					rs = doc.getInteger("count");
				}
			} catch (final Exception e) {
				_logger.debug("getCount ", e);
			}
		}

		return rs;
	}

	public int upsert(UserBanning msg) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				if (msg.getId() <= 0) {
					msg.setId(MIdGenLongDAO.getInstance(TABLE_NAME).getNext());
				}
				Document obj = mapper.toDocument(msg);
				obj = mapper.buildInsertTime(obj);
				Document filter = new Document("_id", msg.getUserId());
				Document update = new Document("$push",
						new Document("Items", new Document("$each", Arrays.asList(obj)).append("$position", 0)))
								.append("$inc", new Document("count", 1))
								.append("$set", new Document("updateTime", new Date().getTime()));
				UpdateResult ur = dbSource.getCollection(TABLE_NAME).updateOne(filter, update,
						new UpdateOptions().upsert(true));
				rs = (int) ur.getModifiedCount();
			} catch (final Exception e) {
				_logger.error("upsert ", e);
			}
		}

		return rs;
	}

	public int deleteBanningItem(long banningId) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("Items._id", banningId);
				Document obj = new Document("$set", new Document("Items.$.status", 0));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
				if (rs > 0) {
					obj = new Document("$inc", new Document("count", -rs));
					dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				}
			} catch (final Exception e) {
				_logger.error("deleteBanningItem ", e);
			}
		}

		return rs;
	}

	public int hardDeleteBanningItems(long banningId) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document("Items._id", banningId);
				Document update = new Document("$pull", new Document("Items", new Document("_id", banningId)));
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, update);
				rs = (int) qRs.getModifiedCount();
				if (rs > 0) {
					update = new Document("$inc", new Document("count", -rs));
					dbSource.getCollection(TABLE_NAME).updateOne(filter, update);
				}
			} catch (final Exception e) {
				_logger.error("hardDeleteBanningItem ", e);
			}
		}

		return rs;
	}

	public int hardDeleteBanningItemsOfUser(int userId, List<Long> banningIds) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				if (banningIds == null) {
					banningIds = Arrays.asList();
				}
				Document filter = new Document("_id", userId);
				Document update = new Document("$pull",
						new Document("Items", new Document("_id", new Document("$in", banningIds))));
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, update);
				rs = (int) qRs.getModifiedCount();
				if (rs > 0) {
					Document doc = dbSource.getCollection(TABLE_NAME).find(filter).first();
					if (doc != null) {
						update = new Document("$set", new Document("count", ((List) doc.get("Items")).size()));
						dbSource.getCollection(TABLE_NAME).updateOne(filter, update);
					}
				}
			} catch (final Exception e) {
				_logger.error("hardDeleteBanningItem ", e);
			}
		}

		return rs;
	}

	private class MongoMapper extends MongoDBParse<UserBanning> {

		@Override
		public UserBanning parseObject(Document doc) {
			int type = doc.getInteger("type");
			UserBanning ub = null;
			if (type == UserBanning.BanningType.CHAT.getValue()) {
				ub = new LockUserRoom(doc.getLong("_id"), doc.getInteger("roomId"), doc.getInteger("userId"),
						doc.getDate("createTime").getTime(), doc.getLong("timeDuration"), doc.getInteger("status"));
			} else {
				ub = new UserBanning(doc.getLong("_id"), doc.getInteger("userId"), type, doc.getInteger("status"),
						doc.getDate("createTime").getTime());
			}
			return ub;
		}

		@Override
		public Document toDocument(UserBanning obj) {
			Document doc = new Document("_id", obj.getId()).append("userId", obj.getUserId())
					.append("type", obj.getType()).append("status", obj.getStatus());
			if (obj instanceof LockUserRoom) {
				LockUserRoom roomBan = (LockUserRoom) obj;
				doc.append("roomId", roomBan.getRoomId()).append("timeDuration", roomBan.getTimeDuration());
			}
			return doc;
		}

	}

	public static void main(String[] args) {
		ApiMessage msg = new ApiMessage();
		int count = 10;
		Map<String, Object> rs = new HashMap<String, Object>();
		List<List<UserBanning>> ubs = UserBanningDAO.getInstance().getWithPaging(UserBanning.BanningType.CHAT.getValue(), count + 1, 0);
		boolean hasMore = false;
		if (ubs != null && ubs.size() > count) {
			hasMore = true;
			ubs = ubs.subList(0, count);
		}
		
		rs.put("hasMore", hasMore);
		rs.put("users", UserBanning.buildRoomBanningView(ubs));
		msg.setData(rs);
		
		System.out.println(msg.toString());
	}
}
