package com.mit.dao.react;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.MongoErrorCode;
import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.dao.mid.MIdGenLongDAO;
import com.mit.entities.react.Comment;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class CommentDAO extends CommonDAO {

	private final Logger _logger = LoggerFactory.getLogger(CommentDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static CommentDAO _instance;

	private String TABLE_NAME = "comment";

	public static CommentDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new CommentDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private CommentDAO() {
	}

	public String getTableName() {
		return this.TABLE_NAME;
	}

	public long getTotal() {
		long rs = 0;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("status", new Document("$gt", 0));
				rs = dbSource.getCollection(TABLE_NAME).count(objFinder);
			} catch (final Exception e) {
				_logger.error("getTotal ", e);
			}
		}

		return rs;
	}
	
	public long getTotal(int objectType, long objectId) {
		long rs = 0;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("objectType", objectType)
						.append("objectId", objectId)
						.append("status", new Document("$gt", 0));
				rs = dbSource.getCollection(TABLE_NAME).count(objFinder);
			} catch (final Exception e) {
				_logger.error("getTotal ", e);
			}
		}

		return rs;
	}

	public List<Comment> getSlideComment(int objectType, long objectId, int from, int count,
			String fieldSort, int sortOption) {
		List<Comment> comments = null;
		if (dbSource != null) {
			try {
				Document filter = new Document("objectType", objectType)
						.append("objectId", objectId)
						.append("status", new Document("$gt", 0));
				Document sort = new Document(fieldSort, sortOption);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort).skip(from)
						.limit(count);
				if (doc != null) {
					comments = new MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("getSlideComment ", e);
			}
		}

		return comments;
	}
	
	public List<Long> getUserCmt(int objectType, long objectId) {
		List<Long> rs = null;
		if (dbSource != null) {
			try {
				Document filter = new Document("objectType", objectType)
						.append("objectId", objectId)
						.append("status", new Document("$gt", 0));
				Document sort = new Document("createTime", 1);
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort);
				if (docs != null) {
					rs = new ArrayList<Long>();
					MongoCursor<Document> tmp = docs.iterator();
					while(tmp.hasNext()) {
						Long userId = tmp.next().getLong("userId");
						if (!rs.contains(userId)) {
							rs.add(userId);
						}
					}
				}
				
			} catch (Exception e) {
				_logger.error("getUserCmt ", e);
			}
		}
		
		return rs;
	}

	public Comment getById(long id) {
		Comment comment = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if (doc != null) {
					comment = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return comment;
	}
	
	public Comment getLast(int objectType, long objectId) {
		Comment comments = null;
		if (dbSource != null) {
			try {
				Document filter = new Document("objectType", objectType)
						.append("objectId", objectId)
						.append("status", new Document("$gt", 0));
				Document sort = new Document("createTime", -1);
				Document doc = dbSource.getCollection(TABLE_NAME).find(filter).sort(sort).first();
				if (doc != null) {
					comments = new MongoMapper().parseObject(doc);
				}
			} catch (final Exception e) {
				_logger.error("getLast ", e);
			}
		}

		return comments;
	}
	
	public int addChildComment(long parentComment, long childComment) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document("_id", parentComment);
				Document update = new Document("$inc", new Document("totalChild", 1));
				UpdateResult ur = dbSource.getCollection(TABLE_NAME).updateOne(filter, update);
				rs = (int) ur.getModifiedCount();
			} catch (Exception e) {
				_logger.error("addChildComment ", e);
			}
		}
		
		return rs;
	}
	
	public int countChildComment(long parentComment) {
		int rs = 0;
		if (dbSource != null) {
			try {
				Document filter = new Document("_id", parentComment);
				Document doc = dbSource.getCollection(TABLE_NAME).find(filter).first();
				if (doc != null) {
					rs = doc.getInteger("totalChild");
				}
			} catch (Exception e) {
				_logger.error("countChildComment ", e);
			}
		}
		
		return rs;
	}
	
	public int deleteChildComment(long parentComment, long childComment) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document("_id", parentComment);
				Document update = new Document("$inc", new Document("totalChild", -1));
				UpdateResult ur = dbSource.getCollection(TABLE_NAME).updateOne(filter, update);
				rs = (int) ur.getModifiedCount();
			} catch (Exception e) {
				_logger.error("deleteChildComment ", e);
			}
		}
		
		return rs;
	}

	public int insert(Comment msg) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				if (msg.getId() <= 0) {
					msg.setId(MIdGenLongDAO.getInstance(TABLE_NAME).getNext());
				}
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

	public Comment insertGet(Comment msg) {
		Comment rs = null;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				if (msg.getId() <= 0) {
					msg.setId(MIdGenLongDAO.getInstance(TABLE_NAME).getNext());
				}
				Document obj = mapper.toDocument(msg);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = mapper.parseObject(obj);
			} catch (final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}

	public int update(Comment msg) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", msg.getId());
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

	public Comment updateGet(Comment msg) {
		Comment rs = null;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", msg.getId());
				Document obj = new Document("$set", mapper.toDocument(msg));
				obj = mapper.buildUpdateTime(obj);
				dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = mapper.parseObject(obj);
			} catch (final Exception e) {
				_logger.error("update ", e);
			}
		}

		return rs;
	}

	public int delete(long id) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", id);
				Document obj = new Document("$set", new Document("status", 0));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch (final Exception e) {
				_logger.error("delete ", e);
			}
		}

		return rs;
	}

	public int delete(List<Long> ids) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", new Document("$in", ids));
				Document obj = new Document("$set", new Document("status", 0));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateMany(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch (final Exception e) {
				_logger.error("delete ", e);
			}
		}

		return rs;
	}

	public int hardDelete(long id) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if (dbSource != null) {
			try {
				Document filter = new Document("_id", id);
				DeleteResult qRs = dbSource.getCollection(TABLE_NAME).deleteOne(filter);
				rs = (int) qRs.getDeletedCount();
			} catch (final Exception e) {
				_logger.error("hardDelete ", e);
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

	private class MongoMapper extends MongoDBParse<Comment> {

		@Override
		public Comment parseObject(Document doc) {
			Comment cmt = new Comment(doc.getInteger("objectType"), doc.getLong("objectId"), doc.getLong("_id"),
					doc.getLong("userId"), doc.getString("text"), doc.getString("sticker"), doc.getLong("photoCmtId"),
					doc.getInteger("totalChild"), doc.getInteger("status"), doc.getDate("createTime").getTime(), doc.getDate("updateTime").getTime());

			return cmt;
		}

		@Override
		public Document toDocument(Comment obj) {
			Document doc = new Document("_id", obj.getId()).append("objectType", obj.getObjectType())
					.append("objectId", obj.getObjectId()).append("userId", obj.getUserId())
					.append("text", obj.getText()).append("sticker", obj.getSticker())
					.append("photoCmtId", obj.getPhotoCmtId()).append("totalChild", obj.getTotalChild())
					.append("status", obj.getStatus());

			return doc;
		}

	}

}
