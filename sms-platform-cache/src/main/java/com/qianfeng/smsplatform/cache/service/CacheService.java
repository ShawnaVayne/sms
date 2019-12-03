package com.qianfeng.smsplatform.cache.service;

import java.util.Map;
import java.util.Set;

/**
 * 缓存接口模块
 * @author simon
 * 2019/12/3 9:31
 */
public interface CacheService {
    Boolean set(String key, Object value, int expireTime);

    /**
     * todo:存储为
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    Boolean set(String key, String value, int expireTime);

    /**
     * todo:设置一个object类型的key-value
     * @param key
     * @param value
     * @return
     */
    Boolean set(String key, Object value);

    Boolean set(String key, String value);

    /**
     * todo：根据key获取到缓存中的值
     * @param key key
     * @return
     */
    String get(String key);

    /**
     * todo：获取并同时设置新值
     * @param key
     * @param value 新值
     * @return
     */
    Object getAndSet(String key, String value);

    /**
     * todo：根据key获取到缓存中的对象
     * @param key
     * @return
     */
    Object getObject(String key);

    long size(String key);

    /**
     * todo：删除一些key
     * @param keys
     */
    void del(String... keys);

    /**
     * todo：给一个key设置过期时间
     * @param key
     * @param seconds
     * @return
     */
    boolean expire(String key, long seconds);

    long getExpire(String key);

    /**
     * todo:为用户账户充值
     * @param key 用户账户，类型ELEVEN_CUSTOMER_FEE:+费用
     * @param delta
     * @return
     */
    long incr(String key, long delta);

    long decr(String key, long delta);

    Set<String> keys(String pattern);

    /**
     * todo:根据key，查询map
     * @param key 查询主key
     * @return 含有具体信息的map
     */
    Map<Object, Object> hmget(String key);

    /**
     * todo：缓存为MAP格式，如果是客户路由缓存，请用ELEVEN_ROUTER:+客户id形式，如果是客户信息缓存，请用ELEVEN_CLIENT:+客户id形式，其他基本信息存入map
     * @param key 如：ELEVEN_ROUTER:+客户id
     * @param map 基本信息，比如用户密码，用户ip等
     * @return 是否插入成功
     */
    boolean hmset(String key, Map<String, Object> map);
}
