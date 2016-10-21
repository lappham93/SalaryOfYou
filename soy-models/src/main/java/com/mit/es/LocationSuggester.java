 package com.mit.es;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.suggest.SuggestRequestBuilder;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.entities.Location;
import com.mit.utils.ConfigUtils;

/**
 *
 * @author truyetnm
 */
public class LocationSuggester extends ESController {
	private final Logger _logger = LoggerFactory.getLogger(LocationSuggester.class);
    private static String indexName = ConfigUtils.getConfig().getString("es.location.index");
    private static final String TYPE = ConfigUtils.getConfig().getString("es.location.type");
    private static final int THRESHOLD = ConfigUtils.getConfig().getInteger("es.location.threshold", 99999);

    public static LocationSuggester Instance = new LocationSuggester();

    protected LocationSuggester(String indexName) {
        super(indexName);
    }

    private LocationSuggester() {
    	super(indexName);
    }

    @Override
	public boolean create() {
        try {
            if (this.exist(TYPE)) {
                return true;
            }
            
         // close index before update settings
//            _cli.admin().indices().close(new CloseIndexRequest(indexName)).actionGet();
            //build settings
//            XContentBuilder settingBuilder = XContentFactory.jsonBuilder();
//            XContentBuilder settings =
//            		settingBuilder.startObject()
//                    	.startObject("analysis")
//                    		.startObject("filter")
//                            	.startObject("ngram")
//                            		.field("type", "ngram")
//		                            .field("min_gram", 2)
//		                            .field("max_gram", 15)
//			                    .endObject()
//			                .endObject()
//                    		.startObject("analyzer")
//                            	.startObject("autocomplete-index")
//		                            .field("type", "custom")
//		                            .field("tokenizer", "standard")
//		                            .field("filter", new String[]{"lowercase", "kstem"})
//			                    .endObject()
//			                    .startObject("autocomplete-search")
//		                            .field("type", "custom")
//		                            .field("tokenizer", "standard")
//		                            .field("filter", new String[]{"lowercase", "kstem"})
//			                    .endObject()
//			                .endObject()
//			            .endObject()
//			        .endObject();
//            UpdateSettingsResponse uSR = _cli.admin().indices().prepareUpdateSettings(indexName)
//                    .setSettings(settings.string()).execute().actionGet();
//            // reopen index
//            _cli.admin().indices().open(new OpenIndexRequest(indexName)).actionGet();
            
            //build mapping file
            XContentBuilder xbuilder = XContentFactory.jsonBuilder();
            XContentBuilder mapping =
                    xbuilder.startObject()
                        .startObject(TYPE)
                            .startObject("properties")
								.startObject("suggest")
	                                .field("type", "completion")
	                                .field("index_analyzer", "simple")
	                                .field("search_analyzer", "simple")
	                                .field("payloads", true)
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

    public boolean remove(String type) {
        try {
            DeleteMappingResponse resp = _cli.admin().indices().prepareDeleteMapping(indexName).setType(type).execute().actionGet();
            return resp.isAcknowledged();
        } catch (Exception nodeEx) {
            _logger.error("remove ", nodeEx);
            return false;
        }
    }

    public int index(Location location) {
        int result = -1;
        if (isAvalableNode()) {
            try {
        		String id = getId(location.getCity(), location.getState());
                IndexRequestBuilder irb = _cli.prepareIndex(indexName, TYPE, id);
                XContentBuilder xbuilder = XContentFactory.jsonBuilder();
                //String location = String.format("%f,%f", value.getLatitude(), value.getLongitude());
                xbuilder.startObject()
							.startObject("suggest")
								.field("input", location.getCity())
								.startObject("payload")
									.field("country_code", location.getCountryCode())
									.field("city", location.getCity())
									.field("state", location.getState())
									.field("state_code", location.getStateCode())
									.field("county", location.getCounty())
									.field("county_code", location.getCountyCode())
			                        .startObject("location")
			                        	.field("lat", location.getLat())
			                        	.field("lon", location.getLon())
			                        .endObject()
								.endObject()
			                    .field("weight", location.getWeight())
							.endObject()
						.endObject();
                IndexResponse irsp = irb.setSource(xbuilder).execute().actionGet();
            } catch (Exception ex) {
            	_logger.error("index error ", ex);
            }
        }

        return result;
    }

    public int bulk(List<Location> locations) {
        int result = -1;
        if (isAvalableNode()) {
            try {
            	BulkRequestBuilder bulkRequest = _cli.prepareBulk();

            	for(Location location : locations) {
            		String id = getId(location.getCity(), location.getState());
            		bulkRequest.add(_cli.prepareIndex(indexName, TYPE, id).setSource(
	                XContentFactory.jsonBuilder().startObject()
						.startObject("suggest")
							.field("input", location.getCity())
							.startObject("payload")
								.field("country_code", location.getCountryCode())
								.field("city", location.getCity())
								.field("state", location.getState())
								.field("state_code", location.getStateCode())
								.field("county", location.getCounty())
								.field("county_code", location.getCountyCode())
		                        .startObject("location")
		                        	.field("lat", location.getLat())
		                        	.field("lon", location.getLon())
		                        .endObject()
							.endObject()
		                    .field("weight", location.getWeight())
						.endObject()
					.endObject()));
            	}

            	BulkResponse bulkResponse = bulkRequest.execute().actionGet();
            	if (bulkResponse.hasFailures()) {
            	    System.out.println(bulkResponse.buildFailureMessage());
            	} else {
            		result = 0;
            	}
            } catch (Exception ex) {
            	_logger.error("index error ", ex);
            }
        }

        return result;
    }
    
    public List<Location> search(String q, int size) {
    	List<Location> locations = Collections.emptyList();
        try {
        	CompletionSuggestionBuilder suggestionsBuilder = new CompletionSuggestionBuilder("completeMe");
            suggestionsBuilder.text(q);
            suggestionsBuilder.field("suggest");
            suggestionsBuilder.size(size);
            SuggestRequestBuilder suggestRequestBuilder =
                    _cli.prepareSuggest(indexName).addSuggestion(suggestionsBuilder);

            SuggestResponse suggestResponse = suggestRequestBuilder.execute().actionGet();

            List<CompletionSuggestion.Entry.Option> options = (List<CompletionSuggestion.Entry.Option>)
                    suggestResponse.getSuggest().getSuggestion("completeMe").iterator().next().getOptions();

            locations = parseFromOptions(options);
        } catch (Exception nodeEx) {
             _logger.error("search ", nodeEx);
        }
        
    	return locations;
    }
    
    public String searchId(String city, String state) {
    	String id = "";
        try {
            SearchRequestBuilder srb = _cli.prepareSearch(indexName, TYPE);

            AndFilterBuilder andFilters = FilterBuilders.andFilter(FilterBuilders.termFilter("city", city.toLowerCase()))
            		.add(FilterBuilders.termFilter("state", state.toLowerCase()));
            
            srb.setPostFilter(andFilters);
            srb.addFields("_id");

            SearchResponse resp = srb.setSize(1).execute().actionGet();
            SearchHits hits = resp.getHits();
            if (hits.getHits().length > 0) {
            	SearchHit hit = hits.getAt(0);
            	id = hit.getId();
            }
        } catch (Exception nodeEx) {
             _logger.error("searchId ", nodeEx);
        }
        
    	return id;
    }
    
    public void incWeight(String city, String state, int weight) {
        String id = getId(city, state);
        incWeight(id, weight);
    }
    
    public void incWeight(String id, int weight) {
        if (_cli != null) {
            try {
                UpdateRequestBuilder irb = _cli.prepareUpdate(indexName, TYPE, id);
                irb.setScript("if (ctx._source.suggest.weight < threshold) { ctx._source.suggest.weight += weight }", ScriptService.ScriptType.INLINE)
                	.addScriptParam("weight", weight).addScriptParam("threshold", THRESHOLD);
                irb.execute().get();
            } catch (Exception ex) {
            	_logger.error("incWeight error ", ex);
            }
        }
    }

    public List<Location> parseFromOptions(List<CompletionSuggestion.Entry.Option> options) {
        if (options != null) {
			List<Location> result = new LinkedList<Location>();

            for (CompletionSuggestion.Entry.Option option: options) {
                try {
                	Location value = new Location();

                    Map<String, Object> fields = option.getPayloadAsMap();

					if(fields.get("country_code") != null) {
						value.setCountryCode((String)fields.get("country_code"));
					}

					if(fields.get("city") != null) {
						value.setCity((String)fields.get("city"));
					}

					if(fields.get("state") != null) {
						value.setState((String)fields.get("state"));
					}

					if(fields.get("state_code") != null) {
						value.setStateCode((String)fields.get("state_code"));
					}

					if(fields.get("county") != null) {
						value.setCounty((String)fields.get("county"));
					}

					if(fields.get("county_code") != null) {
						value.setCountyCode((String)fields.get("county_code"));
					}

					result.add(value);
                } catch (Exception ex) {
                    _logger.error("parseFromOptions ", ex);
                }
            }
            return result;
        } else {
            return null;
        }
    }
    
    public String getId(String city, String state) {
    	return city.toLowerCase() + "," + state.toLowerCase();
    }
    
    public static void main(String[] args) {
//    	String q = "yor";
//    	int size = 10;
//    	List<Location> locations = LocationSuggester.Instance.search(q, size);
//    	System.out.println(JsonUtils.Instance.toJson(locations));
    	LocationSuggester.Instance.incWeight("Denali National Park", "Alaska", 1);
	}
}
