package com.qianfeng.smsplatform.search.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @RabbitListener(queues = RabbitMqConsants.TOPIC_PRE_SEND)
    public void handleMsgFormSendSmsTopic(GenericMessage message) throws IOException {
        log.error("收到来自{}的信息是{}",RabbitMqConsants.TOPIC_PRE_SEND,message);
        boolean result = searchService.addToLog(submitIndexName,submitTypeName,objectMapper.writeValueAsString(message.getPayload()));
        if(result){
            log.error("插入成功！");
        }else {
            log.error("插入失败！");
        }
    }
    @RabbitListener(queues = RabbitMqConsants.TOPIC_UPDATE_SMS_REPORT)
    public void handleUpdateSmsReport(GenericMessage message) throws IOException {
        log.error("收到来自{}的信息是{}",RabbitMqConsants.TOPIC_UPDATE_SMS_REPORT,message);
        Standard_Report report = (Standard_Report) message.getPayload();
        String msgId = report.getMsgId();
        boolean result = searchService.updateLog(submitIndexName,submitTypeName,msgId,message);
        if(result){
            log.error("插入成功！");
        }else {
            log.error("插入失败！");
        }
    }
}
