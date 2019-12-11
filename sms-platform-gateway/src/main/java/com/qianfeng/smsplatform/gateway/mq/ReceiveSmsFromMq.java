package com.qianfeng.smsplatform.gateway.mq;

import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.gateway.netty4.NettyClient;
import com.qianfeng.smsplatform.gateway.netty4.Utils.Command;
import com.qianfeng.smsplatform.gateway.netty4.Utils.MsgUtils;
import com.qianfeng.smsplatform.gateway.netty4.entity.CmppMessageHeader;
import com.qianfeng.smsplatform.gateway.netty4.entity.CmppSubmit;
import com.qianfeng.smsplatform.gateway.netty4.entity.ControlWindow;
import com.qianfeng.smsplatform.gateway.thread.SendReportThread;
import com.qianfeng.smsplatform.gateway.thread.SendSubmitRespThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static com.qianfeng.smsplatform.common.constants.RabbitMqConsants.TOPIC_PRE_SEND;
import static com.qianfeng.smsplatform.common.constants.RabbitMqConsants.TOPIC_UPDATE_SMS_REPORT;
import static com.qianfeng.smsplatform.gateway.config.RabbitConfig.QUEUE_DELAY_PER_MESSAGE_TTL_MSG_SMS_SEND;

@Component
public class ReceiveSmsFromMq {
    private final static Logger log = LoggerFactory.getLogger(ReceiveSmsFromMq.class);


    @Autowired
    private NettyClient nettyClient;
    @Value("${gateway.longcode}")
    private String longcode;

//    @Value("${gateway.sendtopic}")
//    private String sendtopic;
    /**
     * 消息接受  并发消费
     */
    @RabbitListener(queues = {"eleven_sms_send_gateway_1","eleven_sms_send_gateway_2","eleven_sms_send_gateway_3"}, containerFactory = "pointTaskContainerFactory")
    public void receive(Standard_Submit submit) throws IOException, InterruptedException {
        SendReportThread sendReportThread = new SendReportThread(RabbitMqConsants.TOPIC_PUSH_SMS_REPORT, RabbitMqConsants.TOPIC_UPDATE_SMS_REPORT, new RabbitTemplate());
        SendSubmitRespThread sendSubmitRespThread = new SendSubmitRespThread(RabbitMqConsants.TOPIC_SMS_SEND_LOG, new RabbitTemplate());
        //发送下发日志
        new Thread(sendSubmitRespThread).start();
        //延时发送
        Thread.sleep(10);
        //开启发送更新状态报告队列和状态报告队列
        new Thread(sendReportThread).start();
        log.info("接收消息:{}" + submit);
        String mobile=submit.getDestMobile();
        String content=submit.getMessageContent();
        int sequenceId =  MsgUtils.getSequence();
        log.info("sequenceId="+ sequenceId);
        String srcId = longcode + submit.getSrcNumber();
        submit.setSrcNumber(srcId);
        CmppMessageHeader submitMessage=new CmppSubmit(Command.CMPP2_VERSION, "", srcId, sequenceId, mobile, content);
        ControlWindow controlWindow = ControlWindow.getInstance();
        log.info("enter window seqID:{},submit:{}",sequenceId,submit);
        controlWindow.put(sequenceId,submit);
        nettyClient.submit(submitMessage);
        Thread.sleep(200);
    }

    /**
     * 消息接受  并发消费 TOPIC_UPDATE_SMS_REPORT  QUEUE_DELAY_PER_MESSAGE_TTL_MSG_SMS_SEND
     */
//    @RabbitListener(queues = TOPIC_UPDATE_SMS_REPORT, containerFactory = "pointTaskContainerFactory")
//    public void receive(Standard_Report report) throws Exception {
//        log.info("接收延迟消息:{}" , report);
//    }
}
