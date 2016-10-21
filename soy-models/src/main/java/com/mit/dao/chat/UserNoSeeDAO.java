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
import com.mit.entities.chat.UserNoSee;
import com.mongodb.client.result.UpdateResult;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nghiatc
 * @since Apr 27, 2016
 */
public class UserNoSeeDAO extends CommonDAO {
    private final Logger _logger = LoggerFactory.getLogger(UserNoSeeDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static UserNoSeeDAO _instance;

	private String TABLE_NAME = "user_no_see";

	public static UserNoSeeDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new UserNoSeeDAO();
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
    
    public UserNoSee getById(int id) {
		UserNoSee uns= null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					uns= new UserNoSeeDAO.MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return uns;
	}
    
    public int insert(UserNoSee uns) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				if(uns.getId() <= 0) {
                    return MongoErrorCode.ID_INVALID;
					//uns.setId(MIdGenDAO.getInstance(TABLE_NAME).getNext());
				}
                UserNoSeeDAO.MongoMapper mapper = new UserNoSeeDAO.MongoMapper();
				Document obj = mapper.toDocument(uns);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}

	public int update(UserNoSee uns) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				UserNoSeeDAO.MongoMapper mapper = new UserNoSeeDAO.MongoMapper();
				Document filter = new Document("_id", uns.getId());
				Document obj = new Document("$set", mapper.toDocument(uns));
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
				UserNoSeeDAO.MongoMapper mapper = new UserNoSeeDAO.MongoMapper();
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
    
    private class MongoMapper extends MongoDBParse<UserNoSee> {

		@Override
		public UserNoSee parseObject(Document doc) {
            List<Integer> listUser = (List<Integer>)doc.get("setUserId");
			UserNoSee obj = new UserNoSee(doc.getInteger("_id"), new HashSet<Integer>(listUser));
			return obj;
		}

		@Override
		public Document toDocument(UserNoSee obj) {
			Document doc = new Document("_id", obj.getId()).append("setUserId", obj.getSetUserId());
			return doc;
		}
	}
    
    public static void main(String[] args) {
//        Set<Integer> setUser = new ConcurrentHashSet<>();
//        setUser.add(1);
//        setUser.add(2);
//        setUser.add(3);
//        UserNoSee une = new UserNoSee();
//        une.setId(-1);
//        une.setSetUserId(setUser);
//        int err = UserNoSeeDAO.getInstance().insert(une);
//        System.out.println("insert err: " + err + " UserNoSee: " + une.toString());
        
        UserNoSee une1 = UserNoSeeDAO.getInstance().getById(1);
        System.out.println("getById UserNoSee: " + une1.toString());
        
    }
    
}
