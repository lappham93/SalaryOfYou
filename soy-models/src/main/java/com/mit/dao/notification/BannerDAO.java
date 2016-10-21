package com.mit.dao.notification;


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
import com.mit.entities.notification.Banner;
import com.mit.entities.notification.BannerType;
import com.mit.entities.notification.BoothBanner;
import com.mit.entities.notification.EventBanner;
import com.mit.entities.notification.VideoBanner;
import com.mit.entities.notification.ProductBanner;
import com.mit.entities.notification.WebBanner;
import com.mit.entities.notification.WelcomeBanner;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class BannerDAO extends CommonDAO {
	private final Logger _logger = LoggerFactory.getLogger(BannerDAO.class);
	private static Lock _lock = new ReentrantLock();
	private static BannerDAO _instance;

	private String TABLE_NAME = "banner";

	public static BannerDAO getInstance() {
		if(_instance == null) {
			_lock.lock();
			try {
				if(_instance == null) {
					_instance = new BannerDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}
    
    public List<Banner> getAllListByType(int type, int ownType) {
		List<Banner> banners = null;
		if(dbSource != null) {
			try {
				Document objFinder = new Document("type", type)
						.append("ownType", ownType);
				Document sort = new Document("updateTime", -1);
				FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder).sort(sort);
				if(doc != null) {
					banners = new MongoMapper().parseList(doc);
				}
			} catch(final Exception e) {
				_logger.error("getByListId ", e);
			}
		}

		return banners;
	}
    
    public List<Banner> getAllListByTypeAndSkuIds(int type, List<Long> skuIds) {
		List<Banner> banners = null;
		if(dbSource != null) {
			try {
				if (skuIds != null) {
					Document objFinder = new Document("type", type)
							.append("skuId", new Document("$in", skuIds));
					FindIterable<Document> doc = dbSource.getCollection(TABLE_NAME).find(objFinder);
					if(doc != null) {
						banners = new MongoMapper().parseList(doc);
					}
				}
			} catch(final Exception e) {
				_logger.error("getByListId ", e);
			}
		}

		return banners;
	}
    
    public List<Banner> getSlice(long ownId, int ownType, int from, int size) {
		List<Banner> banners = null;
		if (dbSource != null) {
			try {
				Document objFinder = new Document("status", new Document("$gt", 0))
						.append("ownId", ownId)
						.append("ownType", ownType);
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
    
	public List<Banner> getByListId(List<Long> ids) {
		List<Banner> banners = null;
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
	
	public Map<Long, Banner> getMapByListId(List<Long> ids) {
		Map<Long, Banner> banners = new HashMap<Long, Banner>();
		if(dbSource != null) {
			try {
				Document objFinder = new Document("_id", new Document("$in", ids)).append("status", new Document("$gt", 0));
				FindIterable<Document> docs = dbSource.getCollection(TABLE_NAME).find(objFinder);
				if(docs != null) {
					MongoMapper mapper = new MongoMapper();
					Document doc = null;
					MongoCursor<Document> cursor = docs.iterator();
					while(cursor != null && cursor.hasNext() && (doc = cursor.next()) != null) {
						Banner newz = mapper.parseObject(doc);
						banners.put(newz.getuId(), newz);
					}
				}
			} catch(final Exception e) {
				_logger.error("getMapByListId ", e);
			}
		}

		return banners;
	}

	public Banner getById(long id) {
		Banner banner= null;
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
    
	public int insert(Banner banner) {
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

	public int update(Banner banner) {
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

	private class MongoMapper extends MongoDBParse<Banner> {

		@Override
		public Banner parseObject(Document doc) {
			long uId = doc.getLong("_id");
			long ownId = doc.getLong("ownId");
			int ownType = doc.getInteger("ownType");
			int type = doc.getInteger("type");
			int status = doc.getInteger("status");
			
			Banner banners = null;
			if (type == BannerType.WEB.getValue()) {
				banners = new WebBanner(uId, doc.getString("id"), ownId, ownType,
						doc.getString("msg"), doc.getLong("thumb"), 
						status);
			} else if (type == BannerType.PRODUCT.getValue()) {
				banners = new ProductBanner(uId, doc.getLong("productId"),
						doc.getLong("skuId"), ownId, ownType, doc.getString("msg"), 
						doc.getLong("thumb"), status);
			} else if (type == BannerType.VIDEO.getValue()) {
				banners = new VideoBanner(uId, doc.getString("id"), ownId, ownType,
						doc.getString("msg"), doc.getLong("thumb"), 
						status);
			} else if (type == BannerType.WELCOME.getValue()) {
				banners = new WelcomeBanner(uId, ownId, ownType, doc.getString("msg"), 
						doc.getLong("thumb"), status);
			} else if (type == BannerType.EVENT.getValue()) {
				banners = new EventBanner(uId, doc.getLong("eventId"), ownId, ownType, 
						doc.getString("msg"), doc.getLong("thumb"), status);
			} else if (type == BannerType.BOOTH.getValue()) {
				banners = new BoothBanner(uId, doc.getLong("eventId"), doc.getLong("boothId"), 
						ownId, ownType, doc.getString("msg"), doc.getLong("thumb"), status);
			}
			
			return banners;
		}

		@Override
		public Document toDocument(Banner obj) {			
			Document doc = new Document("_id", obj.getuId())
				.append("ownId", obj.getOwnId())
				.append("ownType", obj.getOwnType())
				.append("type", obj.getType())
				.append("status", obj.getStatus());

			if (obj instanceof WebBanner) {
				WebBanner webBanner = (WebBanner)obj;
				doc.append("id", webBanner.getId())
					.append("msg", webBanner.getMsg())
					.append("thumb", webBanner.getThumb());
			} else if (obj instanceof ProductBanner) {
				ProductBanner bannerBanner = (ProductBanner)obj;
				doc.append("productId", bannerBanner.getProductId())
					.append("skuId", bannerBanner.getSkuId())
					.append("msg", bannerBanner.getMsg())
					.append("thumb", bannerBanner.getThumb());
			} else if (obj instanceof VideoBanner) {
				VideoBanner videoBanner = (VideoBanner)obj;
				doc.append("id", videoBanner.getId())
					.append("msg", videoBanner.getMsg())
					.append("thumb", videoBanner.getThumb());
			} else if (obj instanceof WelcomeBanner) {
				WelcomeBanner welcomeBanner = (WelcomeBanner)obj;
				doc.append("msg", welcomeBanner.getMsg())
					.append("thumb", welcomeBanner.getThumb());
			} else if (obj instanceof EventBanner) {
				EventBanner eventBanner = (EventBanner) obj;
				doc.append("eventId", eventBanner.getEventId())
					.append("msg", eventBanner.getMsg())
					.append("thumb", eventBanner.getThumb());
			} else if (obj instanceof BoothBanner) {
				BoothBanner boothBanner = (BoothBanner) obj;
				doc.append("eventId", boothBanner.getEventId())
					.append("boothId", boothBanner.getBoothId())
					.append("msg", boothBanner.getMsg())
					.append("thumb", boothBanner.getThumb());
			}
			
			return doc;
		}

	}
}
