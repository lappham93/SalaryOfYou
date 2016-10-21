package com.mit.dao.upload;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.dao.CommonDAO;
import com.mit.dao.MongoDBParse;
import com.mit.constants.MongoErrorCode;
import com.mit.dao.mid.MIdGenLongDAO;
import com.mit.entities.upload.UploadTemp;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.result.UpdateResult;

public class UploadTempDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(UploadTempDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static UploadTempDAO _instance;

	private final String TABLE_NAME = "upload_temp";

	public static UploadTempDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new UploadTempDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private UploadTempDAO() {
		if(dbSource != null) {
			ListIndexesIterable<Document> listIndex = dbSource.getCollection(TABLE_NAME).listIndexes();
			if(listIndex != null && listIndex.first() != null) {
				dbSource.getCollection(TABLE_NAME).createIndex(new Document("updateTime", -1), new IndexOptions().expireAfter(3600l, TimeUnit.SECONDS));
			}
		}
	}

	public UploadTemp getById(long id) {
		UploadTemp feedItem = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					feedItem = new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return feedItem;
	}

	public UploadTemp getByIdNotData(long id) {
		UploadTemp item = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document projection = new Document("data", 0);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection).first();
				if(doc != null) {
					item = new UploadTemp(doc.getLong("_id"), doc.getInteger("type"), doc.getInteger("total"), doc.getInteger("size"), 
                            doc.getInteger("totalComplete"), doc.getInteger("sizeComplete"), (List<Boolean>)doc.get("check"));
				}
			} catch(final Exception e) {
				_logger.error("isExist ", e);
			}
		}

		return item;
	}

	public int insert(UploadTemp msg) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				if(msg.getId() <= 0) {
					msg.setId(MIdGenLongDAO.getInstance(TABLE_NAME).getNext());
				}
				MongoMapper mapper = new MongoMapper();
				Document obj = mapper.toDocument(msg);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}

	public int updateData(long id, int index, byte[] data) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", id);
				Document obj = new Document("$set", new Document("data." + index, new Binary(data))
					.append("check." + index, true)).append("$inc", new Document("totalComplete", 1).append("sizeComplete", data.length));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("updateData ", e);
			}
		}

		return rs;
	}

	private class MongoMapper extends MongoDBParse<UploadTemp> {

		@Override
		public UploadTemp parseObject(Document doc) {
			UploadTemp item = new UploadTemp(doc.getLong("_id"), doc.getInteger("type"), doc.getInteger("total"), doc.getInteger("size"), doc.getInteger("totalComplete"), doc.getInteger("sizeComplete")
					,(List<Boolean>)doc.get("check"));

			if(doc.get("data") instanceof List) {
				List<Binary> listBinary = (List<Binary>) doc.get("data");
				List<byte[]> data = new ArrayList<byte[]>(listBinary.size());
				for(Binary b : listBinary) {
					data.add(b.getData());
				}
				item.setData(data);
			}

			return item;
		}

		@Override
		public Document toDocument(UploadTemp obj) {
			Document doc = new Document("_id", obj.getId()).append("type", obj.getType()).append("total", obj.getTotal()).append("size", obj.getSize())
					.append("data", obj.getData()).append("check", obj.getCheck()).append("totalComplete", obj.getTotalComplete()).append("sizeComplete", obj.getSizeComplete());
			return doc;
		}
	}
}
