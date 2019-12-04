package com.qianfeng.smsplatform.cache.exception;

/**
 * @author simon
 * 2019/12/3 9:54
 */
public class RedisException extends BaseException {
    public RedisException(String code, String msg) {
        super(code, msg);
    }
}
