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
import com.mit.entities.chat.Room;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.UpdateResult;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nghiatc
 * @since Mar 7, 2016
 */
public class RoomDAO extends CommonDAO {
    private final Logger _logger = LoggerFactory.getLogger(RoomDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static RoomDAO _instance;

	private String TABLE_NAME = "room";

	public static RoomDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new RoomDAO();
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
    
    public int totalAllStatus(int status) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document("status", status);
				rs = (int)dbSource.getCollection(TABLE_NAME).count(filter);
			} catch(final Exception e) {
				_logger.error("totalAllStatus ", e);
			}
		}
		return rs;
	}
    
    public List<Room> getSlice(int offset, int count) {
		List<Room> rooms = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document();
				Document sort = new Document("order", -1).append("createTime", -1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort).skip(offset).limit(count);
				if (doc != null) {
					rooms = new MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("getSliceActive ", e);
			}
		}
		return rooms;
	}
    
    public List<Room> getSlideAll(int offset, int count, boolean ascOrder) {
		List<Room> rooms = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document();
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(new Document("createTime", ascOrder ? 1 : -1)).skip(offset).limit(count);
				if (doc != null) {
					rooms = new RoomDAO.MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("getSlideAll ", e);
			}
		}
		return rooms;
	}
    
    public List<Room> listAll() {
		List<Room> rooms = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document();
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(new Document("order", -1).append("createTime", -1));
				if (doc != null) {
					rooms = new RoomDAO.MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("listAll ", e);
			}
		}
		return rooms;
	}
    
    public List<Room> listAllActive() {
		List<Room> rooms = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("status", 1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(new Document("order", -1).append("createTime", -1));
				if (doc != null) {
					rooms = new RoomDAO.MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("listAllActive ", e);
			}
		}
		return rooms;
	}
    
    public List<Room> getSlideByStatus(int offset, int count, int status, boolean ascOrder) {
		List<Room> rooms = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("status", status);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(new Document("order", ascOrder ? 1 : -1).append("createTime", ascOrder ? 1 : -1)).skip(offset).limit(count);
				if (doc != null) {
					rooms = new RoomDAO.MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("getSlideByStatus ", e);
			}
		}
		return rooms;
	}
    
	public List<Room> getByListId(List<Integer> ids) {
		List<Room> rooms = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", new Document("$in", ids)).append("status", new Document("$gt", 0));
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if(doc != null) {
					rooms = new RoomDAO.MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getByListId ", e);
			}
		}

		return rooms;
	}

	public Room getById(int id) {
		Room room= null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					room= new RoomDAO.MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return room;
	}
    
    public int insert(Room room) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				RoomDAO.MongoMapper mapper = new RoomDAO.MongoMapper();
				if(room.getId() <= 0) {
					room.setId(MIdGenDAO.getInstance(TABLE_NAME).getNext());
				}
				Document obj = mapper.toDocument(room);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}

	public int update(Room room) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				RoomDAO.MongoMapper mapper = new RoomDAO.MongoMapper();
				Document filter = new Document("_id", room.getId());
				Document obj = new Document("$set", mapper.toDocument(room));
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

	private class MongoMapper extends MongoDBParse<Room> {

		@Override
		public Room parseObject(Document doc) {
			Room account = new Room(doc.getInteger("_id"), doc.getString("name"), doc.getInteger("order"),
					doc.getInteger("size"), doc.getLong("photoId"), doc.getString("desc"), doc.getInteger("status"), 
					doc.getDate("createTime").getTime(), doc.getDate("updateTime").getTime());
			return account;
		}

		@Override
		public Document toDocument(Room obj) {
			Document doc = new Document("_id", obj.getId())
					.append("name", obj.getName())
					.append("order", obj.getOrder())
					.append("size", obj.getSize())
                    .append("photoId", obj.getPhotoId())
                    .append("desc", obj.getDesc())
					.append("status", obj.getStatus());
			return doc;
		}

	}
    
    public static void main(String[] args) {
        //1.
//        Room r = new Room();
//        r.setId(-1);
//        r.setName("room3");
//        r.setOrder(3);
//        r.setSize(100);
//        r.setDesc("room 3 description");
//        r.setStatus(1);
//        int err = RoomDAO.getInstance().insert(r);
//        System.out.println("err: " + err);
        
        //2.
        int err = RoomDAO.getInstance().addField("photoId", 0L);
        System.out.println("err: " + err);
        
        //3.
//        Room r = RoomDAO.getInstance().getById(1);
//        System.out.println("r: " + r);
        
        //4.
        
        
    }
    
}
