package com.qianfeng.smsplatform.monitor.config;

import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.qianfeng.smsplatform.monitor.util.ElasticJobUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname MonitorQueueSizeJobConfig
 * @Description TODO
 * @Date 2019/12/3 21:12
 * @Created by sunjiangwei
 */
@Configuration
public class MonitorQueueSizeJobConfig {

    @Autowired
    private ZookeeperRegistryCenter registryCenter;

    @Autowired
    private MonitorQueueSizeJob monitorQueueSizeJob;

    @Value("${monitorQueueSizeJob.cron}")
    private String cron;

    @Value("${monitorQueueSizeJob.shardingTotalCount}")
    private int shardingTotalCount;

    @Value("${monitorQueueSizeJob.shardingItemParameters}")
    private String shardingItemParameters;

    @Bean(initMethod = "init")
    public JobScheduler queueSizeScheduler(){
        return new SpringJobScheduler(
                monitorQueueSizeJob,
                registryCenter,
                ElasticJobUtils.getSimpleJobConfiguration(
                      monitorQueueSizeJob.getClass(),cron,shardingTotalCount,shardingItemParameters
                ));
    }
}
