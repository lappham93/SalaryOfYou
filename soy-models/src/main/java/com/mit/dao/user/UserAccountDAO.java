package com.mit.dao.user;

import java.util.HashMap;
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
import com.mit.entities.user.UserAccount;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class UserAccountDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(UserAccountDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static UserAccountDAO _instance;

	public static final String TABLE_NAME = "user_accounts";

	public static UserAccountDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new UserAccountDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private UserAccountDAO() {
		super();
	}

	public UserAccount getByUserName(String userName) {
		UserAccount account = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("userName", userName).append("status", new Document("$gt", 0));
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					account = new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getByUserName ", e);
			}
		}
		return account;
	}

	public UserAccount getByUserNameAndRole(String userName, int roleId) {
		UserAccount account = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("userName", userName).append("roleId", roleId).append("status", new Document("$gt", 0));
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					account = new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getByUserName ", e);
			}
		}
		return account;
	}
	
	public Map<String, UserAccount> getMapByUserNameList(List<String> userNames, int roleId) {
		Map<String, UserAccount> accounts = new HashMap<String, UserAccount>();
		if(dbSource != null) {
			try {
				Document objFinder = new Document("userName", new Document("$in", userNames)).append("roleId", roleId).append("status", new Document("$gt", 0));
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if(docs != null) {
					MongoMapper mapper = new MongoMapper();
					Document doc = null;
					MongoCursor<Document> cursor = docs.iterator();
					while(cursor != null && cursor.hasNext() && (doc = cursor.next()) != null) {
						UserAccount account = mapper.parseObject(doc);
						accounts.put(account.getUsername(), account);
					}
				}
			} catch(final Exception e) {
				_logger.error("getMapByUserNameList ", e);
			}
		}
		return accounts;
	}
	
	public Map<Integer, String> getUserNameByIdList(List<Integer> userIds) {
		Map<Integer, String> accounts = new HashMap<Integer, String>();
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", new Document("$in", userIds));
				Document projection = new Document("userName", 1);
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection);
				if(docs != null) {
					Document doc = null;
					MongoCursor<Document> cursor = docs.iterator();
					while(cursor != null && cursor.hasNext() && (doc = cursor.next()) != null) {
						accounts.put(doc.getInteger("_id"), doc.getString("userName"));
					}
				}
			} catch(final Exception e) {
				_logger.error("getUserNameByIdList ", e);
			}
		}
		return accounts;
	}

	public UserAccount getById(int id) {
		UserAccount account = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					account = new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getById ", e);
			}
		}
		return account;
	}

	public UserAccount getByUserNameAndRoles(String userName, List<Short> roleIds) {
		UserAccount account = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("userName", userName).append("roleId", new Document("$in", roleIds)).append("status", new Document("$gt", 0));
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					account = new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getByUserNameAndRoles ", e);
			}
		}
		return account;
	}

	public UserAccount getByEmail(String email) {
		UserAccount account = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("email", email).append("status", new Document("$gt", 0));
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					account = new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getByEmail ", e);
			}
		}
		return account;
	}
	
	public List<UserAccount> getAll() {
		List<UserAccount> listUA = null;
		if (dbSource != null) {
			try {
				FindIterable<Document> listDoc = dbSource.getCollection(TABLE_NAME).find();
				if (listDoc != null) {
					listUA = new MongoMapper().parseList(listDoc);
				}
			}
			catch (Exception e) {
				_logger.error("getAll ", e);
			}
		}
		
		return listUA;
	}
	
	public List<UserAccount> getByType(int type) {
		List<UserAccount> listUA = null;
		if (dbSource != null) {
			try {
				Document filter = new Document("type", type);
				FindIterable<Document> listDoc = dbSource.getCollection(TABLE_NAME).find(filter);
				if (listDoc != null) {
					listUA = new MongoMapper().parseList(listDoc);
				}
			}
			catch (Exception e) {
				_logger.error("getByType ", e);
			}
		}
		
		return listUA;
	}

	public List<UserAccount> getListfromIds(List<Long> ids) {
		List<UserAccount> uas = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", new Document("$in", ids));
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if (doc != null) {
					uas = new MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("getListfromIds ", e);
			}
		}

		return uas;
	}
	
	public int insert(UserAccount user) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document obj = mapper.toDocument(user);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}

	public int update(UserAccount user) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", user.getId());
				Document obj = new Document("$set", mapper.toDocument(user));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult updateRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int)updateRs.getModifiedCount();// MongoErrorCode.SUCCESS;
			} catch(Exception e) {
				_logger.error("update ", e);
			}
		}

		return rs;
	}

	public int updateDefaultBiz(int accountId, int bizId) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document("_id", accountId);
				Document obj = new Document("$set", new Document("defaultBizId", bizId));
				MongoMapper mapper = new MongoMapper();
				obj = mapper.buildUpdateTime(obj);
				UpdateResult updateRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int)updateRs.getModifiedCount();// MongoErrorCode.SUCCESS;
			} catch(Exception e) {
				_logger.error("updateDefaultShop ", e);
			}
		}

		return rs;
	}

	public int updateStatus(int id, byte status) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document("_id", id);
				Document obj = new Document("$set", new Document("status", status));
				MongoMapper mapper = new MongoMapper();
				obj = mapper.buildUpdateTime(obj);
				UpdateResult updateRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int)updateRs.getModifiedCount();// MongoErrorCode.SUCCESS;
			} catch(Exception e) {
				_logger.error("updateStatus ", e);
			}
		}

		return rs;
	}

	public int delete(int id) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document("_id", id);
				Document obj = new Document("$set", new Document("status", 0));
				UpdateResult updateRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int)updateRs.getModifiedCount();// MongoErrorCode.SUCCESS;
			} catch(Exception e) {
				_logger.error("delete ", e);
			}
		}

		return rs;
	}

	public int hardDelete(int id) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document("_id", id);
				DeleteResult updateRs = dbSource.getCollection(TABLE_NAME).deleteOne(filter);
				rs = (int)updateRs.getDeletedCount();// MongoErrorCode.SUCCESS;
			} catch(Exception e) {
				_logger.error("hardDelete ", e);
			}
		}

		return rs;
	}
	
	public int hardDeleteByUserName(String userName) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document("userName", userName);
				DeleteResult updateRs = dbSource.getCollection(TABLE_NAME).deleteOne(filter);
				rs = (int)updateRs.getDeletedCount();// MongoErrorCode.SUCCESS;
			} catch(Exception e) {
				_logger.error("hardDeleteByUserName ", e);
			}
		}

		return rs;
	}
	
	public int hardDeleteByEmail(String email) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document("email", email);
				DeleteResult updateRs = dbSource.getCollection(TABLE_NAME).deleteMany(filter);
				rs = (int)updateRs.getDeletedCount();// MongoErrorCode.SUCCESS;
			} catch(Exception e) {
				_logger.error("hardDeleteByEmail ", e);
			}
		}

		return rs;
	}
	
	public int revokeBiz(int bizId) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document("defaultBizId", bizId);
				Document obj = new Document("$set", new Document("defaultBizId", 0));
				MongoMapper mapper = new MongoMapper();
				obj = mapper.buildUpdateTime(obj);
				UpdateResult updateRs = dbSource.getCollection(TABLE_NAME).updateMany(filter, obj);
				rs = (int)updateRs.getModifiedCount();// MongoErrorCode.SUCCESS;
			} catch(Exception e) {
				_logger.error("revokeBiz ", e);
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

	private class MongoMapper extends MongoDBParse<UserAccount> {

		@Override
		public UserAccount parseObject(Document doc) {
			UserAccount account = new UserAccount(doc.getInteger("_id"), doc.getInteger("roleId").byteValue(), doc.getString ("userName"), doc.getString("password"), doc.getString("salt"),
					doc.getString("email"), doc.getInteger("defaultBizId", 0), doc.getInteger("status").byteValue(), doc.getInteger("type"), doc.getDate("createTime").getTime(), doc.getDate("updateTime").getTime());
			return account;
		}

		@Override
		public Document toDocument(UserAccount obj) {
			Document doc = new Document("_id", obj.getId()).append("roleId", obj.getRoleId()).append("userName", obj.getUsername()).append("password", obj.getPassword()).append("salt", obj.getSalt())
					.append("email", obj.getEmail()).append("defaultBizId", obj.getDefaultBizId()).append("status", obj.getStatus()).append("type", obj.getType());
			return doc;
		}

	}

}
