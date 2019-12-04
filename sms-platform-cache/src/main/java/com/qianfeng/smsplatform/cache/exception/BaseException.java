package com.qianfeng.smsplatform.cache.exception;

/**
 * @author simon
 * 2019/12/3 9:52
 */
public class BaseException extends RuntimeException {
    String code;
    String msg;

    public BaseException() {
    }

    public BaseException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BaseException{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
