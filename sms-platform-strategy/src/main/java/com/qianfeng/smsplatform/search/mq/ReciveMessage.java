package com.qianfeng.smsplatform.search.mq;

import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.common.constants.RabbitMqConsants;
import com.qianfeng.smsplatform.common.constants.StrategyConstants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.search.config.QueueConfig;
import com.qianfeng.smsplatform.search.feign.CacheFeignClient;
import com.qianfeng.smsplatform.search.service.MyFilter;
import com.qianfeng.smsplatform.search.service.impl.SmsBlackFilter;
import com.qianfeng.smsplatform.search.util.CheckPhone;
import com.sun.javafx.sg.prism.CacheFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class ReciveMessage {

    @Autowired
    private Map<String, MyFilter> filterList;
    @Value("${smsplatform.filters}")
    private String filterNames;
    @Autowired
    private SendMessage sendMessage;

    @Autowired
    private CacheFeignClient cacheFeignClient;

    @Autowired
    private CreateQueue createQueue;

    /**
     * todo: 每次接受信息进行处理
     *
     * @param submit
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws TimeoutException
     */
    @RabbitListener(queues = RabbitMqConsants.TOPIC_PRE_SEND, autoStartup = "true", containerFactory = "customContainerFactory")
    public void getMessage(Standard_Submit submit) throws IOException, ClassNotFoundException, TimeoutException {

        String[] filterNameArray = filterNames.split(",");
        //从mq中反序列化发送来的对象
        Standard_Report report = new Standard_Report();
        //客户ID
        report.setClientID(submit.getClientID());
        //目的手机号
        report.setMobile(submit.getDestMobile());
        //客户侧唯一序列号
        report.setSrcID(submit.getSrcSequenceId());
        //待发送状态
        report.setState(1);

        //从缓存中读取手机号
        Map<Object, Object> hmget = cacheFeignClient.hmget(CacheConstants.CACHE_PREFIX_CLIENT + submit.getClientID());
        if (hmget != null && hmget.size() > 0) {
            String mobile = (String) hmget.get("mobile");
            if (mobile != null) {
                submit.setSrcNumber(mobile);
                //检验发送人手机号
                if (CheckPhone.isPhoneOrTel(submit.getSrcNumber())) {
                    //检验接收人手机号
                    if (CheckPhone.isPhoneOrTel(submit.getDestMobile())) {
                        //移动
                        if (CheckPhone.isChinaMobilePhoneNum(submit.getSrcNumber())) {
                            submit.setOperatorId(1);
                        }
                        //联通
                        if (CheckPhone.isChinaUnicomPhoneNum(submit.getSrcNumber())) {
                            submit.setOperatorId(2);
                        }
                        //电信
                        if (CheckPhone.isChinaTelecomPhoneNum(submit.getSrcNumber())) {
                            submit.setOperatorId(3);
                        }
                        for (int i = 0; i < filterNameArray.length; i++) {
                            //System.out.println(filterNameArray[i]);
                            MyFilter myFilter = filterList.get(filterNameArray[i]);

                            myFilter.doFilter(submit, report);
                            //如果是状态2(发送失败)，就跳出循环
                            if (report.getState() == 2) {
                                break;
                            }
                        }
                        //如果接收人手机号不符合要求
                    } else {
                        report.setErrorCode(StrategyConstants.STRATEGY_ERROR_ROUTER);
                        report.setState(2);
                    }
                    //如果发送人手机号不符合要求
                } else {
                    report.setErrorCode(StrategyConstants.STRATEGY_ERROR_ROUTER);
                    report.setState(2);
                }
                //如果缓存中查询不到客户信息
            } else {
                report.setErrorCode(StrategyConstants.STRATEGY_ERROR_ROUTER);
                report.setState(2);
            }
            //把report的状态同步到submit
            submit.setReportState(report.getState());
            //从缓存中拿优先级
            //缓存中的优先级数据类型是Integer
            Integer priority = (Integer) hmget.get("priority");
            log.info("优先级{}", priority);
            //先把优先级转换为字符串，再转为short
            submit.setMessagePriority(new Short(priority + ""));
        }
        log.info("发送人手机号：{}", submit.getSrcNumber());

        //如果过滤成功
        if (report.getErrorCode() == null) {
            report.setErrorCode("DELIVER");
            submit.setErrorCode(report.getErrorCode());
        }
        //int类型成员变量默认值为0，如果网关id为0，说明没有查到或者没有执行到这一步就被拦截，此时不需要创建网关队列
        if (submit.getGatewayID() != 0) {
            log.info("创建队列：{}", RabbitMqConsants.TOPIC_SMS_GATEWAY + submit.getGatewayID());
            //动态创建网关队列
            createQueue.createQueue(RabbitMqConsants.TOPIC_SMS_GATEWAY + submit.getGatewayID());
        }
        //如果状态不是发送失败状态
        if (report.getState() != 2) {
            log.info("发送待发日志");
            //序列化submit，将submit发送到网关队列
            //发送待发日志
            log.error("submit:{}", submit);
            //发送成功
            report.setState(0);
            sendMessage.sendMessage(RabbitMqConsants.TOPIC_SMS_GATEWAY + submit.getGatewayID(), submit);

        } else {
            //设置msgid
            submit.setMsgid(UUID.randomUUID().toString());
            log.info("发送下发日志");
            //发送下发日志
            log.error("submit:{}", submit);
            submit.setErrorCode(report.getErrorCode());
            sendMessage.sendMessage(RabbitMqConsants.TOPIC_SMS_SEND_LOG, submit);
        }
        //http方式推送，web方式不推送
        if (submit.getSource() == 1) {
            //发送状态报告
            log.info("发送状态报告");
            log.error("report:{}", report);

            sendMessage.sendMessage(RabbitMqConsants.TOPIC_PUSH_SMS_REPORT, report);
        }
    }

}
