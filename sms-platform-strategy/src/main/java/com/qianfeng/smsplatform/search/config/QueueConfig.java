package com.qianfeng.smsplatform.search.config;


import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author simon
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
