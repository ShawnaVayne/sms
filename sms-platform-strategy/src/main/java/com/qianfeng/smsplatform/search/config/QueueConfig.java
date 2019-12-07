package com.qianfeng.smsplatform.search.config;


import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.search.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author 徐胜涵
 */
@Configuration
public class QueueConfig {

    //下发日志
    @Bean
    public Queue logQueue() {
        return new Queue(RabbitMqConsants.TOPIC_SMS_SEND_LOG);
    }
    //状态报告
    public @Bean Queue reportQueue() {
        return new Queue(RabbitMqConsants.TOPIC_PUSH_SMS_REPORT);
    }
    //更新状态报告
    public @Bean Queue updateQueue() {
        return new Queue(RabbitMqConsants.TOPIC_UPDATE_SMS_REPORT);
    }

}
