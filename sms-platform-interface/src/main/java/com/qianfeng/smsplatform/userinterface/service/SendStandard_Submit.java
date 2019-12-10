package com.qianfeng.smsplatform.userinterface.service;

import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;

/**
 * 2019/12/316:28
 * <p>
 * 未知的事情 永远充满变数
 */
public interface SendStandard_Submit {

    //发送短信
    public void setMessage(String queue, Standard_Submit standard_submit);

    //从发状态报告
      public void sendstatus(String queue, Standard_Report report);


}
