package com.qianfeng.smsplatform.monitor.config;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname ElasticRegCenterConfig
 * @Description TODO
 * @Date 2019/12/3 20:53
 * @Created by sunjiangwei
 */

/**
 * 配置zookeeper注册中心
 */
@Configuration
public class ElasticRegCenterConfig {

    @Value("${regCenter.serverList}")
    private String serverList;

    @Value("${regCenter.namespace}")
    private String namespace;

    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter registryCenter(){
        ZookeeperConfiguration zcf = new ZookeeperConfiguration(serverList,namespace);
        //设置重试次数
        zcf.setMaxRetries(6);
        //设置超时时间
        zcf.setSessionTimeoutMilliseconds(300000);
        return new ZookeeperRegistryCenter(zcf);
    }
}
