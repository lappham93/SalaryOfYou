package com.mit.dao.user;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
import com.mit.entities.user.UserInfo;
import com.mit.entities.user.UserProfileType;
import com.mit.utils.JsonUtils;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class UserInfoDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory
			.getLogger(UserInfoDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static UserInfoDAO _instance;

	private final String TABLE_NAME = "user_info";

	public static UserInfoDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new UserInfoDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private UserInfoDAO() {
	}
	
	public List<UserInfo> getSlideCND(String email, List<Integer> cndIds, int from, int count) {
		List<UserInfo> customers = null;
		if(dbSource != null) {
			try {
				List<Document> orExp = Arrays.asList(new Document("cndLevel", new Document("$in", cndIds)), 
						new Document("cndPurAmtInPer", new Document("$gt", 0)),
						new Document("cndBufPurAmtInPer", new Document("$gt", 0)));
				Document objFinder = new Document("email", new Document("$regex", email))
						.append("$or", orExp)
						.append("status", new Document("$eq", 1));
				Document sort = new Document("cndUpdateTime", -1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort).skip(from).limit(count);
				if(doc != null) {
					customers = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getSlideCND ", e);
			}
		}

		return customers;
	}
	
	public List<UserInfo> getSlideCND(List<Integer> cndIds, int from, int count) {
		List<UserInfo> customers = null;
		if(dbSource != null) {
			try {
				List<Document> orExp = Arrays.asList(new Document("cndLevel", new Document("$in", cndIds)), 
						new Document("cndPurAmtInPer", new Document("$gt", 0)),
						new Document("cndBufPurAmtInPer", new Document("$gt", 0)));
				Document objFinder = new Document("$or", orExp)
						.append("status", new Document("$eq", 1));
				Document sort = new Document("cndUpdateTime", -1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort).skip(from).limit(count);
				if(doc != null) {
					customers = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getSlideCND ", e);
			}
		}

		return customers;
	}

	public List<UserInfo> getListfromIds(List<Integer> userIds) {
		List<UserInfo> customers = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", new Document("$in", userIds)).append("status", new Document("$eq", 1));
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if(doc != null) {
					customers = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getListfromIds ", e);
			}
		}

		return customers;
	}
	
	public List<Long> getAllUserIds() {
		List<Long> customers = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("status", new Document("$eq", 1));
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if(doc != null) {
					customers = new LinkedList<>();
					MongoCursor<Document> tmps = doc.iterator();
					while (tmps.hasNext()) {
						customers.add((long)tmps.next().getInteger("_id"));
					}
				}
			} catch(final Exception e) {
				_logger.error("getAllUserIds ", e);
			}
		}

		return customers;
	}
	
	public List<UserInfo> getListfromIdsLong(List<Long> userIds) {
		List<UserInfo> customers = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", new Document("$in", userIds)).append("status", new Document("$eq", 1));
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if(doc != null) {
					customers = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getListfromIds ", e);
			}
		}

		return customers;
	}
	
	public Map<Integer, UserInfo> getMapFromIdList(List<Integer> userIds) {
		Map<Integer, UserInfo> userInfos = new HashMap<Integer, UserInfo>();
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", new Document("$in", userIds));
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if(docs != null) {
					MongoMapper mapper = new MongoMapper();
					Document doc = null;
					MongoCursor<Document> cursor = docs.iterator();
					while(cursor != null && cursor.hasNext() && (doc = cursor.next()) != null) {
						UserInfo userInfo = mapper.parseObject(doc);
						userInfos.put(userInfo.getId(), userInfo);
					}
				}
			} catch(final Exception e) {
				_logger.error("getMapFromIdList ", e);
			}
		}

		return userInfos;
	}
	
	public Map<Integer, UserInfo> getMapFromIdLList(List<Long> userIds) {
		Map<Integer, UserInfo> userInfos = new HashMap<Integer, UserInfo>();
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", new Document("$in", userIds));
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if(docs != null) {
					MongoMapper mapper = new MongoMapper();
					Document doc = null;
					MongoCursor<Document> cursor = docs.iterator();
					while(cursor != null && cursor.hasNext() && (doc = cursor.next()) != null) {
						UserInfo userInfo = mapper.parseObject(doc);
						userInfos.put(userInfo.getId(), userInfo);
					}
				}
			} catch(final Exception e) {
				_logger.error("getMapFromIdList ", e);
			}
		}

		return userInfos;
	}
	
	public Map<Long, UserInfo> getSliceFromIds(List<Long> userIds, int count, int from) {
		Map<Long, UserInfo> userInfos = new HashMap<Long, UserInfo>();
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", new Document("$in", userIds));
				Document sort = new Document("updateTime", -1);
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort).skip(from).limit(count);
				if(docs != null) {
					MongoMapper mapper = new MongoMapper();
					Document doc = null;
					MongoCursor<Document> cursor = docs.iterator();
					while(cursor != null && cursor.hasNext() && (doc = cursor.next()) != null) {
						UserInfo userInfo = mapper.parseObject(doc);
						userInfos.put((long)userInfo.getId(), userInfo);
					}
				}
			} catch(final Exception e) {
				_logger.error("getSliceFromIds ", e);
			}
		}

		return userInfos;
	}


	public List<Integer> getListByDiscountCode(long discountCode) {
		List<Integer> customers = Collections.emptyList();
		if(dbSource != null) {
			try {
				Document objFinder = new Document("defaultDiscountCode", discountCode).append("status", new Document("$eq", 1));
				Document projection = new Document("_id", 1);
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection);
				if(docs != null) {
					Document doc = null;
					MongoCursor<Document> cursor = docs.iterator();
					customers = new LinkedList<Integer>();
					while(cursor != null && cursor.hasNext() && (doc = cursor.next()) != null) {
						customers.add(doc.getInteger("_id"));
					}
				}
			} catch(final Exception e) {
				_logger.error("getListByDiscountCode ", e);
			}
		}

		return customers;
	}

	public List<UserInfo> getListFromRange(Date from, Date to) {
		List<UserInfo> customers = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("createTime", new Document("$gte", from).append("$lt", to))
					.append("status", new Document("$eq", 1));
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if(doc != null) {
					customers = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getListFromRange ", e);
			}
		}

		return customers;
	}
	
	public List<UserInfo> getListByTypeAndCountry(String countryCode, int type) {
		List<UserInfo> customers = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document();
				if (!"all".equalsIgnoreCase(countryCode)) {
					objFinder.append("countryCode", countryCode);
				}
				if (type != 0) {
					objFinder.append("type", type);
				}
				objFinder.append("status", new Document("$gt", 0));
				
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if(doc != null) {
					customers = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getListByTypeAndCountry ", e);
			}
		}

		return customers;
	}
	
	public List<UserInfo> getSliceCndExpirationDate(long cndExpirationDate, int skip, int limit) {
		List<UserInfo> customers = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("cndExpirationDate", new Document("$lte", cndExpirationDate)).append("status", new Document("$gt", 0));
				Document sort = new Document("_id", 1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort).skip(skip).limit(limit);
				if(doc != null) {
					customers = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getSliceCndExpirationDate ", e);
			}
		}

		return customers;
	}

	public UserInfo getById(int id) {
		UserInfo customer = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					customer = new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return customer;
	}
	
	public UserInfo getUserByEmail(String email) {
		UserInfo customer = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("email", email);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					customer = new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getUserByEmail ", e);
			}
		}

		return customer;
	}
	
	public long countByType(int type) {
		long count = 0;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("type", type);
				count = dbSource.getCollection(TABLE_NAME).count(objFinder);
			} catch(final Exception e) {
				_logger.error("countByType ", e);
			}
		}

		return count;
	}
	
	public long totalAll() {
		long count = 0;
		if(dbSource != null) {
			try {
				Document objFinder = new Document();
				count = dbSource.getCollection(TABLE_NAME).count(objFinder);
			} catch(final Exception e) {
				_logger.error("totalAll ", e);
			}
		}

		return count;
	}
	
	public long totalAll(String email) {
		long count = 0;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("email", new Document("$regex", email));
				count = dbSource.getCollection(TABLE_NAME).count(objFinder);
			} catch(final Exception e) {
				_logger.error("totalAll ", e);
			}
		}

		return count;
	}
	
	public long totalReview() {
		long count = 0;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("isPreviewing", true);
				count = dbSource.getCollection(TABLE_NAME).count(objFinder);
			} catch(final Exception e) {
				_logger.error("totalReview ", e);
			}
		}

		return count;
	}
	
	public long totalReview(String email) {
		long count = 0;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("isPreviewing", true)
						.append("email", new Document("$regex", email));
				count = dbSource.getCollection(TABLE_NAME).count(objFinder);
			} catch(final Exception e) {
				_logger.error("totalReview ", e);
			}
		}

		return count;
	}

    public long getTotalActive(int status) {
		long count = 0;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("status", new Document("$eq", 1));
				count = dbSource.getCollection(TABLE_NAME).count(objFinder);
			} catch(final Exception e) {
				_logger.error("getTotalActive ", e);
			}
		}

		return count;
	}
    
    public long getTotalCND(List<Integer> cndIds) {
		long count = 0;
		if(dbSource != null) {
			try {
				List<Document> orExp = Arrays.asList(new Document("cndLevel", new Document("$in", cndIds)), 
						new Document("cndPurAmtInPer", new Document("$gt", 0)),
						new Document("cndBufPurAmtInPer", new Document("$gt", 0)));
				Document objFinder = new Document("$or", orExp)
						.append("status", new Document("$eq", 1));
				count = dbSource.getCollection(TABLE_NAME).count(objFinder);
			} catch(final Exception e) {
				_logger.error("getTotalCND ", e);
			}
		}

		return count;
	}
    
    public long getTotalCND(String email, List<Integer> cndIds) {
		long count = 0;
		if(dbSource != null) {
			try {
				List<Document> orExp = Arrays.asList(new Document("cndLevel", new Document("$in", cndIds)), 
						new Document("cndPurAmtInPer", new Document("$gt", 0)),
						new Document("cndBufPurAmtInPer", new Document("$gt", 0)));
				Document objFinder = new Document("email", new Document("$regex", email))
						.append("$or", orExp)
						.append("status", new Document("$eq", 1));
				count = dbSource.getCollection(TABLE_NAME).count(objFinder);
			} catch(final Exception e) {
				_logger.error("getTotalCND ", e);
			}
		}

		return count;
	}
    
	public List<UserInfo> getAll() {
		List<UserInfo> listUI = null;
		if (dbSource != null) {
			try {
				FindIterable<Document> listDoc = dbSource.getCollection(TABLE_NAME).find().sort(new Document("updateTime", -1));
				if (listDoc != null) {
					listUI = new MongoMapper().parseList(listDoc);
				}
			}
			catch (Exception e) {
				_logger.error("getAll ", e);
			}
		}
		return listUI;
	}
	
	public List<UserInfo> getAllActive() {
		List<UserInfo> listUI = null;
		if (dbSource != null) {
			try {
				Document filter = new Document("status", new Document("$gt", 0));
				Document sort = new Document("email", 1);
				FindIterable<Document> listDoc = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort);
				if (listDoc != null) {
					listUI = new MongoMapper().parseList(listDoc);
				}
			}
			catch (Exception e) {
				_logger.error("getAllActive ", e);
			}
		}
		return listUI;
	}
	
	public List<UserInfo> getSlice(int count, int from) {
		List<UserInfo> listUI = null;
		if (dbSource != null) {
			try {
				Document filter = new Document();
				Document sort = new Document("updateTime", -1);
				FindIterable<Document> listDoc = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort).skip(from).limit(count);
				if (listDoc != null) {
					listUI = new MongoMapper().parseList(listDoc);
				}
			}
			catch (Exception e) {
				_logger.error("getSlice ", e);
			}
		}
		return listUI;
	}
	
	public List<UserInfo> getSlice(String email, int count, int from) {
		List<UserInfo> listUI = null;
		if (dbSource != null) {
			try {
				Document filter = new Document("email", new Document("$regex", email));
				Document sort = new Document("updateTime", -1);
				FindIterable<Document> listDoc = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort).skip(from).limit(count);
				if (listDoc != null) {
					listUI = new MongoMapper().parseList(listDoc);
				}
			}
			catch (Exception e) {
				_logger.error("getSlice ", e);
			}
		}
		return listUI;
	}
	
	public List<UserInfo> getAllWithSort(String field, int sortOption) {
		List<UserInfo> listUI = null;
		if (dbSource != null) {
			try {
				Document sort = new Document(field, sortOption);
				FindIterable<Document> listDoc = dbSource.getCollection(TABLE_NAME).find().sort(sort);
				if (listDoc != null) {
					listUI = new MongoMapper().parseList(listDoc);
				}
			}
			catch (Exception e) {
				_logger.error("getAllWithSort ", e);
			}
		}
		return listUI;
	}
	
	public List<UserInfo> getReviewSlice(int count, int from) {
		List<UserInfo> listUI = null;
		if (dbSource != null) {
			try {
				Document filter = new Document("isPreviewing", true);
				Document sort = new Document("updateTime", -1);
				FindIterable<Document> listDoc = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort).skip(from).limit(count);
				if (listDoc != null) {
					listUI = new MongoMapper().parseList(listDoc);
				}
			}
			catch (Exception e) {
				_logger.error("getReviewSlice ", e);
			}
		}
		return listUI;
	}
	
	public List<UserInfo> getReviewSlice(String email, int count, int from) {
		List<UserInfo> listUI = null;
		if (dbSource != null) {
			try {
				Document filter = new Document("isPreviewing", true)
						.append("email", new Document("$regex", email));
				Document sort = new Document("updateTime", -1);
				FindIterable<Document> listDoc = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort).skip(from).limit(count);
				if (listDoc != null) {
					listUI = new MongoMapper().parseList(listDoc);
				}
			}
			catch (Exception e) {
				_logger.error("getReviewSlice ", e);
			}
		}
		return listUI;
	}
	
	public List<UserInfo> getByLicenseCode(String licenseCode) {
		List<UserInfo> listUI = null;
		if (dbSource != null) {
			try {
				Document filter = new Document("licenseCode", licenseCode)
						.append("isPreviewing", true);
				FindIterable<Document> listDoc = dbSource.getCollection(TABLE_NAME).find(filter).sort(new Document("updateTime", 1));
				if (listDoc != null) {
					listUI = new MongoMapper().parseList(listDoc);
				}
			}
			catch (Exception e) {
				_logger.error("getByLicenseCode ", e);
			}
		}
		return listUI;
	}
	
