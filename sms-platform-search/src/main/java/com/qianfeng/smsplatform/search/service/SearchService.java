package com.qianfeng.smsplatform.search.service;

import java.io.IOException;

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
     * todo:创建库表-- 状态报告表
     * @return
     */
    boolean createIndexReport() throws IOException;

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
     * @param indexName index名字
     * @param TypeName type名字
     * @param json 插入内容
     * @return 是否插入成功
     */
    boolean addToLog(String indexName,String typeName,String json) throws IOException;

    /**
     * todo:根据状态报告修改下行日志
     * @param indexName
     * @param TypeName
     * @param json
     * @return
     */
    boolean updateLog(String indexName,String TypeName,String json);
}
