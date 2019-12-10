package com.qianfeng.smsplatform.search.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.search.service.SearchService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author simon
 * 2019/12/4 23:24
 */
@Component
@Slf4j
public class LogChannelListen {
    @Value("${elasticsearch.index.submit.name}")
    private String submitIndexName;
    @Value("${elasticsearch.index.submit.type}")
    private String submitTypeName;
    @Autowired
    private SearchService searchService;
    @Autowired
    private ObjectMapper objectMapper;
    @RabbitListener(queues = RabbitMqConsants.TOPIC_SMS_SEND_LOG)
    public void handleMsgFormSendSmsTopic(GenericMessage message) throws IOException {
        log.error("收到来自{}的信息是{}",RabbitMqConsants.TOPIC_SMS_SEND_LOG,message);
        Standard_Submit submitLog = (Standard_Submit) message.getPayload();
        boolean result = searchService.addToLog(submitIndexName,submitTypeName,submitLog.getMsgid(),objectMapper.writeValueAsString(message.getPayload()));
        if(result){
            log.error("插入成功！");
        }else {
            log.error("插入失败！");
        }
    }
    @RabbitListener(queues = RabbitMqConsants.TOPIC_UPDATE_SMS_REPORT)
    public void handleUpdateSmsReport(GenericMessage message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,Channel channel) throws IOException {
        log.error("收到来自{}的信息是{}",RabbitMqConsants.TOPIC_UPDATE_SMS_REPORT,message);
        Standard_Report report = (Standard_Report) message.getPayload();
        System.err.println(report);
        String msgId = report.getMsgId();
        try {
            boolean result = searchService.updateLog(submitIndexName, submitTypeName, msgId, objectMapper.writeValueAsString(report));
            if(result){
                log.error("插入成功！");
            }else {
                log.error("插入失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            channel.basicAck(deliveryTag,false);
        }
    }
}
