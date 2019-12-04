package com.qianfeng.smsplatform.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author simon
 * 2019/12/4 11:53
 */
@Configuration
public class ElasticSearchConfig {
    @Value("${spring.data.elasticsearch.host}")
    private String host;
    @Value("${spring.data.elasticsearch.port}")
    private String port;

    @Bean
    public RestHighLevelClient client(){
        System.err.println("port:"+port+" 类型："+port.getClass());
        RestClientBuilder builder = RestClient.builder(new HttpHost(host, Integer.parseInt(port)));
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }
}
