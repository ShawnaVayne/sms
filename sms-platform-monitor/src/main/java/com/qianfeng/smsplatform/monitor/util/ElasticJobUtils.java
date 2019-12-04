package com.qianfeng.smsplatform.monitor.util;

import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;

/**
 * @Classname ElasticJobUtils
 * @Description TODO
 * @Date 2019/12/3 21:22
 * @Created by sunjiangwei
 */

public class ElasticJobUtils {

    /**
     * 创建简单任务详细信息
     * @param jobClass
     * @param cron
     * @param shardingTotalCount
     * @param shardingItemParamters
     * @return
     */
    public static LiteJobConfiguration getSimpleJobConfiguration(final Class<? extends SimpleJob> jobClass,
                                                                 final String cron,
                                                                 final int shardingTotalCount,
                                                                 final String shardingItemParamters){
        return LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(
                JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount)
                        .shardingItemParameters(shardingItemParamters).build()
                , jobClass.getCanonicalName())
        ).overwrite(true).build();
    }

    public static LiteJobConfiguration getDataFlowJobConfiguration(final Class<? extends DataflowJob> jobClass,
                                                                   final String cron,
                                                                   final int shardingTotalCount,
                                                                   final String shardingItemParamters,
                                                                   final boolean streamingProcess){
        return LiteJobConfiguration.newBuilder(new DataflowJobConfiguration(
                JobCoreConfiguration.newBuilder(jobClass.getName(),cron,shardingTotalCount)
                    .shardingItemParameters(shardingItemParamters).build(),
                jobClass.getCanonicalName(),streamingProcess)
        ).overwrite(true).build();
    }

}
