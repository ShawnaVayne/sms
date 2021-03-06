package com.qianfeng.smsplatform.search.util;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @author simon
 * 2019/12/4 15:40
 */
public class SearchUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static void buildSubmitMapping(String typeName, CreateIndexRequest request) throws IOException {
        XContentBuilder builder = JsonXContent.contentBuilder().startObject()
                .startObject("properties")
                .startObject("cityId")
                .field("type","long")
                .endObject()
                .startObject("clientID")
                .field("type","long")
                .endObject()
                .startObject("destMobile")
                .field("type","keyword")
                .endObject()
                .startObject("errorCode")
                .field("type","keyword")
                .endObject()
                .startObject("gatewayID")
                .field("type","long")
                .endObject()
                .startObject("messageContent")
                .field("type","text")
                .field("analyzer","ik_max_word")
                .endObject()
                .startObject("messagePriority")
                .field("type","long")
                .endObject()
                .startObject("msgid")
                .field("type","keyword")
                .endObject()
                .startObject("productID")
                .field("type","long")
                .endObject()
                .startObject("provinceId")
                .field("type","long")
                .endObject()
                .startObject("reportErrorCode")
                .field("type","keyword")
                .endObject()
                .startObject("reportState")
                .field("type","keyword")
                .endObject()
                .startObject("sendTime")
                .field("type","long")
                .endObject()
                .startObject("source")
                .field("type","long")
                .endObject()
                .startObject("srcNumber")
                .field("type","keyword")
                .endObject()
                .startObject("srcSequenceId")
                .field("type","long")
                .endObject()
                .startObject("operatorId")
                .field("type","long")
                .endObject()
                .endObject()
                .endObject();
        request.mapping(typeName,builder);
    }
   public static SearchSourceBuilder getSearchSourceBuilder(Map map) throws ParseException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
       BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
       Object startTime = map.get("startTime");
       Object endTime = map.get("endTime");
       Object mobile = map.get("mobile");
       Object clientID = map.get("clientID");
       Object keywords = map.get("keyword");

       TermQueryBuilder clientTerm = null;
       TermQueryBuilder mobileTerm = null;
       RangeQueryBuilder receiveTerm = null;
       MatchQueryBuilder keywordMatch = null;
       if(clientID!=null){
           clientTerm = new TermQueryBuilder("clientID",String.valueOf(clientID));
           boolQueryBuilder.must(clientTerm);
       }
       if(mobile!=null && !"".equalsIgnoreCase(mobile.toString().trim())){
           mobileTerm = new TermQueryBuilder("destMobile",mobile.toString());
           boolQueryBuilder.must(mobileTerm);
       }
       if(startTime!=null & endTime!=null){
           receiveTerm = QueryBuilders.rangeQuery("sendTime").gte((long)startTime).lte((long)endTime);
           boolQueryBuilder.must(receiveTerm);
       }else if(startTime != null & endTime == null){
           receiveTerm =  QueryBuilders.rangeQuery("sendTime").gte((long)startTime);
           boolQueryBuilder.must(receiveTerm);
       }else if(startTime == null & endTime != null){
           receiveTerm = QueryBuilders.rangeQuery("sendTime").lte((long)endTime);
           boolQueryBuilder.must(receiveTerm);
       }
       if(keywords != null && !"".equalsIgnoreCase(keywords.toString().trim())){
           keywordMatch = QueryBuilders.matchQuery("messageContent", keywords.toString());
           boolQueryBuilder.must(keywordMatch);
       }
       sourceBuilder.query(boolQueryBuilder);
       return sourceBuilder;
   }
}
