package com.qianfeng.smsplatform.monitor.job.queuejob;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.monitor.feign.ChannelFeign;
import com.qianfeng.smsplatform.monitor.utils.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @Classname QueueSizeSimpleJob
 * @Description TODO
 * @Date 2019/12/5 22:18
 * @Created by sunjiangwei
 */
@Slf4j
public class QueueSizeSimpleJob implements SimpleJob {
    @Autowired
    private ChannelFeign channelFeign;
    @Value("${alert.gatewayQueue.size}")
    private Long messageCountLimit;

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private Integer port;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void execute(ShardingContext shardingContext) {
        List<Long> allChannel = channelFeign.getAllChannel();
        for (Long aLong : allChannel) {
            String gateWayQueue = RabbitMqConsants.TOPIC_SMS_GATEWAY + aLong;
            //log.info("{}对应的队列：{}",aLong,gateWayQueue);
            try {
                Long messageCount = getMessageCount(gateWayQueue);
                if (messageCount > messageCountLimit) {
                    log.info("{}队列消息堆积量达到上限，请注意查收~~~",gateWayQueue);
                    System.err.println(gateWayQueue+"队列消息堆积量达到上限，请注意查收~~~");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }


    //获取网关队列中有多少消息
    public Long getMessageCount(String queueName) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection(host,port,username,password,virtualHost);
        Channel channel = connection.createChannel();
        return channel.messageCount(queueName);
    }
}
