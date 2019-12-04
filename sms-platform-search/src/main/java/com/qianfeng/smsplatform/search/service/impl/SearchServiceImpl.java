package com.qianfeng.smsplatform.search.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.smsplatform.search.service.SearchService;
import com.qianfeng.smsplatform.search.util.SearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author simon
 * 2019/12/4 16:32
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    @Value("${elasticsearch.index.name}")
    private String indexName;
    @Value("${elasticsearch.index.type}")
    private String typeName;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestHighLevelClient client;
    @Override
    public boolean createIndex() throws IOException {
        //首先判断index存在不存在
        if(!existIndex(indexName)){
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            createIndexRequest.settings(Settings.builder().put("number_of_replicas",1).put("number_of_shards",1).build());
            SearchUtil.buildMapping(indexName,createIndexRequest);
            CreateIndexResponse response = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            boolean result = response.isAcknowledged();
            if(result){
                log.error("创建成功！");
                return true;
            }else{
                log.error("创建失败！");
                return false;
            }
        }else {
            log.error("创建失败，库也存在");
            return false;
        }
    }

    @Override
    public boolean existIndex(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(indexName);
        return client.indices().exists(request,RequestOptions.DEFAULT);
    }

    @Override
    public boolean deleteIndex(String indexName) throws IOException {
        if(existIndex(indexName)){
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
            AcknowledgedResponse response = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            boolean result = response.isAcknowledged();
            if(result){
                log.error("删除的结果是：{}"+objectMapper.writeValueAsString(response));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(String json) throws IOException {
        IndexRequest request = new IndexRequest(indexName,typeName);
        request.source(json,XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        DocWriteResponse.Result result = response.getResult();

        if(result.equals(DocWriteResponse.Result.CREATED)){
            log.error("创建成功！");
            return true;
        }
        log.error("创建失败！");
        return false;
    }


}
