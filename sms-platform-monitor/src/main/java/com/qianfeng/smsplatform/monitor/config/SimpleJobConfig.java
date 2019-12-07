package com.qianfeng.smsplatform.monitor.config;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.qianfeng.smsplatform.monitor.job.feejob.FeeSimpeJob;
import com.qianfeng.smsplatform.monitor.job.queuejob.QueueSizeSimpleJob;
import com.qianfeng.smsplatform.monitor.job.reportFailure.ReportFailureListenerJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname FeeSimpleJobConfig
 * @Description TODO
 * @Date 2019/12/6 10:09
 * @Created by sunjiangwei
 */
@Configuration
public class SimpleJobConfig {

    @Autowired
    private ZookeeperRegistryCenter regCenter;

    //返回监控余额的作业
    @Bean
    public SimpleJob simpleJob() {
        return new FeeSimpeJob();
    }

    //监控状态报告发送失败的作业
    @Bean
    public SimpleJob simpleJob2() {
        return new ReportFailureListenerJob();
    }

    //监控队列消息数量的作业
    @Bean
    public SimpleJob simpleJob3(){
        return new QueueSizeSimpleJob();
    }


    //监控余额的调度器
    @Bean(initMethod = "init")
    public JobScheduler simpleJobScheduler(final SimpleJob simpleJob,
                                           @Value("${monitorFeeJob.cron}") final String cron,
                                           @Value("${monitorFeeJob.shardingTotalCount}") final int shardingTotalCount,
                                           @Value("${monitorFeeJob.shardingItemParameters}") final String shardingItemParamters) {
        return new SpringJobScheduler(simpleJob, regCenter, getLiteJobConfiguration(simpleJob.getClass(), cron, shardingTotalCount, shardingItemParamters));
    }

    //监控状态报告发送失败的调度器
    @Bean(initMethod = "init")
    public JobScheduler simpleJobScheduler2(final SimpleJob simpleJob2,
                                           @Value("${monitorReportFailure.cron}") final String cron,
                                           @Value("${monitorReportFailure.shardingTotalCount}") final int shardingTotalCount,
                                           @Value("${monitorReportFailure.shardingItemParameters}") final String shardingItemParamters) {
        return new SpringJobScheduler(simpleJob2, regCenter, getLiteJobConfiguration(simpleJob2.getClass(), cron, shardingTotalCount, shardingItemParamters));
    }

    //监控队列消息数量的调度器
    @Bean(initMethod = "init")
    public JobScheduler simpleJobScheduler3(
            final SimpleJob simpleJob3,
            @Value("${monitorQueueSizeJob.cron}") final String cron,
            @Value("${monitorQueueSizeJob.shardingTotalCount}") final int shardingTotalCount,
            @Value("${monitorQueueSizeJob.shardingItemParameters}") final String shardingItemParamters
    ){
        return new SpringJobScheduler(simpleJob3,regCenter,getLiteJobConfiguration(simpleJob3.getClass(),cron,shardingTotalCount,shardingItemParamters));
    }


    public LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass,
                                                        final String cron,
                                                        final int shardingTotalCount,
                                                        final String shardingIterParamters) {
        return LiteJobConfiguration.newBuilder(
                new SimpleJobConfiguration(JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount).shardingItemParameters(shardingIterParamters).build(),
                        jobClass.getCanonicalName())
        ).overwrite(true).build();
    }
}
