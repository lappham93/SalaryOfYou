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
import com.mit.entities.chat.LockUserRoom;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
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
public class LockUserRoomDAO extends CommonDAO {
    private final Logger _logger = LoggerFactory.getLogger(LockUserRoomDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static LockUserRoomDAO _instance;

	private String TABLE_NAME = "lock_user_room";

	public static LockUserRoomDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new LockUserRoomDAO();
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
    
    public List<LockUserRoom> getByUserId(int userId) {
		List<LockUserRoom> lurs = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("userId", userId);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(new Document("createTime", -1));
				if (doc != null) {
					lurs = new LockUserRoomDAO.MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("getByUserId ", e);
			}
		}
		return lurs;
	}
    
    public LockUserRoom getById(int id) {
		LockUserRoom lur = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					lur= new LockUserRoomDAO.MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return lur;
	}
    
    public int insert(LockUserRoom lur) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				LockUserRoomDAO.MongoMapper mapper = new LockUserRoomDAO.MongoMapper();
				if(lur.getId() <= 0) {
					lur.setId(MIdGenDAO.getInstance(TABLE_NAME).getNext());
				}
				Document obj = mapper.toDocument(lur);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}

	public int update(LockUserRoom lur) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				LockUserRoomDAO.MongoMapper mapper = new LockUserRoomDAO.MongoMapper();
				Document filter = new Document("_id", lur.getId());
				Document obj = new Document("$set", mapper.toDocument(lur));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("update ", e);
			}
		}

		return rs;
	}
    
    public int delete(int id) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document("_id", id);
				DeleteResult qRs = dbSource.getCollection(TABLE_NAME).deleteOne(filter);
				rs = (int) qRs.getDeletedCount();
			} catch(final Exception e) {
				_logger.error("delete ", e);
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

	private class MongoMapper extends MongoDBParse<LockUserRoom> {

		@Override
		public LockUserRoom parseObject(Document doc) {
			LockUserRoom obj = new LockUserRoom(doc.getInteger("_id"), doc.getInteger("roomId"), doc.getInteger("userId"),
					doc.getDate("timeLock").getTime(), doc.getLong("timeDuration"));
			return obj;
		}

		@Override
		public Document toDocument(LockUserRoom obj) {
			Document doc = new Document("_id", obj.getId())
					.append("roomId", obj.getRoomId())
                    .append("userId", obj.getUserId())
                    .append("timeLock", new Date())
                    .append("timeDuration", obj.getTimeDuration());
			return doc;
		}

	}
}
