package com.qianfeng.smsplatform.search.util;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;

import java.io.IOException;
import java.text.SimpleDateFormat;

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
                .field("type","text")
                .startObject("fields")
                    .startObject("keyword")
                    .field("type","keyword")
                    .field("ignore_above",256)
                    .endObject()
                .endObject()
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
                .field("type","long")
                .endObject()
                .startObject("sendTime")
                .field("type","date")
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
    public static void buildReportMapping(String typeName,CreateIndexRequest request) throws IOException {
        XContentBuilder builder = JsonXContent.contentBuilder();
        builder.startObject()
                .startObject("properties")
                .startObject("mobile")
                .field("type","keyword")
                .endObject()
                .startObject("state")
                .field("type","long")
                .endObject()
                .startObject("errorCode")
                .field("type","type")
                .endObject()
                .startObject("srcID")
                .field("type","long")
                .endObject()
                .startObject("clientID")
                .field("type","long")
                .endObject()
                .startObject("msgId")
                .field("type","keyword")
                .endObject()
                .startObject("sendCount")
                .field("type","long")
                .endObject()
                .endObject()
                .endObject();
        request.mapping(typeName,builder);
    }
}
