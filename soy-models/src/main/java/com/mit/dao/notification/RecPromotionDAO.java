package com.mit.dao.notification;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
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
import com.mit.entities.notification.RecPromotion;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class RecPromotionDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(RecPromotionDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static RecPromotionDAO _instance;

	public final String TABLE_NAME = "rec_promotion";

	public static RecPromotionDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new RecPromotionDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}

	private RecPromotionDAO() {
	}
	
	public int addItem(int userId, RecPromotion item) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {				
				Document objFinder = new Document("_id", userId);
				Document update = new Document("$push", new Document("recPromotions", new Document("$each", Arrays.asList(new MongoMapper().toDocument(item)))
					.append("$position", 0)));
				UpdateResult doc = dbSource.getCollection(TABLE_NAME).updateOne(objFinder, update, new UpdateOptions().upsert(true));

				rs = (int)doc.getModifiedCount() + (doc.getUpsertedId() != null ? 1 : 0);
				
				if(rs > 0) {
					update = new Document("$inc", new Document("newCount", rs));
					doc = dbSource.getCollection(TABLE_NAME).updateOne(objFinder, update);
				}
			} catch(final Exception e) {
				_logger.error("addItem ", e);
			}
		}
		return rs;
	}
	
	public Map<Integer, RecPromotion> getItems(int userId, int from, int count) {
		Map<Integer, RecPromotion> recPromos = new LinkedHashMap<Integer, RecPromotion>();
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", userId);
				Document projection = new Document("recPromotions", new Document("$slice", Arrays.asList(from, count)));
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).projection(projection).first();
				if(doc != null && !doc.isEmpty()) {
					MongoMapper mapper = new MongoMapper();			
					List<Document> itemDocs = (List)doc.get("recPromotions"); 					
					for (Document itemDoc: itemDocs) {
						RecPromotion recPromo = mapper.parseObject(itemDoc);
						recPromos.put(recPromo.getId(), recPromo);
					}
				}
			} catch(final Exception e) {
				_logger.error("getItems ", e);
			}
		}
		return recPromos;
	}
	
	public int countNewItems(int userId) {
		int total = 0;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", userId);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null && !doc.isEmpty()) {
					total = doc.getInteger("newCount", 0);
				}
			} catch(final Exception e) {
				_logger.error("countNewItems ", e);
			}
		}
		return total;
	}
	
	public int clearItems(long fromTime) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document objFinder = new Document();
				Document update = new Document("$pull", new Document("recPromotions", new Document("createTime", new Document("$lt", new Date(fromTime)))));
				UpdateResult doc = dbSource.getCollection(TABLE_NAME).updateOne(objFinder, update);

				rs = (int)doc.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("removeItems ", e);
			}
		}
		return rs;
	}
	
	public int updateView(int userId, List<Integer> promotionIds) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				List<WriteModel<Document>> updateModels = new LinkedList<WriteModel<Document>>();

				Document update = new Document("$set", new Document("recPromotions.$.viewed", true));
				for (int promotionId: promotionIds) {
					Document objFinder = new Document("_id", userId).append("recPromotions.id", promotionId);
					updateModels.add(new UpdateOneModel<Document>(objFinder, update));
				}
				
				BulkWriteResult doc = dbSource.getCollection(TABLE_NAME).bulkWrite(updateModels);

				rs = doc.getModifiedCount();
				
				if(rs > 0) {
					Document objFinder = new Document("_id", userId);
					update = new Document("$inc", new Document("newCount", -rs));
					dbSource.getCollection(TABLE_NAME).updateOne(objFinder, update);
				}
			} catch(final Exception e) {
				_logger.error("updateView ", e);
			}
		}
		return rs;
	}
	
	public int truncate() {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document();
				DeleteResult qRs = dbSource.getCollection(TABLE_NAME).deleteMany(filter);
				rs = (int) qRs.getDeletedCount();
			} catch(final Exception e) {
				_logger.error("truncate ", e);
			}
		}

		return rs;
	}
	
	private class MongoMapper extends MongoDBParse<RecPromotion> {
		@Override
		public RecPromotion parseObject(Document doc) {
			RecPromotion account = new RecPromotion(
					doc.getInteger("id"),
					doc.getBoolean("viewed", false),
					doc.getDate("createTime").getTime());
			return account;
		}

		@Override
		public Document toDocument(RecPromotion obj) {
			Document doc = new Document("id", obj.getId())
					.append("viewed", obj.isViewed())
					.append("createTime", new Date());
			return doc;
		}
	}
	
	public static void main(String[] args) {
		int userId = 1;
		RecPromotionDAO.getInstance().truncate();
		RecPromotion item = new RecPromotion(1);
		RecPromotion item2 = new RecPromotion(2);
		RecPromotion item3 = new RecPromotion(3);
		RecPromotion item4 = new RecPromotion(4);
		RecPromotionDAO.getInstance().addItem(userId, item);
		RecPromotionDAO.getInstance().addItem(userId, item2);
		RecPromotionDAO.getInstance().addItem(userId, item3);
		RecPromotionDAO.getInstance().addItem(userId, item4);
		
		System.out.println(RecPromotionDAO.getInstance().countNewItems(userId));
		
		int rs = RecPromotionDAO.getInstance().updateView(userId, Arrays.asList(4, 3));
//		System.out.println(rs);
		
		System.out.println(RecPromotionDAO.getInstance().countNewItems(userId));
		
//		int page = 1;
//		int count = 4;
//		int from = (page - 1) * count;
//		Map<Integer, RecPromotion> items = RecPromotionDAO.getInstance().getItems(userId, from, count);
//		System.out.println(JsonUtils.Instance.toJson(items));
//		
//		System.out.println(RecPromotionDAO.getInstance().countNewItems(userId));
		
//		page = 2;
//		from = (page - 1) * count;
//		items = RecPromotionDAO.getInstance().getItems(userId, from, count);
//		System.out.println(JsonUtils.Instance.toJson(items));
//
//		page = 3;
//		from = (page - 1) * count;
//		items = RecPromotionDAO.getInstance().getItems(userId, from, count);
//		System.out.println(JsonUtils.Instance.toJson(items));
//		
//		page = 4;
//		from = (page - 1) * count;
//		items = RecPromotionDAO.getInstance().getItems(userId, from, count);
//		System.out.println(JsonUtils.Instance.toJson(items));
		
		
//		List<RecPromotion> items = RecPromotionDAO.getInstance().getItems(userId, 0, 10);
//		System.out.println(JsonUtils.Instance.toJson(items));
	}
}
