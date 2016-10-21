///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package com.mit.models;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import org.apache.commons.lang.math.NumberUtils;
//import org.eclipse.jetty.client.api.ContentResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import scala.collection.mutable.StringBuilder;
//
//import com.google.i18n.phonenumbers.NumberParseException;
//import com.google.i18n.phonenumbers.PhoneNumberUtil;
//import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
//import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
//import com.mit.dao.address.HiringAddressDAO;
//import com.mit.dao.mid.MIdGenLongDAO;
//import com.mit.entities.address.HiringAddress;
//import com.mit.es.LBSQuotaRedis;
//import com.mit.http.HttpsRequest;
//import com.mit.utils.ConfigUtils;
//import com.mit.utils.JsonUtils;
//
///**
// *
// * @author truyetnm
// */
//public class FoursquareModel {
//	private static Logger logger = LoggerFactory.getLogger(FoursquareModel.class);
//
//	private static final String redisKey = "foursquare";
//	private static String API_URL = ConfigUtils.getConfig().getString("foursquare.api.url", "");
//	private static String VENUE_DETAIL_URL = ConfigUtils.getConfig().getString("foursquare.api.venue.url", "https://api.foursquare.com/v2/venues/");
//	private static String CLIENT_ID = ConfigUtils.getConfig().getString("foursquare.api.clientid", "");
//	private static String SECRET_KEY = ConfigUtils.getConfig().getString("foursquare.api.secret", "");
//	public static int C_QUOTA = ConfigUtils.getConfig().getInt("foursquare.api.quota", 0);
//	public static int TIME_OUT = ConfigUtils.getConfig().getInt("foursquare.api.timeout", 0);
//	private static AtomicInteger QUOTA = null;//new AtomicInteger(LBSQuotaRedis.Instance.getQuota(redisKey, C_QUOTA, TIME_OUT));
//
//	private static boolean _outQuota = false;
//	private static long _resetTime = 0;
//
//	public static final int OK = 200;
//	public static final int BAD_REQUEST = 400;
//	public static final int UNAUTHORIZED = 401;
//	public static final int FORBIDDEN = 403;
//	public static final int NOT_FOUND = 404;
//	public static final int METHOD_NOT_ALLOW = 405;
//	public static final int CONFLICT = 409;
//	public static final int INTERNAL_ERROR = 500;
//
//	public static final int MAX_LIMIT = 50;
//
//	public static List<HiringAddress> getData(double lon, double lat) {
//		return getData(lon, lat, "", MAX_LIMIT);
//	}
//
//	public static List<HiringAddress> getData(double lon, double lat, String query, int limit) {
//		List<HiringAddress> geoPlaces = new LinkedList<HiringAddress>();
//		if(!_outQuota) {
//			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//			StringBuilder builder = new StringBuilder(API_URL + "limit=" + limit);
//			//4bf58dd8d48988d11f941735,4bf58dd8d48988d11b941735,4bf58dd8d48988d121941735,4bf58dd8d48988d116941735,52e81612bcbc57f1066b7a0d
//			builder.append("&categoryId=4bf58dd8d48988d11f941735,4bf58dd8d48988d11b941735,4bf58dd8d48988d121941735,4bf58dd8d48988d116941735,52e81612bcbc57f1066b7a0d");
//			builder.append("&ll=" + lat + "," + lon);
//
//			if(query != null && !query.isEmpty()) {
//				builder.append("&query=" + query);
//			}
//
//			builder.append("&client_id=" + CLIENT_ID);
//			builder.append("&client_secret=" + SECRET_KEY);
//			builder.append("&v=" + format.format(Calendar.getInstance().getTime()));
//			//geoPlaces = getFromLink(builder.toString());
//			geoPlaces = getFromLink(buildLink(lat, lon, 0, "", limit));
//		} else {
//			long time = System.currentTimeMillis() - _resetTime;
//			if(time > 0) {
//				_outQuota = false;
//				_resetTime = 0;
//				if(QUOTA.get() >= C_QUOTA) {
//					QUOTA.set(LBSQuotaRedis.Instance.getQuota(redisKey, C_QUOTA, TIME_OUT));
//				}
//			}
//		}
//
//		return geoPlaces;
//	}
//
//	public static List<HiringAddress> getData(double lon, double lat, int radius, int limit) {
//		List<HiringAddress> geoPlaces = new LinkedList<HiringAddress>();
//		if(!_outQuota) {
//			geoPlaces = getFromLink(buildLink(lat, lon, radius, "", limit));
//		} else {
//			long time = System.currentTimeMillis() - _resetTime;
//			if(time > 0) {
//				_outQuota = false;
//				_resetTime = 0;
//				if(QUOTA.get() >= C_QUOTA) {
//					QUOTA.set(LBSQuotaRedis.Instance.getQuota(redisKey, C_QUOTA, TIME_OUT));
//				}
//			}
//		}
//
//		return geoPlaces;
//	}
//
//	public static List<HiringAddress> getData(String near, String query, int limit) {
//		List<HiringAddress> geoPlaces = new LinkedList<HiringAddress>();
//		if(!_outQuota) {
//			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//			StringBuilder builder = new StringBuilder(API_URL + "limit=" + limit);
//			builder.append("&categoryId=4bf58dd8d48988d11f941735,4bf58dd8d48988d11b941735,4bf58dd8d48988d121941735,4bf58dd8d48988d116941735,52e81612bcbc57f1066b7a0d");
//			builder.append("&near=" + near);
//
//			if(query != null && !query.isEmpty()) {
//				builder.append("&query=" + query);
//			}
//
//			builder.append("&client_id=" + CLIENT_ID);
//			builder.append("&client_secret=" + SECRET_KEY);
//			builder.append("&v=" + format.format(Calendar.getInstance().getTime()));
//			geoPlaces = getFromLink(builder.toString());
//
//		} else {
//			long time = System.currentTimeMillis() - _resetTime;
//			if(time > 0) {
//				_outQuota = false;
//				_resetTime = 0;
//				if(QUOTA.get() >= C_QUOTA) {
//					QUOTA.set(LBSQuotaRedis.Instance.getQuota(redisKey, C_QUOTA, TIME_OUT));
//				}
//			}
//		}
//
//		return geoPlaces;
//	}
//
//	public static String buildNoAuthenLink(double lat, double lon, double radius, String query, int limit) {
//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//		StringBuilder builder = new StringBuilder(API_URL + "limit=" + limit);
//		//builder.append("&categoryId=4bf58dd8d48988d11f941735,4bf58dd8d48988d11b941735,4bf58dd8d48988d121941735,4bf58dd8d48988d116941735,52e81612bcbc57f1066b7a0d");
//		builder.append("&ll=" + lat + "," + lon);
//
//		if (radius > 0) {
//			builder.append("&radius=" + radius);
//		}
//
//		if(query != null && !query.isEmpty()) {
//			try {
//				builder.append("&query=" + URLEncoder.encode(query, StandardCharsets.UTF_8.toString()));
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else {
//			builder.append("&categoryId=" + ConfigUtils.getConfig().getString("crawler.categoryId", "4bf58dd8d48988d11f941735,4bf58dd8d48988d11b941735,4bf58dd8d48988d121941735,4bf58dd8d48988d116941735,52e81612bcbc57f1066b7a0d"));
//		}
//
//		return builder.toString();
//	}
//
//	public static String buildAuthenLink(String url) {
//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//		StringBuilder builder = new StringBuilder(url);
//		builder.append(((url.contains("?")) ? "&" : "?") + "client_id=" + CLIENT_ID);
//		builder.append("&client_secret=" + SECRET_KEY);
//		builder.append("&v=" + format.format(Calendar.getInstance().getTime()));
//
//		return builder.toString();
//	}
//	
//	public static String buildAuthenLink(String url, String clientId, String secretKey) {
//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//		StringBuilder builder = new StringBuilder(url);
//		builder.append(((url.contains("?")) ? "&" : "?") + "client_id=" + clientId);
//		builder.append("&client_secret=" + secretKey);
//		builder.append("&v=" + format.format(Calendar.getInstance().getTime()));
//
//		return builder.toString();
//	}
//
//	public static String buildLink(double lat, double lon, double radius, String query, int limit) {
//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//		StringBuilder builder = new StringBuilder(API_URL + "limit=" + limit);
//		//builder.append("&categoryId=4bf58dd8d48988d11f941735,4bf58dd8d48988d11b941735,4bf58dd8d48988d121941735,4bf58dd8d48988d116941735,52e81612bcbc57f1066b7a0d");
//		builder.append("&ll=" + lat + "," + lon);
//
//		if (radius > 0) {
//			builder.append("&radius=" + radius);
//		}
//
//		if(query != null && !query.isEmpty()) {
//			try {
//				builder.append("&query=" + URLEncoder.encode(query, StandardCharsets.UTF_8.toString()));
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else {
//			builder.append("&categoryId=" + ConfigUtils.getConfig().getString("crawler.categoryId", "4bf58dd8d48988d11f941735,4bf58dd8d48988d11b941735,4bf58dd8d48988d121941735,4bf58dd8d48988d116941735,52e81612bcbc57f1066b7a0d"));
//		}
//
//		builder.append("&client_id=" + CLIENT_ID);
//		builder.append("&client_secret=" + SECRET_KEY);
//		builder.append("&v=" + format.format(Calendar.getInstance().getTime()));
//		return builder.toString();
//	}
//
//	public static String buildVenueDetailNoAuthenLink(String refId) {
//		return VENUE_DETAIL_URL + refId;
//	}
//
//	public static Map<String, Object> parseContent(byte[] content) {
//		return parseContent(content, 0, false);
//	}
//
//	public static Map<String, Object> parseContent(byte[] content, int minCheckinsCount, boolean nullable) {
//		Map<String, Object> rs = new HashMap<String, Object>();
//		List<HiringAddress> geoPlaces = new LinkedList<HiringAddress>();
//
//		Map<String, Object> data = JsonUtils.Instance.getMapObject(content);
//		if(data != null) {
//			if(data.get("meta") instanceof Map) {
//				Map<String, Object> meta = (Map<String, Object>)data.get("meta");
//				int status = NumberUtils.toInt(String.valueOf(meta.get("code")));
//				rs.put("status", status);
//				switch(status) {
//					case OK:
//						if(data.get("response") instanceof Map) {
//							Map<String, Object> response = (Map)data.get("response");
//							if (response.get("venues") instanceof List) {
//								List<Map<String, Object>> items = (List) response.get("venues");
//								if(items != null ) {
//									long time = Calendar.getInstance().getTimeInMillis();
//									for(Map<String, Object> json : items) {
//										HiringAddress geo = parseData(json, time, minCheckinsCount);
//										if(nullable || geo != null) {
//											geoPlaces.add(geo);
//										}
//									}
//								}
//							}
//
//						}
//						break;
//					case FORBIDDEN:
//						String errorType = String.valueOf(meta.get("errorType"));
//						if("rate_limit_exceeded".equals(errorType)) {
//							long curTime = System.currentTimeMillis();
//							if(QUOTA.get() <= 0) {
//								_resetTime = curTime + LBSQuotaRedis.Instance.getRemainTimeToLive(redisKey);
//							} else {
//								_resetTime = curTime + 5000;
//							}
//							_outQuota = true;
//							logger.info("out of quota: " + QUOTA.get());
//						} else {
//							logger.error("query error " + JsonUtils.Instance.toJson(meta));
//						}
//
//						break;
//					default :
//						logger.error("query error " + JsonUtils.Instance.toJson(meta));
//						break;
//				}
//			}
//		}
//
//		rs.put("geoPlaces", geoPlaces);
//
//		return rs;
//	}
//
//	public static Map<String, Object> parseExploreContent(byte[] content, int minCheckinsCount, boolean nullable) {
//		Map<String, Object> rs = new HashMap<String, Object>();
//		List<HiringAddress> geoPlaces = new LinkedList<HiringAddress>();
//
//		Map<String, Object> data = JsonUtils.Instance.getMapObject(content);
//		if(data != null) {
//			if(data.get("meta") instanceof Map) {
//				Map<String, Object> meta = (Map<String, Object>)data.get("meta");
//				int status = NumberUtils.toInt(String.valueOf(meta.get("code")));
//				rs.put("status", status);
//				switch(status) {
//					case OK:
//						if(data.get("response") instanceof Map) {
//							Map<String, Object> response = (Map)data.get("response");
//							if (response.get("groups") instanceof List) {
//								List<Map<String, Object>> groups = (List)response.get("groups");
//								if(groups != null && groups.size() > 0) {
//									Map<String, Object> group = groups.get(0);
//									if (group.get("items") instanceof List) {
//										List<Map<String, Object>> items = (List)group.get("items");
//										long time = Calendar.getInstance().getTimeInMillis();
//										for(Map<String, Object> item : items) {
//											if (item.get("venue") instanceof Map) {
//												Map<String, Object> json = (Map)item.get("venue");
//												HiringAddress geo = parseData(json, time, minCheckinsCount);
//												if(nullable || geo != null) {
//													geoPlaces.add(geo);
//												}
//											}
//										}
//									}
//								}
//							}
//
//						}
//						break;
//					case FORBIDDEN:
//						String errorType = String.valueOf(meta.get("errorType"));
//						if("rate_limit_exceeded".equals(errorType)) {
//							long curTime = System.currentTimeMillis();
//							if(QUOTA.get() <= 0) {
//								_resetTime = curTime + LBSQuotaRedis.Instance.getRemainTimeToLive(redisKey);
//							} else {
//								_resetTime = curTime + 5000;
//							}
//							_outQuota = true;
//							logger.info("out of quota: " + QUOTA.get());
//						} else {
//							logger.error("query error " + JsonUtils.Instance.toJson(meta));
//						}
//
//						break;
//					default :
//						logger.error("query error " + JsonUtils.Instance.toJson(meta));
//						break;
//				}
//			}
//		}
//
//		rs.put("geoPlaces", geoPlaces);
//
//		return rs;
//	}
//
//	public static Map<String, Object> parseVenueDetailContent(byte[] content) {
//		Map<String, Object> rs = new HashMap<String, Object>();
//
//		Map<String, Object> data = JsonUtils.Instance.getMapObject(content);
//		if(data != null) {
//			if(data.get("meta") instanceof Map) {
//				Map<String, Object> meta = (Map<String, Object>)data.get("meta");
//				int status = NumberUtils.toInt(String.valueOf(meta.get("code")));
//				rs.put("status", status);
//				switch(status) {
//					case OK:
//						if(data.get("response") instanceof Map) {
//							Map<String, Object> response = (Map)data.get("response");
//							if (response.get("venue") instanceof Map) {
//								Map<String, Object> venue = (Map)response.get("venue");
//								long time = Calendar.getInstance().getTimeInMillis();
//								HiringAddress geo = parseData(venue, time, 0);
//								rs.put("geoPlace", geo);
//							}
//
//						}
//						break;
//					case FORBIDDEN:
//						String errorType = String.valueOf(meta.get("errorType"));
//						if("rate_limit_exceeded".equals(errorType)) {
//							long curTime = System.currentTimeMillis();
//							if(QUOTA.get() <= 0) {
//								_resetTime = curTime + LBSQuotaRedis.Instance.getRemainTimeToLive(redisKey);
//							} else {
//								_resetTime = curTime + 5000;
//							}
//							_outQuota = true;
//							logger.info("out of quota: " + QUOTA.get());
//						} else {
//							logger.error("query error " + JsonUtils.Instance.toJson(meta));
//						}
//
//						break;
//					default :
//						logger.error("query error " + JsonUtils.Instance.toJson(meta));
//						break;
//				}
//			}
//		}
//
//		return rs;
//	}
//
//	private static List<HiringAddress> getFromLink(String url) {
//		List<HiringAddress> geoPlaces = new LinkedList<HiringAddress>();
//		ContentResponse httpResult = HttpsRequest.Instance.doGet(url);
//		QUOTA.decrementAndGet();
//		LBSQuotaRedis.Instance.getQuotaAndDecrement("foursquare");
//		if(httpResult != null && httpResult.getStatus() == 200) {
//			Map<String, Object> rs = parseContent(httpResult.getContent());
//			geoPlaces = (List<HiringAddress>)rs.get("geoPlaces");
//		}
//		return geoPlaces;
//	}
//
////	public static List<BarLocation> getData(double lon, double lat, int from, int record, boolean isGetCache) {
////		List<BarLocation> geoPlaces = getData(lon, lat);
////
////		if(geoPlaces == null || geoPlaces.isEmpty()) {
////			if(isGetCache) {
////				Coordinate coor = new Coordinate(lon, lat);
////				geoPlaces = LBSModel.Instance.findNearbyBars(userId, lon, lat).getInstance().findArroundWithSrc(lon, lat, distance, PlaceSrc.FOUR_SQUARE.getId(), from, record);
////			}
////		}
////
////		return geoPlaces;
////	}
//
//	public static HiringAddress parseData(Map<String, Object> data, long time, int minCheckinsCount) {
//		Map<String, Object> stats = (Map)data.get("stats");
//		int checkinsCount = NumberUtils.toInt(String.valueOf(stats.get("checkinsCount")));
//		HiringAddress barLocation = null;
//
//		if (checkinsCount >= minCheckinsCount) {
//			Map<String, Object> location = (Map)data.get("location");
//			String name = String.valueOf(data.get("name"));
//			String idRef = String.valueOf(data.get("id"));
//	//		if(location.get("address") == null || String.valueOf(location.get("address")).isEmpty()) {
//	//			return null;
//	//		}
////			List<String> addrList = ((List<String>)location.get("formattedAddress"));
//			String countryCode = location.get("cc") != null ? String.valueOf(location.get("cc")) : "US";
//			String address = location.get("address") != null ? String.valueOf(location.get("address")) : "";
//			String city = location.get("city") != null ? String.valueOf(location.get("city")) : "";
//			String state = location.get("state") != null ? String.valueOf(location.get("state")) : "";
//			String country = location.get("country") != null ? String.valueOf(location.get("country")) : "";
//			String zipCode = location.get("postalCode") != null ? String.valueOf(location.get("postalCode")) : "";
//			
//
//			Map<String, Object> contact = (Map)data.get("contact");
//			String phone = contact.get("phone") != null ? String.valueOf(contact.get("phone")) : "";
//			if (!phone.isEmpty()) {
//				phone = getPhoneNumber(phone, countryCode);
//			}
////			String twitter = contact.get("twitter") != null ? String.valueOf(contact.get("twitter")) : "";
//			
//			double lat = NumberUtils.toDouble(String.valueOf(location.get("lat")));
//			double lon = NumberUtils.toDouble(String.valueOf(location.get("lng")));
////			double distance = NumberUtils.toDouble(String.valueOf(location.get("distance")));
//			barLocation = new HiringAddress(0, idRef, name, address, city, state, country, zipCode, phone, lon, lat, true, false);
//			//barLocation = new BarLocation(0, data.get("name").toString(), address, city, state, country, zipCode, phone, distance, lon, lat, BarLocation.FOURSQUARE_SRC, data.get("id").toString(), System.currentTimeMillis());
//		}
////		if(data.get("categories") instanceof List) {
////			List<Map<String, Object>> cates = (List)data.get("categories");
////			List<String> categories = new ArrayList<String>();
////			for(Map<String, Object> cate : cates) {
////				String id =  String.valueOf(cate.get("id"));
////				categories.add(id);
////			}
////
////			String cate = "";
////			if(categories.size() > 0) {
////				cate = categories.get(0);
////			}
////
////			String icon = PlaceCategories.getInstance().getIcon(cate, geo.getSrcType());
////			if(icon != null && !icon.isEmpty()) {
////				geo.setIcon(icon);
////			}
////
////			geo.setCategories(categories);
////		}
//
//		return barLocation;
//	}
//
//	public int getQuota() {
//		return QUOTA.get();
//	}
//
//	public String getAppId() {
//		return CLIENT_ID;
//	}
//	
//	private static String getPhoneNumber(String phone, String countryCode) {
//		String result = "";
//		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
//		try {
//			PhoneNumber phoneNumber = phoneUtil.parse(phone, countryCode);
//			if(phoneUtil.isValidNumber(phoneNumber)) {
//				result = phoneUtil.format(phoneNumber, PhoneNumberFormat.INTERNATIONAL);
//			}
//		} catch (NumberParseException e) {}
//
//		return result;
//	}
//}
