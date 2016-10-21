 package com.mit.es;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.GeoDistanceFilterBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.dao.address.HiringAddressDAO;
import com.mit.dao.mid.MIdGenLongDAO;
import com.mit.entities.address.HiringAddress;
import com.mit.utils.ConfigUtils;

public class HiringESController extends ESController {
	private final Logger _logger = LoggerFactory.getLogger(HiringESController.class);
	private static String indexName = ConfigUtils.getConfig().getString("es.hiring.index");
	private final String TYPE = ConfigUtils.getConfig().getString("es.hiring.type");
	private final double maxDistance = ConfigUtils.getConfig().getDouble("es.hiring.maxdistance");
//	private final int maxSize = ConfigUtils.getConfig().getInt("es.barlbs.maxsize");

	public static HiringESController Instance = new HiringESController();

    private HiringESController() {
        super(indexName);
        create();
    }

    @Override
	public boolean create() {
        try {
            if (this.exist(TYPE)) {
                return true;
            }
            //build mapping file
            XContentBuilder xbuilder = XContentFactory.jsonBuilder();
            XContentBuilder mapping =
                    xbuilder.startObject()
                        .startObject(TYPE)
                            .startObject("properties")
                            	.startObject("id")
                            	.field("type", "long")
                            	.endObject()
								.startObject("name")
                                .field("type", "string")
                                .endObject()
                                .startObject("address")
                                .field("type", "string")
                                .endObject()
                                .startObject("city")
                                .field("type", "string")
                                .endObject()
                                .startObject("state")
                                .field("type", "string")
                                .endObject()
                                .startObject("country")
                                .field("type", "string")
                                .endObject()
                                .startObject("zip_code")
                                .field("type", "string")
                                .endObject()
                                .startObject("phone")
                                .field("type", "string")
                                .endObject()
                                .startObject("location")
                                .field("type", "geo_point")
								.field("lat_lon", "true")
								.endObject()
								.startObject("isVerified")
								.field("type", "boolean")
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject();
            PutMappingResponse resp = _cli.admin().indices().preparePutMapping(indexName)
                    .setType(TYPE).setSource(mapping).execute().actionGet();
            return resp.isAcknowledged();

        } catch (Exception nodeEx) {
            _logger.error("createMapping ", nodeEx);
            return false;
        }
    }
    
    public int index(HiringAddress barLocation) {
        int result = -1;
        if (_cli != null) {
            try {
            	if(barLocation.getId() <= 0) {
            		HiringAddress cur = getByIdRef(barLocation.getIdRef());
            		if(cur ==null) {
            			barLocation.setId(MIdGenLongDAO.getInstance(HiringAddressDAO.getInstance().getTableName()).getNext());
            		} else {
            			barLocation.setId(cur.getId());
            		}
				}
                IndexRequestBuilder irb = _cli.prepareIndex(indexName, TYPE, barLocation.getIdRef());
                XContentBuilder xbuilder = XContentFactory.jsonBuilder();
                //String location = String.format("%f,%f", value.getLatitude(), value.getLongitude());
                
                xbuilder.startObject()
						.field("name", barLocation.getName())
						.field("address", barLocation.getAddress())
						.field("city", barLocation.getCity())
						.field("state", barLocation.getState())
						.field("country", barLocation.getCountry())
						.field("zip_code", barLocation.getZipCode())
						.field("phone", barLocation.getPhone())
						.field("isVerified", barLocation.getIsVerified())
						.field("id", barLocation.getId())
                        .startObject("location").field("lat", barLocation.getLat()).field("lon", barLocation.getLon()).endObject()
						.endObject();
                IndexResponse irsp = irb.setSource(xbuilder).execute().actionGet();
                if (barLocation.getId() ==  NumberUtils.toInt(irsp.getId())) {
                    result = 0;
                }
            } catch (Exception ex) {
            	_logger.error("index error ", ex);
            }
        }

        return result;
    }
    
    public int bulk(List<HiringAddress> barLocations) {
        int result = -1;
        boolean isUpdate = false;
        if (isAvalableNode()) {
            try {
            	BulkRequestBuilder bulkRequest = _cli.prepareBulk();

            	for(HiringAddress barLocation : barLocations) {
            		if (barLocation != null) {
            			HiringAddress cur = getByIdRef(barLocation.getIdRef());
            			HiringAddress cur_mongo = HiringAddressDAO.getInstance().getByIdRef(barLocation.getIdRef());
                		if(cur_mongo != null || (cur != null && cur.getIsVerified())) {
                			continue;
                		}
                		HiringAddressDAO.getInstance().insert(barLocation);
            			isUpdate = true;
                		bulkRequest.add(_cli.prepareIndex(indexName, TYPE, barLocation.getIdRef()).setSource(
    	                XContentFactory.jsonBuilder().startObject()
    						.field("name", barLocation.getName())
    						.field("address", barLocation.getAddress())
    						.field("city", barLocation.getCity())
							.field("state", barLocation.getState())
							.field("country", barLocation.getCountry())
							.field("zip_code", barLocation.getZipCode())
							.field("phone", barLocation.getPhone())
    						.field("id", barLocation.getId())
    						.field("isVerified", barLocation.getIsVerified())
                            .startObject("location").field("lat", barLocation.getLat()).field("lon", barLocation.getLon()).endObject()));
            		}
            	}

            	if(isUpdate) {
	            	BulkResponse bulkResponse = bulkRequest.execute().actionGet();
	            	if (bulkResponse.hasFailures()) {
	            	    System.out.println(bulkResponse.buildFailureMessage());
	            	} else {
	            		result = 0;
	            	}
            	} else {
            		result = 0;
            	}
            } catch (Exception ex) {
            	_logger.error("index error ", ex);
            }
        }

        return result;
    }


