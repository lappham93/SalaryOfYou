/*
 * Copyright 2016 nghiatc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mit.dao.chat;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.dao.mid.MIdGenDAO;
import com.mit.entities.chat.TrackingReportUser;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nghiatc
 * @since Apr 28, 2016
 */
public class TrackingReportUserDAO extends CommonDAO {
    private final Logger _logger = LoggerFactory.getLogger(TrackingReportUserDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static TrackingReportUserDAO _instance;

	private String TABLE_NAME = "tracking_report_user";

	public static TrackingReportUserDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new TrackingReportUserDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}
	
    public int totalAll() {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document();
				rs = (int)dbSource.getCollection(TABLE_NAME).count(filter);
			} catch(final Exception e) {
				_logger.error("totalAll ", e);
			}
		}
		return rs;
	}
    
//    public List<TrackingReportUser> getListTrackingUser
    
//    public TrackingReportUser getById(int id) {
//		TrackingReportUser tru = null;
//		if(dbSource != null) {
//			try {
//				Document objFinder = new Document("_id", id);
//				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
//				if(doc != null) {
//					tru= new TrackingReportUserDAO.MongoMapper().parseObject(doc);
//				}
//			} catch(final Exception e) {
//				_logger.error("getById ", e);
//			}
//		}
//
//		return tru;
//	}
    
    public List<TrackingReportUser> getByUserId(int userId) {
		List<TrackingReportUser> trus = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", userId);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					trus = new MongoMapper().parseList((List)doc.get("Items"));
				}
			} catch (final Exception e) {
				_logger.error("getByUserId ", e);
			}
		}
		return trus;
	}
    
    public List<List<TrackingReportUser>> getWithPaging(int count, int from) {
    	List<List<TrackingReportUser>> trus = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("count", new Document("$gt", 0));
				Document sort = new Document("count", -1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort).skip(from).limit(count);
				if (doc != null) {
					trus = new ArrayList<>();
					MongoCursor<Document> docIter = doc.iterator();
					while(docIter.hasNext()) {
						trus.add(new MongoMapper().parseList((List)docIter.next().get("Items")));
					}
				}
			} catch (final Exception e) {
				_logger.error("getByUserId ", e);
			}
		}
		return trus;
    }
    
//    public List<TrackingReportUser> getByUserAndRoom(int userId, int roomId) {
//		List<TrackingReportUser> trus = null;
//		if (dbSource != null) {
//			try {
//				Document objFinder = new Document("userId", userId)
//						.append("roomId", roomId)
//						.append("status", new Document("$gt", 0));
//				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
//				if (doc != null) {
//					trus = new TrackingReportUserDAO.MongoMapper().parseList(doc);
//				}
//			} catch (final Exception e) {
//				_logger.error("getByUserAndRoom ", e);
//			}
//		}
//		return trus;
//	}
//    
//    public List<TrackingReportUser> getAll() {
//    	List<TrackingReportUser> trus = null;
//		if (dbSource != null) {
//			try {
//				Document objFinder = new Document("status", new Document("$gt", 0));
//				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(new Document("userId", 1).append("createTime", 1));
//				if (doc != null) {
//					trus = new TrackingReportUserDAO.MongoMapper().parseList(doc);
//				}
//			} catch (final Exception e) {
//				_logger.error("getByUserId ", e);
//			}
//		}
//		return trus;
//    }
    
//    public int updateStatusAccUser(int userId) {
//    	int rs = MongoErrorCode.NOT_CONNECT;
//    	if (dbSource != null) {
//    		try {
//    			List<TrackingReportUser> trus = getByUserId(userId);
//    			Document update = new Document("$set", new Document("status", 0));
//    			List<WriteModel<Document>> requests = new ArrayList<>();
//    			for (TrackingReportUser tru : trus) {
//    				Document filter = new Document("_id", tru.getId());
//    				requests.add(new UpdateOneModel<>(filter, update));
//    			}
//    			BulkWriteResult bwr = dbSource.getCollection(TABLE_NAME).bulkWrite(requests);
//    			rs = bwr.getModifiedCount();
//    		} catch (final Exception e) {
//    			_logger.error("updateStatusAccUser ", e);
//    		}
//    	}
//    	
//    	return rs;
//    } 
    
//    public int updateStatusAccUserAndRoom(int userId, int roomId) {
//    	int rs = MongoErrorCode.NOT_CONNECT;
//    	if (dbSource != null) {
//    		try {
//    			List<TrackingReportUser> trus = getByUserAndRoom(userId, roomId);
//    			Document update = new Document("$set", new Document("status", 0));
//    			List<WriteModel<Document>> requests = new ArrayList<>();
//    			for (TrackingReportUser tru : trus) {
//    				Document filter = new Document("_id", tru.getId());
//    				requests.add(new UpdateOneModel<>(filter, update));
//    			}
//    			BulkWriteResult bwr = dbSource.getCollection(TABLE_NAME).bulkWrite(requests);
//    			rs = bwr.getModifiedCount();
//    		} catch (final Exception e) {
//    			_logger.error("updateStatusAccUserAndRoom ", e);
//    		}
//    	}
//    	
//    	return rs;
//    } 
    
