/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mit.es;

import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.utils.ConfigUtils;


/**
 *
 * @author truyetnm
 */
public abstract class ESController {

    protected TransportClient _cli = null;
    protected static final Logger _logger = LoggerFactory.getLogger(ESController.class);
    private String indexName;

    protected ESController(String index_name) {
    	if(index_name != null && !index_name.isEmpty()) {
    		indexName = index_name;
        	_cli = reload();
    	} else {
    		_logger.error("missing config index");
    		System.exit(0);
    	}
    }

    protected ESController() {}

    private TransportClient reload() {
    	Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", ConfigUtils.getConfig().getString("es.clustername")).build();
    	TransportClient transport = new TransportClient(settings);

        String[] serverList = ConfigUtils.getConfig().getStringArray("es.servers");
        for(String server : serverList) {
        	String[] hostPort = server.split(":");
        	if(hostPort.length == 2) {
        		int port = NumberUtils.toInt(hostPort[1]);
        		transport = transport.addTransportAddress(new InetSocketTransportAddress(hostPort[0], port));
        	}
        }

        return transport;
    }


    public boolean exist() {
        return _cli.admin().indices().exists(new IndicesExistsRequest(indexName)).actionGet().isExists();
    }

    public boolean exist(String type) {
        try {
            //ClusterState cs = _cli.admin().cluster().prepareState().setFilterIndices(m_strIndexName).execute().actionGet().getState();
            ClusterState cs = _cli.admin().cluster().prepareState().setIndices(indexName).execute().actionGet().getState();

            IndexMetaData imd = cs.getMetaData().index(indexName);
            if (imd != null) {
                MappingMetaData mmd = imd.mapping(type);
                if (mmd != null) {
                    return true;
                }
            }
            return false;
        } catch (ElasticsearchException ex) {
            _logger.error("exist ", ex);
            return false;
        }

    }

    protected boolean create() {
        CreateIndexResponse resp = _cli.admin().indices().create(new CreateIndexRequest(indexName)).actionGet();
        return resp.isAcknowledged();
    }

    public void refresh() {
        try {
            RefreshRequest rr = new RefreshRequest(indexName);
            _cli.admin().indices().refresh(rr).actionGet();
        } catch (Exception e) {
            _logger.error("refresh ", e);
        }
    }

    protected boolean delete(String type) {
        try {
            DeleteMappingResponse resp = _cli.admin().indices().prepareDeleteMapping(indexName).setType(type).execute().actionGet();
            return resp.isAcknowledged();
        } catch (ElasticsearchException ex) {
            _logger.error("delete ", ex);
        }
        return false;
    }

    public String delete(String type, String id) {
        try {
            DeleteRequest dr = new DeleteRequest(indexName, type, id);
            DeleteResponse ret = _cli.delete(dr).actionGet();
            if (ret != null) {
                return ret.getId();
            }
            return null;
        } catch (ElasticsearchException ex) {
            _logger.error("remove " , ex);
            return null;
        }
    }

    public boolean isAvalableNode() {
    	if(_cli != null) {
    		if(_cli.connectedNodes().isEmpty()) {
    			_cli.close();
    			_cli = reload();
    		}
    	} else {
    		_cli = reload();
    	}

    	return !_cli.connectedNodes().isEmpty();
    }
}
