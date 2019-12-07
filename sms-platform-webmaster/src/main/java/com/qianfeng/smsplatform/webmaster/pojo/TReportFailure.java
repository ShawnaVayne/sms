package com.qianfeng.smsplatform.webmaster.pojo;


import java.io.Serializable;

public class TReportFailure implements Serializable {

  private long id;
  private String mobile;
  private long state;
  private String errorCode;
  private long srcId;
  private long clientId;
  private String msgId;
  private long sendCount;
  private long failureCount;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }


  public long getState() {
    return state;
  }

  public void setState(long state) {
    this.state = state;
  }


  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }


  public long getSrcId() {
    return srcId;
  }

  public void setSrcId(long srcId) {
    this.srcId = srcId;
  }


  public long getClientId() {
    return clientId;
  }

  public void setClientId(long clientId) {
    this.clientId = clientId;
  }


  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }


  public long getSendCount() {
    return sendCount;
  }

  public void setSendCount(long sendCount) {
    this.sendCount = sendCount;
  }

}
