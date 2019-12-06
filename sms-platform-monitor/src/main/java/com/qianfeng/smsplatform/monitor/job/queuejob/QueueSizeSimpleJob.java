package com.qianfeng.smsplatform.monitor.job.queuejob;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * @Classname QueueSizeSimpleJob
 * @Description TODO
 * @Date 2019/12/5 22:18
 * @Created by sunjiangwei
 */
public class QueueSizeSimpleJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        System.err.println("Item:"+shardingContext.getShardingItem());
        System.err.println("ShardingParamter:"+shardingContext.getShardingParameter());
        System.err.println("ShardingTotalCount:"+shardingContext.getShardingTotalCount());
        System.err.println("CurrentThread:"+Thread.currentThread());
        System.err.println("======================================");
    }
}
