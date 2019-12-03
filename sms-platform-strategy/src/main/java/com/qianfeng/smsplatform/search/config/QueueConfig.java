package com.qianfeng.smsplatform.search.config;


import com.qianfeng.smsplatform.search.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author 徐胜涵
 */
@Component
public class QueueConfig {

    public void declareQueue(String queueName) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, true, false, false, null);
    }
}
