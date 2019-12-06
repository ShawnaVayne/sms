package com.qianfeng.smsplatform.search.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author simon
 * 2019/12/4 11:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElasticSerachBean {
    /**
     * 客户端id
     */
    private int clientId;
    /**
     * 下发的源号码
     */
    private String srcNumber;
    /**
     * 消息优先级 0 最低 -- 3 最高
     */
    private short messagePriority;
    /**
     * 客户端侧唯一序列号
     */
    private long srcSequenceId;
    /**
     * 网关id
     */
    private int gatewayId;
    /**
     * 产品编号
     */
    private int productId;
    /**
     * 目标手机号
     */
    private String destMobile;
    /**
     * 短信内容
     */
    private String messageContent;
    /**
     * 响应返回的运营商编号
     */
    private String msgId;
    /**
     * 手机接收状态 0 成功 1 等待 2 失败
     */
    private int reportState;
    /**
     * 状态错误码
     */
    private String errorCode;
    /**
     * 发送时间
     */
    private Date sendTime;
    /**
     * 运营商id
     */
    private Integer operatorId;
    /**
     * 省
     */
    private Integer provinceId;
    /**
     * 市id
     */
    private Integer cityId;
    /**
     * 发送方式 1 HTTP 2 WEB
     */
    private Integer source;
}
