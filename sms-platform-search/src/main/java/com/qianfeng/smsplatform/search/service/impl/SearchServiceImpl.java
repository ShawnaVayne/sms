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
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author simon
 * 2019/12/4 16:32
 */
@Service
@Slf4j
public class  SearchServiceImpl implements SearchService {
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
    public boolean addToLog(String indexName, String typeName, String idName, String json) throws IOException {
        IndexRequest request = new IndexRequest(indexName,typeName,idName);
        request.source(json,XContentType.JSON);
        DocWriteResponse.Result result = client.index(request, RequestOptions.DEFAULT).getResult();
        if(result==DocWriteResponse.Result.CREATED || result==DocWriteResponse.Result.UPDATED){
            log.error("添加数据成功！");
            return true;
        }
        log.error("添加数据失败！");
        return false;
    }

    @Override
    public boolean updateLog(String indexName, String typeName, String table, String json) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(indexName,typeName,table);
        System.out.println(json);
        Map map = objectMapper.readValue(json, Map.class);
        UpdateRequest request = updateRequest.doc(map);
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
        Object pageNum = map.get("start");
        Object pageSize = map.get("rows");
        int start = 1;
        int rows = 10;
        if(pageNum==null){
            start = 1;
        }else{
            start = Integer.parseInt(String.valueOf(pageNum));
            if (start<=0){
                start = 1;
            }
        }
        if(pageSize == null){
            rows = 10;
        }else{
            rows = Integer.parseInt(String.valueOf(pageSize));
            if(rows<=0){
                rows = 10;
            }
        }
        searchSourceBuilder.from(start);
        searchSourceBuilder.size(rows);
        //判断是否有关键字
        if(map.get("keyword")!=null){
            //判断用户是否传递了高亮前缀和高亮后缀，如果没有则用默认的
            Object highLightPreTag = map.get("highLightPreTag");
            Object highLightPostTag = map.get("highLightPostTag");
            if(highLightPostTag==null || "".equalsIgnoreCase(highLightPostTag.toString())){
                highLightPostTag = "</span>";
            }
            if(highLightPreTag==null || "".equalsIgnoreCase(highLightPreTag.toString())){
                highLightPreTag = "<span style='color:red;font-weight:bold;'>";
            }
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("messageContent");
            highlightBuilder.preTags(highLightPreTag.toString());
            highlightBuilder.postTags(highLightPostTag.toString());
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        SearchRequest request = new SearchRequest(submitIndexName);
        request.source(searchSourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String sourceAsString = searchHit.getSourceAsString();
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            HighlightField highlightField = highlightFields.get("messageContent");
            Map sourceMap = objectMapper.readValue(sourceAsString, Map.class);
            if(highlightField!=null){
                Text[] fragments = highlightField.getFragments();
                if(fragments!=null){
                    for (Text fragment : fragments) {
                        sourceMap.put("messageContent",fragment);
                    }
                    String fragment = fragments[0].string();
                    sourceMap.put("messageContent",fragment);
                }
            }
            list.add(sourceMap);
        }
        return list;
    }

    @Override
    public Map<String,Long> getState(String param) throws IOException, ParseException {
        Map map = objectMapper.readValue(param, Map.class);
        SearchRequest searchRequest = new SearchRequest(submitIndexName);
        searchRequest.types(submitTypeName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        Object startTime = map.get("startTime");
        Object endTime = map.get("endTime");
        Object clientID = map.get("clientID");

        if(startTime!=null & endTime!=null){
            sourceBuilder.query(QueryBuilders.rangeQuery("sendTime").from((long)startTime).to((long)endTime));
        }else if(startTime != null & endTime == null){
            sourceBuilder.query(QueryBuilders.rangeQuery("sendTime").from((long)startTime));
        }else if(startTime == null & endTime != null){
            sourceBuilder.query(QueryBuilders.rangeQuery("sendTime").to((long)endTime));
        }
        if(clientID!=null){
            TermQueryBuilder termQuery = QueryBuilders.termQuery("clientID", clientID.toString());
            sourceBuilder.query(termQuery);
        }
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("reportState")
                    .field("reportState");
        sourceBuilder.aggregation(aggregation);
        searchRequest.source(sourceBuilder);

        System.err.println("source:"+searchRequest.source());

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        /*SearchHits hits = response.getHits();*/
        Aggregations aggregations = response.getAggregations();
        Terms client = aggregations.get("reportState");
        List<? extends Terms.Bucket> buckets = client.getBuckets();
        Map<String,Long> countMap = new HashMap<>(16);
        for (int i = 0; i < buckets.size(); i++) {
            Terms.Bucket bucket = buckets.get(i);
            Object key = bucket.getKey();
            long docCount = bucket.getDocCount();
            countMap.put(key.toString(),docCount);
        }
        System.err.println(countMap);
        return countMap;
    }

    @Override
    public long getCount(String param) throws IOException, ParseException {
        Map map = objectMapper.readValue(param, Map.class);
        SearchSourceBuilder sourceBuilder = SearchUtil.getSearchSourceBuilder(map);
        SearchRequest request = new SearchRequest(submitIndexName);
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        long totalHits = response.getHits().getTotalHits();
        return totalHits;
    }
}
