package com.qianfeng.smsplatform.search.service;

import java.io.IOException;

/**
 * es管理模块
 * @author simon
 * 2019/12/4 15:38
 */
public interface SearchService {
    /**
     * todo：创建库表
     * @return
     */
    boolean createIndex() throws IOException;

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
}
