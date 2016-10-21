package com.mit.dao.stream;

import java.util.Collection;
import java.util.Date;
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
import com.mit.dao.mid.MIdGenLongDAO;
import com.mit.entities.stream.SocialStream;
import com.mit.entities.stream.SocialStream.SocialStreamStatus;
import com.mit.utils.NumberUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class SocialStreamDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(SocialStreamDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static SocialStreamDAO _instance;

	private final String TABLE_NAME = "social_stream";

	public static SocialStreamDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new SocialStreamDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private SocialStreamDAO() {
	}
	
	public SocialStream getById(long id) {
		SocialStream socialStream = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					socialStream = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return socialStream;
	}
	
	public SocialStream getByToken(String token, long createTime) {
		SocialStream socialStream = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("token", token).append("status", SocialStreamStatus.ACTIVE.getValue())
						.append("createTime", new Document("$gt", new Date(createTime)));
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					socialStream = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getByToken ", e);
			}
		}

		return socialStream;
	}
	
	public SocialStream getByName(String name) {
		SocialStream socialStream = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("name", name).append("status", SocialStreamStatus.ACTIVE.getValue());
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					socialStream = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getByName ", e);
			}
		}

		return socialStream;
	}
	
	public SocialStream getByLastActive(int userId, long createTime) {
		SocialStream socialStream = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("userId", userId).append("status", SocialStreamStatus.ACTIVE.getValue())
						.append("createTime", new Document("$gt", new Date(createTime)));
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					socialStream = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getByLastActive ", e);
			}
		}

		return socialStream;
	}
	
	public List<SocialStream> getSliceActive(int from, int count, long createTime) {
		List<SocialStream> ss = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("status", SocialStreamStatus.ACTIVE.getValue()).append("createTime", new Document("$gt", new Date(createTime)));
				Document sort = new Document("_id", -1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort).skip(from).limit(count);
				if (doc != null) {
					ss = new MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("getSliceActive ", e);
			}
		}

		return ss;
	}

	public List<SocialStream> getByListId(List<Long> ids) {
		List<SocialStream> SocialStream = null;
		if (dbSource != null) {
			try {
				if (ids != null) {
					Document objFinder = new Document("_id", new Document("$in", ids));
					FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
					if (doc != null) {
						SocialStream = new MongoMapper().parseList(doc);
					}
				}
			} catch (final Exception e) {
				_logger.error("getByListId ", e);
			}
		}

		return SocialStream;
	}

	public Map<Long, SocialStream> getMapByListId(List<Long> ids) {
		Map<Long, SocialStream> socialStreams = new HashMap<Long, SocialStream>();
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", new Document("$in", ids));
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if (docs != null) {
					MongoMapper mapper = new MongoMapper();
					Document doc = null;
					MongoCursor<Document> cursor = docs.iterator();
					while(cursor != null && cursor.hasNext() && (doc = cursor.next()) != null) {
						SocialStream item = mapper.parseObject(doc);
						socialStreams.put(item.getId(), item);
					}
				}
			} catch (final Exception e) {
				_logger.error("getMapByListId ", e);
			}
		}

		return socialStreams;
	}

	public SocialStream getByBizCode(String bizCode) {
		SocialStream SocialStream = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("bizCode", bizCode);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					SocialStream = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getByBizCode ", e);
			}
		}

		return SocialStream;
	}

	public Map<Integer, Map<String, Object>> getByIdList(Collection<Integer> ids) {
		Map<Integer, Map<String, Object>> bizs = new HashMap<Integer, Map<String,Object>>();

		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", new Document("$in", ids));
				Document projection = new Document("name", 1).append("avtVer", 1);
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection);
				if(docs != null) {
					MongoCursor<Document> cursor = docs.iterator();
					Document doc;
					while(cursor != null && cursor.hasNext() && (doc = cursor.next()) != null) {
						Map<String, Object> biz = new HashMap<String, Object>();
						biz.put("name", doc.getString("name"));
						biz.put("avtVer", doc.getInteger("avtVer"));
						bizs.put(doc.getInteger("_id"), biz);
					}
				}
			} catch (final Exception e) {
				_logger.error("getByIdList ", e);
			}
		}

		return bizs;
	}

	public int insert(SocialStream SocialStream) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
                if(SocialStream.getId() <= 0){
                    SocialStream.setId(MIdGenLongDAO.getInstance(TABLE_NAME).getNext());
                }
				MongoMapper mapper = new MongoMapper();
				Document obj = mapper.toDocument(SocialStream);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch (final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}

	public int update(SocialStream SocialStream) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", SocialStream.getId());
				Document obj = new Document("$set", mapper.toDocument(SocialStream));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch (final Exception e) {
				_logger.error("update ", e);
			}
		}

		return rs;
	}

	public int updateStatus(long id, int status) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", id);
				Document obj = new Document("$set", new Document("status", status));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch (final Exception e) {
				_logger.error("updateStatus ", e);
			}
		}

		return rs;
	}
	
	public int deleteById(int bid) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document("_id", bid);
				DeleteResult dRs = dbSource.getCollection(TABLE_NAME).deleteOne(filter);
				rs = (int) dRs.getDeletedCount();
			} catch (final Exception e) {
				_logger.error("deleteById ", e);
			}
		}

		return rs;
	}
	
	public int addField(String field, Object defaultValue) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document();
				Document obj = new Document("$set", new Document(field, defaultValue));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateMany(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch (final Exception e) {
				_logger.error("addField ", e);
			}
		}

		return rs;
	}

	private class MongoMapper extends MongoDBParse<SocialStream> {

		@Override
		public SocialStream parseObject(Document doc) {
			SocialStream socialStream = new SocialStream(doc.getLong("_id"),
					doc.getInteger("userId"), doc.getString("name"),
					doc.getString("title"), doc.getString("token"),
					NumberUtils.toPrimitive(doc.getLong("feedId")), doc.getInteger("status"),
					doc.getDate("createTime").getTime(), doc.getDate("updateTime").getTime());
			return socialStream;
		}

		@Override
		public Document toDocument(SocialStream obj) {
			Document doc = new Document("_id", obj.getId())
					.append("userId", obj.getUserId())
					.append("name", obj.getName())
					.append("title", obj.getTitle())
					.append("token", obj.getToken())
					.append("feedId", obj.getFeedId())
					.append("status", obj.getStatus());
			return doc;
		}

	}
}
