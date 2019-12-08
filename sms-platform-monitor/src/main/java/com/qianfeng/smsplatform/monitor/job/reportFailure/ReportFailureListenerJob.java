package com.qianfeng.smsplatform.monitor.job.reportFailure;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.qianfeng.smsplatform.monitor.feign.ChannelFeign;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Classname ReportFailureListenerJob
 * @Description TODO
 * @Date 2019/12/7 15:27
 * @Created by sunjiangwei
 */

public class ReportFailureListenerJob implements SimpleJob {

    @Autowired
    private ChannelFeign channelFeign;


    @Override
    public void execute(ShardingContext shardingContext) {
        long failureCount = channelFeign.findAllReportFailure();
        if(failureCount>0){
            System.err.println("库中有失败数据，请重新发送~~");
        }
    }
}
