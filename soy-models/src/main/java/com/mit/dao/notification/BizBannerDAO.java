package com.mit.dao.notification;


import java.util.Collection;
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
import com.mit.dao.mid.MIdGenLongDAO;
import com.mit.entities.notification.BannerType;
import com.mit.entities.notification.BizBanner;
import com.mit.entities.notification.BizProductBanner;
import com.mit.entities.notification.BizVideoBanner;
import com.mit.entities.notification.BizWebBanner;
import com.mit.entities.notification.BizWelcomeBanner;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class BizBannerDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(BizBannerDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static BizBannerDAO _instance;

	private String TABLE_NAME = "biz_banner";

	public static BizBannerDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new BizBannerDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}
    
    public List<BizBanner> getAllListByType(int type) {
		List<BizBanner> banners = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("type", type);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if(doc != null) {
					banners = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getByListId ", e);
			}
		}

		return banners;
	}
    
    public List<BizBanner> getAllListByTypeAndBizs(Collection bizIds, int type) {
		List<BizBanner> banners = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("type", type)
						.append("bizId", new Document("$in", bizIds));
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if(doc != null) {
					banners = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getAllListByTypeAndBizs ", e);
			}
		}

		return banners;
	}
    
    public List<BizBanner> getListByBizId(int bizId) {
		List<BizBanner> banners = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("bizId", bizId).append("status", new Document("$gt", 0));
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if (doc != null) {
					banners = new MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("getListByBizId ", e);
			}
		}
		return banners;
	}
    
    public List<BizBanner> getSlice(int bizId, int from, int size) {
		List<BizBanner> banners = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("bizId", bizId).append("status", new Document("$gt", 0));
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).skip(from).limit(size);
				if (doc != null) {
					banners = new MongoMapper().parseList(doc);
				}
			} catch (final Exception e) {
				_logger.error("getSlice ", e);
			}
		}
		return banners;
	}
    
    public Map<Integer, List<BizBanner>> getByBizIdList(List<Integer> bizIds) {
    	Map<Integer, List<BizBanner>> banners = new HashMap<Integer, List<BizBanner>>();
    	
    	for (int bizId: bizIds) {
    		banners.put(bizId, new LinkedList<BizBanner>());
    	}
    	
		if (dbSource != null) {
			try {
				Document objFinder = new Document("bizId", new Document("$in", bizIds)).append("status", new Document("$gt", 0));
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if (docs != null) {
					MongoMapper mapper = new MongoMapper();
					Document doc = null;
					MongoCursor<Document> cursor = docs.iterator();
					while(cursor != null && cursor.hasNext() && (doc = cursor.next()) != null) {
						BizBanner banner = mapper.parseObject(doc);
						banners.get(banner.getBizId()).add(banner);
					}
				}
			} catch (final Exception e) {
				_logger.error("getByBizIdList ", e);
			}
		}
		return banners;
	}
    
	public List<BizBanner> getByListId(List<Long> ids) {
		List<BizBanner> banners = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", new Document("$in", ids)).append("status", new Document("$gt", 0));
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if(doc != null) {
					banners = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getByListId ", e);
			}
		}

		return banners;
	}
	
	public Map<Long, BizBanner> getMapByListId(List<Long> ids) {
		Map<Long, BizBanner> banners = new HashMap<Long, BizBanner>();
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", new Document("$in", ids)).append("status", new Document("$gt", 0));
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if(docs != null) {
					MongoMapper mapper = new MongoMapper();
					Document doc = null;
					MongoCursor<Document> cursor = docs.iterator();
					while(cursor != null && cursor.hasNext() && (doc = cursor.next()) != null) {
						BizBanner newz = mapper.parseObject(doc);
						banners.put(newz.getuId(), newz);
					}
				}
			} catch(final Exception e) {
				_logger.error("getMapByListId ", e);
			}
		}

		return banners;
	}

	public BizBanner getById(long id) {
		BizBanner banner= null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", id);
				Document doc = dbSource.getCollection(TABLE_NAME).find(objFinder).first();
				if(doc != null) {
					banner= new MongoMapper().parseObject(doc);
				}
			} catch(final Exception e) {
				_logger.error("getById ", e);
			}
		}

		return banner;
	}
    
	public int insert(BizBanner banner) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				if(banner.getuId() <= 0) {
					banner.setuId(MIdGenLongDAO.getInstance(TABLE_NAME).getNext());
				}
				Document obj = mapper.toDocument(banner);
				obj = mapper.buildInsertTime(obj);
				dbSource.getCollection(TABLE_NAME).insertOne(obj);
				rs = MongoErrorCode.SUCCESS;
			} catch(final Exception e) {
				_logger.error("insert ", e);
			}
		}

		return rs;
	}

	public int update(BizBanner banner) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				MongoMapper mapper = new MongoMapper();
				Document filter = new Document("_id", banner.getuId());
				Document obj = new Document("$set", mapper.toDocument(banner));
				obj = mapper.buildUpdateTime(obj);
				UpdateResult qRs = dbSource.getCollection(TABLE_NAME).updateOne(filter, obj);
				rs = (int) qRs.getModifiedCount();
			} catch(final Exception e) {
				_logger.error("update ", e);
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
	
	public int hardDelete(long id) {
		int rs = MongoErrorCode.NOT_CONNECT;
		if(dbSource != null) {
			try {
				Document filter = new Document("_id", id);
				DeleteResult qRs = dbSource.getCollection(TABLE_NAME).deleteOne(filter);
				rs = (int) qRs.getDeletedCount();
			} catch(final Exception e) {
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

	private class MongoMapper extends MongoDBParse<BizBanner> {

		@Override
		public BizBanner parseObject(Document doc) {
			long uId = doc.getLong("_id");
			int bizId = doc.getInteger("bizId");
			int type = doc.getInteger("type");
			int status = doc.getInteger("status");
			
			BizBanner banners = null;
			if (type == BannerType.WEB.getValue()) {
				banners = new BizWebBanner(uId, bizId, doc.getString("id"), 
						doc.getString("msg"), doc.getLong("thumb"), 
						status);
			} else if (type == BannerType.PRODUCT.getValue()) {
				banners = new BizProductBanner(uId, bizId, doc.getLong("bannerId"), 
						doc.getLong("skuId"), doc.getString("msg"), 
						doc.getLong("thumb"), status);
			} else if (type == BannerType.VIDEO.getValue()) {
				banners = new BizVideoBanner(uId, bizId, doc.getString("id"), 
						doc.getString("msg"), doc.getLong("thumb"), 
						status);
			} else if (type == BannerType.WELCOME.getValue()) {
				banners = new BizWelcomeBanner(uId, bizId, doc.getString("msg"), 
						doc.getLong("thumb"), status);
			}
			
			return banners;
		}

		@Override
		public Document toDocument(BizBanner obj) {			
			Document doc = new Document("_id", obj.getuId())
				.append("bizId", obj.getBizId())
				.append("type", obj.getType())
				.append("status", obj.getStatus());

			if (obj instanceof BizWebBanner) {
				BizWebBanner webBanner = (BizWebBanner)obj;
				doc.append("id", webBanner.getId())
					.append("msg", webBanner.getMsg())
					.append("thumb", webBanner.getThumb());
			} else if (obj instanceof BizProductBanner) {
				BizProductBanner bannerBanner = (BizProductBanner)obj;
				doc.append("bannerId", bannerBanner.getProductId())
					.append("skuId", bannerBanner.getSkuId())
					.append("msg", bannerBanner.getMsg())
					.append("thumb", bannerBanner.getThumb());
			} else if (obj instanceof BizVideoBanner) {
				BizVideoBanner videoBanner = (BizVideoBanner)obj;
				doc.append("id", videoBanner.getId())
					.append("msg", videoBanner.getMsg())
					.append("thumb", videoBanner.getThumb());
			} else if (obj instanceof BizWelcomeBanner) {
				BizWelcomeBanner welcomeBanner = (BizWelcomeBanner)obj;
				doc.append("msg", welcomeBanner.getMsg())
					.append("thumb", welcomeBanner.getThumb());
			}
			
			return doc;
		}

	}
}
