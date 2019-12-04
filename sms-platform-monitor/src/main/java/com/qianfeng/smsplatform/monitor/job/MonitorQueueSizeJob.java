package com.qianfeng.smsplatform.monitor.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.qianfeng.smsplatform.monitor.feign.ChannelFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Classname MonitorQueueSizeJob
 * @Description TODO
 * @Date 2019/12/3 21:02
 * @Created by sunjiangwei
 */
@Component
public class MonitorQueueSizeJob implements SimpleJob {
    @Autowired
    private ChannelFeign channelFeign;

    @Override
    public void execute(ShardingContext shardingContext) {
        List<Long> allChannel = channelFeign.getAllChannel();
        System.err.println(allChannel);
    }
}