    public boolean remove(String id) {
        try {
            DeleteResponse resp = _cli.prepareDelete(indexName, TYPE, id).execute().actionGet();
            return resp.getId().equals(id);
        } catch (Exception nodeEx) {
            _logger.error("remove ", nodeEx);
            return false;
        }
    }
    
    public void remove(List<Long> ids) {
    	try {
    		TermsQueryBuilder filter = QueryBuilders.termsQuery("id", ids);
    		_cli.prepareDeleteByQuery(indexName).setQuery(filter).execute().actionGet();
    	} catch (Exception e) {
    		_logger.error("remove ", e);
    	}
    }

    public List<HiringAddress> findArround(double longitude, double latitude) {
    	List<HiringAddress> rs = null;
    	try {
    		SearchRequestBuilder srb = _cli.prepareSearch(indexName);
    		GeoDistanceFilterBuilder gdfb = FilterBuilders.geoDistanceFilter("location");
    		gdfb.distance(maxDistance, DistanceUnit.KILOMETERS);
    		gdfb.lat(latitude).lon(longitude);
    		
    		GeoDistanceSortBuilder gdsb = SortBuilders.geoDistanceSort("location").point(latitude, longitude)
    				.order(SortOrder.ASC);
    		
    		srb.setTypes(TYPE);
    		srb.setPostFilter(gdfb);
    		srb.addSort(gdsb);
//    		srb.setSize(maxSize);
    		
    		SearchResponse resp = srb.execute().actionGet();
    		SearchHits hits = resp.getHits();
    		rs = parseFromSearchHits(hits);
    	} catch (Exception e) {
    		_logger.error("findArround ", e);
    	}
    	
    	return rs;
    }
    
    public List<HiringAddress> findArround(double longitude, double latitude, int from , int count, String name, double distance) {
    	List<HiringAddress> rs = null;
    	try {
    		SearchRequestBuilder srb = _cli.prepareSearch(indexName);
    		GeoDistanceFilterBuilder gdfb = FilterBuilders.geoDistanceFilter("location");
    		gdfb.distance(distance, DistanceUnit.KILOMETERS);
    		gdfb.lat(latitude).lon(longitude);
    		
    		GeoDistanceSortBuilder gdsb = SortBuilders.geoDistanceSort("location").point(latitude, longitude)
    				.order(SortOrder.ASC);
    		
    		srb.setTypes(TYPE);
    		if (name != null && !name.isEmpty()) {
    			srb.setQuery(QueryBuilders.matchPhraseQuery("name", name));
    		}
    		srb.setPostFilter(gdfb);
    		srb.addSort(gdsb);
    		srb.setFrom(from);
    		srb.setSize(count);
    		
    		SearchResponse resp = srb.execute().actionGet();
    		SearchHits hits = resp.getHits();
    		rs = parseFromSearchHits(hits);
    	} catch (Exception e) {
    		_logger.error("findArround ", e);
    	}
    	
    	return rs;
    }
    
    public List<Long> findPlaceIdArround(double longitude, double latitude, String name, double distance) {
    	List<Long> rs = null;
    	try {
    		SearchRequestBuilder srb = _cli.prepareSearch(indexName);
    		GeoDistanceFilterBuilder gdfb = FilterBuilders.geoDistanceFilter("location");
    		gdfb.distance(distance, DistanceUnit.KILOMETERS);
    		gdfb.lat(latitude).lon(longitude);
    		
    		GeoDistanceSortBuilder gdsb = SortBuilders.geoDistanceSort("location").point(latitude, longitude)
    				.order(SortOrder.ASC);
    		srb.setTypes(TYPE);
    		if (name != null && !name.isEmpty()) {
    			srb.setQuery(QueryBuilders.matchPhraseQuery("name", name));
    		}
    		srb.setPostFilter(gdfb);
    		srb.addSort(gdsb);
    		
    		SearchResponse resp = srb.execute().actionGet();
    		SearchHits hits = resp.getHits();
    		if (hits != null && hits.getTotalHits() > 0) {
    			rs = new ArrayList<Long>();
    			for (int i = 0; i < hits.getHits().length; i++) {
    				SearchHit hit = hits.getAt(i);
    				Map<String, Object> source = hit.getSource();
    				if (source != null && source.get("id") != null) {
    					rs.add(NumberUtils.toLong(String.valueOf(source.get("id"))));
    				}
    			}
    		}
    		
    		
    	} catch (Exception e) {
    		_logger.error("findPlaceIdArround ", e);
    	}
    	
    	return rs;
    }
    
