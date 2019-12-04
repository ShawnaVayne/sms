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

    public static void buildMapping(String typeName, CreateIndexRequest request) throws IOException {
        XContentBuilder builder = JsonXContent.contentBuilder().startObject()
                .startObject("properties")
                .startObject("cityId")
                .field("type","long")
                .endObject()
                .startObject("clientId")
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
                .startObject("gatewayId")
                .field("type","long")
                .endObject()
                .startObject("messageContent")
                .field("type","text")
                .field("analyzer","ik_max_word")
                .endObject()
                .startObject("messagePriority")
                .field("type","long")
                .endObject()
                .startObject("msgId")
                .field("type","keyword")
                .endObject()
                .startObject("productId")
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
                .endObject()
                .endObject();
        request.mapping(typeName,builder);
    }
}
