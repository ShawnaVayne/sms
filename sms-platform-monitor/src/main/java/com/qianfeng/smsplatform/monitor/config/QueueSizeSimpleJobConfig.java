package com.qianfeng.smsplatform.monitor.config;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.qianfeng.smsplatform.monitor.job.queuejob.QueueSizeSimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname GatewaySimpleJobConfig
 * @Description TODO
 * @Date 2019/12/5 22:23
 * @Created by sunjiangwei
 */
@Configuration
public class QueueSizeSimpleJobConfig {
    /**
     * 给作业配置注入一个注册中心（zookerpper）
     */
    @Autowired
    private ZookeeperRegistryCenter regCenter;

    /**
     *
     * @return 返回一个自定义简单作业
     */
    @Bean
    public SimpleJob simpleJob(){
        return new QueueSizeSimpleJob();
    }

    /**
     * todo:创建作业调度器，并将作业和一些执行参数赋值给该调度器
     * @param simpleJob
     * @param cron
     * @param shardingTotalCount
     * @param shardingItemParamters
     * @return 返回一个调度器
     */
    @Bean(initMethod = "init")
    public JobScheduler simpleJobScheduler(
            final SimpleJob simpleJob,
            @Value("${monitorQueueSizeJob.cron}") final String cron,
            @Value("${monitorQueueSizeJob.shardingTotalCount}") final int shardingTotalCount,
            @Value("${monitorQueueSizeJob.shardingItemParameters}") final String shardingItemParamters
    ){
        return new SpringJobScheduler(simpleJob,regCenter,getLiteJobConfiguration(simpleJob.getClass(),cron,shardingTotalCount,shardingItemParamters));
    }

    /**
     *
     * @param jobClass
     * @param cron
     * @param shardingTotalCount
     * @param shardingItemParamters
     * @return 返回Lite作业配置
     */
    public LiteJobConfiguration getLiteJobConfiguration(
            final Class<? extends SimpleJob> jobClass,
            final String cron,
            final int shardingTotalCount,
            final String shardingItemParamters
    ){
        return LiteJobConfiguration.newBuilder(
            new SimpleJobConfiguration(JobCoreConfiguration.newBuilder(jobClass.getName(),cron,shardingTotalCount).shardingItemParameters(shardingItemParamters).build(),jobClass.getCanonicalName())
        ).overwrite(true).build();
    }

}