    public HiringAddress getByIdRef(String _id) {
    	HiringAddress rs = null;
    	try {
    		SearchRequestBuilder srb = _cli.prepareSearch(indexName);
    		srb.setQuery(QueryBuilders.termQuery("_id", _id));
    		SearchResponse resp = srb.execute().actionGet();
    		SearchHits hits = resp.getHits();
    		if (hits.getHits().length > 0) {
    			rs = parseFromSearchHit(hits.getAt(0));
    		}
    	} catch (Exception e) {
    		_logger.error("getByIdRef ", e);
    	}
    	
    	return rs;
    }
    
    public HiringAddress getById(long id) {
    	HiringAddress rs = null;
    	try {
    		SearchRequestBuilder srb = _cli.prepareSearch(indexName);
    		srb.setQuery(QueryBuilders.termQuery("id", id));
    		SearchResponse resp = srb.execute().actionGet();
    		SearchHits hits = resp.getHits();
    		if (hits.getHits().length > 0) {
    			rs = parseFromSearchHit(hits.getAt(0));
    		}
    	} catch (Exception e) {
    		_logger.error("getById ", e);
    	}
    	
    	return rs;
    }
    
    public HiringAddress parseFromSearchHit(SearchHit hit) {
    	HiringAddress rs = null;
    	if (hit != null) {
    		rs = new HiringAddress();

            rs.setIdRef(hit.getId());
            
            Map<String, Object> fields = hit.getSource();
            
            if (fields.get("id") != null) {
            	rs.setId(NumberUtils.toLong(String.valueOf(fields.get("id"))));
            }
			
            if(fields.get("name") != null) {
				rs.setName((String)fields.get("name"));
			}

			if(fields.get("address") != null) {
				rs.setAddress((String)fields.get("address"));
			}

			if(fields.get("city") != null) {
				rs.setCity((String)fields.get("city"));
			}
			
            if(fields.get("state") != null) {
            	rs.setState((String)fields.get("state"));
			}
            
            if(fields.get("country") != null) {
            	rs.setCountry((String)fields.get("country"));
			}
            
            if(fields.get("zip_code") != null) {
            	rs.setZipCode((String)fields.get("zip_code"));
			}
            
            if(fields.get("phone") != null) {
            	rs.setPhone((String)fields.get("phone"));
			}
            
            if (fields.get("isVerified") != null) {
            	rs.setIsVerified((boolean)fields.get("isVerified"));
            }

			if(fields.get("location") instanceof Map) {
				Map<String, Double> loc = (Map<String, Double>) fields.get("location");
				rs.setLon(loc.get("lon"));
				rs.setLat(loc.get("lat"));
			}

    	}
    	
    	return rs;
    }

    
    public List<HiringAddress> parseFromSearchHits(SearchHits hits) {
    	List<HiringAddress> result = null;
    	if (hits != null) {
            int total = hits.getHits().length;
			result = new LinkedList<HiringAddress>();

            for (int i = 0; i < total; ++i) {
                try {
                	SearchHit hit = hits.getAt(i);
                	HiringAddress value = parseFromSearchHit(hit);
                	if (value != null) {
                		result.add(value);
                	}
                } catch (Exception ex) {
                    _logger.error("parseFromSearchHits ", ex);
                }
            }
    	}
    	
    	return result;
    }
    
    public static void main(String[] args) {
//		Map<String, Object> rs = new HashMap<String, Object>();
//    	ApiMessage msg = new ApiMessage();
////		List<Long> adds = HiringESController.Instance.findPlaceIdArround(-116.54435361196911, 33.80427695384729, "spa", 10);
////    	List<HiringAddressView> data = null;
//		boolean hasMore = false;
//		int count = 100, from = 0;
////		List<Long> adds = HiringESController.Instance.findPlaceIdArround(-116.54435361196911, 33.80427695384729, "", 10);
//		List<HiringAddress> places = HiringESController.Instance.findArround(-116.54435361196911, 33.80427695384729, from, count + 1, "", 10);
//		if (places != null && places.size() > count) {
//			hasMore = true;
//			places = places.subList(0, count);
//		}
//		Map<String, Object> rs = new HashMap<String, Object>();
//		rs.put("hasMore", hasMore);
//		rs.put("places", HiringAddress.buildListAddView(places));
//		msg.setData(rs);
//		
//		System.out.println(msg.toString());
    	
    	HiringESController.Instance.remove(Arrays.asList(2L,3L,1L,4L));
	}
}