//    public int insert(TrackingReportUser tru) {
//		int rs = MongoErrorCode.NOT_CONNECT;
//		if(dbSource != null) {
//			try {
//				TrackingReportUserDAO.MongoMapper mapper = new TrackingReportUserDAO.MongoMapper();
//				if(tru.getId() <= 0) {
//					tru.setId(MIdGenDAO.getInstance(TABLE_NAME).getNext());
//				}
//				Document obj = mapper.toDocument(tru);
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
    
    public int countUser() {
    	int rs = MongoErrorCode.NOT_CONNECT;
    	if (dbSource != null) {
    		try {
    			Document filter = new Document("count", new Document("$gt", 0));
    			rs = (int)dbSource.getCollection(TABLE_NAME).count(filter);
    		} catch(final Exception e) {
    			_logger.debug("countUser ", e);
    		}
    	}
    	
    	return rs;
    }
    
    public int upsert(TrackingReportUser tru) {
    	int rs = MongoErrorCode.NOT_CONNECT;
    	if (dbSource != null) {
    		try {
    			if(tru.getId() <= 0) {
					tru.setId(MIdGenDAO.getInstance(TABLE_NAME).getNext());
				}
    			MongoMapper mm = new MongoMapper();
    			Document filter = new Document("_id", tru.getReportId());
    			Document update = new Document("$push", new Document("Items", new Document("$each", Arrays.asList(mm.toDocument(tru)))
    					.append("$position", 0))).append("$inc", new Document("count", 1));
    			UpdateResult ur = dbSource.getCollection(TABLE_NAME).updateOne(filter, update, new UpdateOptions().upsert(true));
    			rs = (int)ur.getModifiedCount();
    		} catch (final Exception e) {
    			_logger.error("upsert ", e);
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
    		} catch(final Exception e) {
    			_logger.debug("getCount ", e);
    		}
    	}
    	
    	return rs;
    }

	public int deleteTrackingItem(long trackingId) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				TrackingReportUserDAO.MongoMapper mapper = new TrackingReportUserDAO.MongoMapper();
				Document filter = new Document("Items._id", trackingId);
				Document obj = new Document("$set", new Document("Items.$.status", 0));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
				if (rs > 0) {
					obj = new Document("$inc", new Document("count", -rs));
					dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				}
			} catch(final Exception e) {
				_logger.error("deleteTrackingItem ", e);
			}
		}

		return rs;
	}
    
    public int hardDeleteTrackingItem(long trackingId) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document("Items._id", trackingId);
				Document update = new Document("$pull", new Document("Items", new Document("_id", trackingId)));
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, update);
				rs = (int) qRs.getModifiedCount();
				if (rs > 0) {
					update = new Document("$inc", new Document("count", -rs));
					dbSource.getCollection(TABLE_NAME).updateOne(filter, update);
				}
			} catch(final Exception e) {
				_logger.error("hardDeleteTrackingItem ", e);
			}
		}

		return rs;
	}
    
    public int hardDeleteUserAndRoom(int userId, int roomId) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document("_id", userId);
				Document update = new Document("$pull", new Document("Items", new Document("roomId", roomId)));
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, update);
				rs = (int) qRs.getModifiedCount();
				if (rs > 0) {
					Document doc = dbSource.getCollection(TABLE_NAME).find(filter).first();
					if (doc != null) {
						update = new Document("$set", new Document("count", ((List)doc.get("Items")).size()));
						dbSource.getCollection(TABLE_NAME).updateOne(filter, update);
					}
				}
			} catch(final Exception e) {
				_logger.error("hardDeleteTrackingItem ", e);
			}
		}

		return rs;
	} 
    
    public int addField(String field, Object defaultValue) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document();
				Document obj = new Document("$set", new Document("Items", new Document(field, defaultValue)));
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateMany(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch (final Exception e) {
				_logger.error("addField ", e);
			}
		}

		return rs;
	}

	private class MongoMapper extends MongoDBParse<TrackingReportUser> {

		@Override
		public TrackingReportUser parseObject(Document doc) {
			TrackingReportUser obj = new TrackingReportUser(doc.getLong("_id"), doc.getInteger("userId"), doc.getInteger("reportId"),
					doc.getInteger("roomId"), doc.getDate("timeReport").getTime(), doc.getInteger("status"));
			return obj;
		}

		@Override
		public Document toDocument(TrackingReportUser obj) {
			Document doc = new Document("_id", obj.getId())
					.append("userId", obj.getUserId())
					.append("reportId", obj.getReportId())
					.append("roomId", obj.getRoomId())
                    .append("timeReport", new Date())
                    .append("status", obj.getStatus());
			return doc;
		}

	}
	
	public static void main(String[] args) {
//		TrackingReportUserDAO.getInstance().upsert(new TrackingReportUser(0, 99, 28, 3, new Date().getTime()));
//		List<List<TrackingReportUser>> trus = TrackingReportUserDAO.getInstance().getWithPaging(10, 1);
//		for (List<TrackingReportUser> tru : trus) 
//			System.out.println(JsonUtils.Instance.toJson(tru));
	}
    
}
