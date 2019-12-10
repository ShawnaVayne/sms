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
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author simon
 * 2019/12/4 16:32
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    @Value("${elasticsearch.index.submit.name}")
    private String submitIndexName;
    @Value("${elasticsearch.index.submit.type}")
    private String submitTypeName;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestHighLevelClient client;
    @Override
    public boolean createIndexSubmit() throws IOException {
        //首先判断index存在不存在
        if(!existIndex(submitIndexName)){
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(submitIndexName);
            createIndexRequest.settings(Settings.builder().put("number_of_replicas",1).put("number_of_shards",1).build());
            SearchUtil.buildSubmitMapping(submitTypeName,createIndexRequest);
            CreateIndexResponse response = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            boolean result = response.isAcknowledged();
            if(result){
                log.error("{} 创建成功！",submitIndexName);
                return true;
            }else{
                log.error("{} 创建失败！",submitIndexName);
                return false;
            }
        }else {
            log.error("{} 创建失败，库也存在",submitIndexName);
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
    public boolean addToLog(String indexName,String TypeName,String json) throws IOException {
        IndexRequest request = new IndexRequest(indexName,TypeName);
        request.source(json,XContentType.JSON);
        DocWriteResponse.Result result = client.index(request, RequestOptions.DEFAULT).getResult();
        if(result.equals(DocWriteResponse.Result.CREATED)){
            log.error("添加数据成功！");
            return true;
        }
        log.error("添加数据失败！");
        return false;
    }

    @Override
    public boolean updateLog(String indexName, String typeName, String table, GenericMessage json) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(indexName,typeName,table);
        UpdateRequest request = updateRequest.doc(json);
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        DocWriteResponse.Result result = response.getResult();
        if (result==DocWriteResponse.Result.UPDATED){
            log.error("更新成功！");
            return true;
        }
        log.error("更新失败！");
        return false;
    }

    @Override
    public List<Map> search(String param) throws IOException, ParseException {
        System.err.println(param);
        List list = new ArrayList();
        Map map = objectMapper.readValue(param, Map.class);
        SearchSourceBuilder searchSourceBuilder = SearchUtil.getSearchSourceBuilder(map);
        Object offset = map.get("offset");
        Object limit = map.get("limit");
        int start = 1;
        int end = 10;
        if(offset==null){
            start = 1;
        }else{
            start = Integer.parseInt((String) offset);
            if (start<=0){
                start = 1;
            }
        }
        if(limit == null){
            end = 10;
        }else{
            end = Integer.parseInt(offset.toString());
            if(end<=0){
                end = 10;
            }
        }
        searchSourceBuilder.from((start-1)*end);
        SearchRequest request = new SearchRequest(submitIndexName);
        request.source(searchSourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String sourceAsString = searchHit.getSourceAsString();
            Map sourceMap = objectMapper.readValue(sourceAsString, Map.class);
            list.add(sourceMap);
        }
        return list;
    }

    @Override
    public long getCount(String param) throws IOException, ParseException {
        Map map = objectMapper.readValue(param, Map.class);
        SearchSourceBuilder searchSourceBuilder = SearchUtil.getSearchSourceBuilder(map);
        SearchRequest request = new SearchRequest(submitIndexName);
        request.source(searchSourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        return response.getHits().getTotalHits();
    }
}
