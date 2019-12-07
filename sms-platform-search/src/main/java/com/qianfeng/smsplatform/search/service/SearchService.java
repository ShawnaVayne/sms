package com.qianfeng.smsplatform.search.service;

import org.springframework.messaging.support.GenericMessage;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * es管理模块
 * @author simon
 * 2019/12/4 15:38
 */
public interface SearchService {
    /**
     * todo：创建库表--下行日志队列
     * @return
     */
    boolean createIndexSubmit() throws IOException;

    /**
     * todo：判断库存在不存在
     * @param indexName
     * @return
     */
    boolean existIndex(String indexName) throws IOException;

    /**
     * todo:删除一个库
     * @param indexName
     * @return
     * @throws IOException
     */
    boolean deleteIndex(String indexName) throws IOException;

    /**
     * todo:插入数据
     * @param TypeName type名字
     * @param indexName index名字
     * @param idName
     * @param json 插入内容
     * @return 是否插入成功
     */
    boolean addToLog(String typeName, String indexName, String idName, String json) throws IOException;

    /**
     * todo:根据状态报告修改下行日志
     * @param indexName
     * @param TypeName
     * @param table
     * @param json
     * @return
     */
    boolean updateLog(String indexName, String TypeName, String table, GenericMessage json) throws IOException;

    /**
     * todo:根据传递过来的json类型的字符串
     * @param param
     * @return
     */
    List<Map> search(String param) throws IOException, ParseException;

    /**
     * todo:根据条件查找统计数据
     * @param param
     * @return
     */
    long getCount(String param) throws IOException, ParseException;
}