//	public List<UserInfo> getByEmail(String email) {
//		List<UserInfo> listUI = null;0933585513
//		if (dbSource != null && email != null) {
//			try {
//				Document filter = new Document("email", new Document("$regex", email));
//				FindIterable<Document> listDoc = dbSource.getCollection(TABLE_NAME).find(filter).sort(new Document("updateTime", -1));
//				if (listDoc != null) {
//					listUI = new MongoMapper().parseList(listDoc);
//				}
//			}
//			catch (Exception e) {
//				_logger.error("getByEmail ", e);
//			}
//		}
//		return listUI;
//	}
	
//	public List<UserInfo> getReviewByEmail(String email) {
//		List<UserInfo> listUI = null;
//		if (dbSource != null && email != null) {
//			try {
//				Document filter = new Document("email", new Document("$regex", email))
//						.append("isPreviewing", true);
//				FindIterable<Document> listDoc = dbSource.getCollection(TABLE_NAME).find(filter).sort(new Document("updateTime", -1));
//				if (listDoc != null) {
//					listUI = new MongoMapper().parseList(listDoc);
//				}
//			}
//			catch (Exception e) {
//				_logger.error("getByEmail ", e);
//			}
//		}
//		return listUI;
//	}
	
	public UserInfo getByAccountId(int accountId) {
		UserInfo customer = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", accountId);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					customer = new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return customer;
	}
	
	public long getDefaultShippingAddress(int accountId) {
		long defaultShippingAddress = 0;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", accountId);
				Document projection = new Document("defaultShippingAddress", 1);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection).first();
				if(doc != null) {
					defaultShippingAddress = doc.getLong("defaultShippingAddress");
				}
			} catch(final Exception e) {
				_logger.error("getDefaultShippingAddress ", e);
			}
		}

		return defaultShippingAddress;
	}
	
	public long getDefaultBillingAddress(int accountId) {
		long defaultBillingAddress = 0;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", accountId);
				Document projection = new Document("defaultBillingAddress", 1);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection).first();
				if(doc != null) {
					defaultBillingAddress = doc.getLong("defaultBillingAddress");
				}
			} catch(final Exception e) {
				_logger.error("getDefaultBillingAddress ", e);
			}
		}
		return defaultBillingAddress;
	}
	
	public Map<String, Long> getPoint(int accountId) {
		Map<String, Long> points = new HashMap<String, Long>();
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", accountId);
				Document projection = new Document("bcPoint", 1).append("cndPoint", 1);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection).first();
				if(doc != null) {
					points.put("bcPoint", doc.getLong("bcPoint"));
					points.put("cndPoint", doc.getLong("cndPoint"));
				}
			} catch(final Exception e) {
				_logger.error("getPoint ", e);
			}
		}
		return points;
	}
	
	public boolean hasLicenseCode(int userId) {
		boolean hasLicenseCode = false;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", userId).append("licenseCode", new Document("$ne", "")).append("isPreviewing", false);
				Document projection = new Document("_id", 1);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection).first();
				if(doc != null) {
					hasLicenseCode = doc.getInteger("_id") == userId;
				}
			} catch(final Exception e) {
				_logger.error("hasLicenseCode ", e);
			}
		}
		return hasLicenseCode;
	}
	
	public long getPhoto(int userId) {
		long photo = 0;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", userId);
				Document projection = new Document("photo", 1);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection).first();
				if(doc != null) {
					photo = doc.getLong("photo");
				}
			} catch(final Exception e) {
				_logger.error("getPhoto ", e);
			}
		}
		return photo;
	}

	public int insert(UserInfo userInfo) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document obj = mapper.toDocument(userInfo);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insert ", e);
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
    
    public int hardDeleteByEmail(String email) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document("email", email);
				DeleteResult updateRs = dbSource.getCollection(TABLE_NAME).deleteMany(filter);
				rs = (int)updateRs.getDeletedCount();// MongoErrorCode.SUCCESS;
			} catch(Exception e) {
				_logger.error("hardDelete ", e);
			}
		}

		return rs;
	}
	
	public int update(UserInfo userInfo) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", userInfo.getId());
				Document obj = new Document("$set", mapper.toDocument(userInfo));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("update ", e);
			}
		}

		return rs;
	}
	
	public int updateBatch(List<UserInfo> userInfos) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				List<WriteModel<Document>> updateModels = new LinkedList<WriteModel<Document>>();
				MongoMapper mapper = new MongoMapper();

				for (UserInfo userInfo: userInfos) {
					Document filter = new Document("_id", userInfo.getId());
					Document obj = new Document("$set", mapper.toDocument(userInfo));
					obj = mapper.buildUpdateTime(obj);
					updateModels.add(new UpdateOneModel<Document>(filter, obj));
				}
				
				BulkWriteResult doc = dbSource.getCollection(TABLE_NAME).bulkWrite(updateModels);
				rs = (int) doc.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("updateBatch ", e);
			}
		}

		return rs;
	}
	
	public int updatePoint(int id, long bcPoint, long cndPoint) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", id);
				Document obj = new Document("$set", new Document("bcPoint", bcPoint).append("cndPoint", cndPoint));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("updatePoint ", e);
			}
		}

		return rs;
	}
	
	public int updateCndExpirationDate(int id, long cndExpirationDate) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", id);
				Document obj = new Document("$set", new Document("cndExpirationDate", cndExpirationDate));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("updateCndExpirationDate ", e);
			}
		}

		return rs;
	}
	
	public int increasePoint(int id, long bcPoint, long cndPoint) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", id);
				Document obj = new Document("$inc", new Document("bcPoint", cndPoint).append("cndPoint", cndPoint));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("increasePoint ", e);
			}
		}

		return rs;
	}

