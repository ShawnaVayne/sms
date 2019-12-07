package com.qianfeng.smsplatform.common.constants;

public class RabbitMqConsants {
    public final static String TOPIC_PRE_SEND = "eleven_pre_send_sms_topic";
    //下发日志TOPIC
    public final static String TOPIC_SMS_SEND_LOG = "eleven_sms_send_log_topic";
    //推送状态报告TOPIC
    public final static String TOPIC_PUSH_SMS_REPORT = "eleven_push_sms_report_topic";
    //状态报告更新TOPIC
    public final static String TOPIC_UPDATE_SMS_REPORT = "eleven_report_update_topic";
    //待发送短信网关队列 + 网关ID号
    public final static String TOPIC_SMS_GATEWAY = "eleven_sms_send_gateway_";

}