//	public int updateAvatar(int uid) {
//		int rs = MongoErrorCode.NOT_CONNECT;
//		if(dbSource != null) {
//			try {
//				MongoMapper mapper = new MongoMapper();
//				Document filter = new Document("_id", uid);
//				Document obj = new Document("$inc", new Document("avtVer", 1));
//				obj = mapper.buildUpdateTime(obj);
//				Document qRs = dbSource.getCollection(TABLE_NAME).findOneAndUpdate(filter, obj);
//				if(qRs != null) {
//					rs = qRs.getInteger("avtVer") + 1;
//				} else {
//					rs = 1;
//				}
//			} catch(final Exception e) {
//				_logger.error("updateAvatar ", e);
//			}
//		}
//
//		return rs;
//	}
	
	public int updatePhoto(int id, long pId) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", id);
				Document obj = new Document("$set", new Document("photo", pId));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("updatePhoto ", e);
			}
		}

		return rs;
	}

	public int updateStatus(int id, byte status) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", id);
				Document obj = new Document("$set", new Document("status", status));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("updateStatus ", e);
			}
		}

		return rs;
	}
	
	public int updateDefaultDiscountCode(int id, long defaultDiscountCode) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", id);
				Document obj = new Document("$set", new Document("defaultDiscountCode", defaultDiscountCode));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("updateDefaultDiscountCode ", e);
			}
		}

		return rs;
	}
	
	public int updateDefaultBillingAddress(int id, long billingAddressId) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", id);
				Document obj = new Document("$set", new Document("defaultBillingAddress", billingAddressId));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("updateDefaultBillingAddress ", e);
			}
		}

		return rs;
	}
	
	public int updateDefaultShippingAddress(int id, long shippingAddressId) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", id);
				Document obj = new Document("$set", new Document("defaultShippingAddress", shippingAddressId));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("updateDefaultShippingAddress ", e);
			}
		}

		return rs;
	}
	
	public int updateLocation(int id, double lat, double lon) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", id);
				
				Document obj = new Document("$set", new Document("location", new Document("type", "Point").append("coordinates", Arrays.asList(lat, lon))));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("updateLocation ", e);
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
	
	public int removeField(String field) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document();
				Document obj = new Document("$unset", new Document(field, ""));
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateMany(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch (final Exception e) {
				_logger.error("addField ", e);
			}
		}

		return rs;
	}

	private class MongoMapper extends MongoDBParse<UserInfo> {

		@Override
		public UserInfo parseObject(Document doc) {
			UserInfo account = null;
			if(doc.getInteger("type", UserProfileType.USER.getValue()) == UserProfileType.USER.getValue()) {
				account = new UserInfo(doc.getInteger("_id"), doc.getString ("firstName"), 
						doc.getString("lastName"), doc.getInteger("gender"), doc.getString("email"),
					doc.getString("mobilePhone"), doc.getString("countryCode"), doc.getString("birthday"), 
					doc.getLong("photo"), doc.getInteger("status").byteValue(), 
					doc.getDate("createTime").getTime(), doc.getDate("updateTime").getTime());
				account.setType(UserProfileType.USER.getValue());
			} else {
				account = new UserInfo();
				account.setId(doc.getInteger("_id"));
				account.setType(doc.getInteger("type").byteValue());
			}

			return account;
		}

		@Override
		public Document toDocument(UserInfo obj) {
			Document doc = new Document("_id", obj.getId())
				.append("firstName", obj.getFirstName())
				.append("lastName", obj.getLastName())
				.append("gender", obj.getGender())
				.append("photo", obj.getPhoto())
//				.append("licenseCode", obj.getLicenseCode())
//				.append("defaultBillingAddress", obj.getDefaultBillingAddress())
//				.append("defaultShippingAddress", obj.getDefaultShippingAddress())
				.append("email", obj.getEmail())
				.append("mobilePhone", obj.getMobilePhone())
				.append("countryCode", obj.getCountryCode())
				.append("birthday", obj.getBirthday())
				.append("type", obj.getType())
//				.append("bcPoint", obj.getBcPoint())
//				.append("cndPoint", obj.getCndPoint())
//				.append("cndLevel", obj.getCndLevel())
//				.append("cndPurAmtInPer", BigDecimalUtils.Instance.convertToLong(obj.getCndPurAmtInPer()))
//				.append("cndBufPurAmtInPer", BigDecimalUtils.Instance.convertToLong(obj.getCndBufPurAmtInPer()))
//				.append("cndExpirationDate", obj.getCndExpirationDate())
				.append("type", obj.getType())
//				.append("isPreviewing", obj.isPreviewing())
				.append("status", obj.getStatus())
				.append("cndUpdateTime", new Date());
			return doc;
		}

	}
	
	public static void main(String[] args) {
//		UserInfoDAO.getInstance().removeField("defaultDiscountCode");
//		long l = 0;
//		int rs = UserInfoDAO.getInstance().addField("defaultDiscountCode", l);
//		System.out.println(rs);
//		int rs = UserInfoDAO.getInstance().addField("countryCode", "VN");
//		List<UserInfo> result = UserInfoDAO.getInstance().getByEmail("123");
//		UserInfoDAO.getInstance().addField("bcPoint", 0L);
//		UserInfoDAO.getInstance().addField("cndPoint", 0L);
//		UserInfoDAO.getInstance().addField("isPreviewing", false);
//		UserInfoDAO.getInstance().addField("cndLevel", 0);
//		UserInfoDAO.getInstance().addField("cndPurAmtInPer", 0L);
//		UserInfoDAO.getInstance().addField("cndPeriodStart", System.currentTimeMillis());
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.DATE, AppConstantDAO.getInstance().getByKey(AppConstantKey.CND_EXPIRATION_LENGTH.getKey()).getIntValue());
//		UserInfoDAO.getInstance().addField("cndExpirationDate", cal.getTimeInMillis());
//		UserInfoDAO.getInstance().getById(1);
        
        String email = "congnghia0609@gmail.com";
        UserInfo ui = UserInfoDAO.getInstance().getUserByEmail(email);
        System.out.println("ui: " + JsonUtils.Instance.toJson(ui));
        
        
	}
}