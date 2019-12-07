package com.qianfeng.smsplatform.webmaster.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.webmaster.dao.TClientBusinessMapper;
import com.qianfeng.smsplatform.webmaster.dto.DataGridResult;
import com.qianfeng.smsplatform.webmaster.dto.QueryDTO;
import com.qianfeng.smsplatform.webmaster.feign.CacheFeign;
import com.qianfeng.smsplatform.webmaster.pojo.TClientBusiness;
import com.qianfeng.smsplatform.webmaster.pojo.TClientBusinessExample;
import com.qianfeng.smsplatform.webmaster.service.ClientBusinessService;
import com.qianfeng.smsplatform.webmaster.util.JsonUtils;
import com.qianfeng.smsplatform.webmaster.util.MD5Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class ClientBusinessServiceImpl implements ClientBusinessService {

    @Autowired
    private TClientBusinessMapper tClientBusinessMapper;

    @Autowired
    private CacheFeign cacheFeign;

    @Autowired
    private ObjectMapper objectMapper;

    private Logger logger = LoggerFactory.getLogger(ClientBusinessServiceImpl.class);


    @Override
    public int addClientBusiness(TClientBusiness tClientBusiness) {
        String pwd = tClientBusiness.getPwd();
        String build = MD5Builder.build(pwd, "UTF-8");
        String md5PASS = build.toUpperCase();
        tClientBusiness.setPwd(md5PASS);
        //持久层已经获得返回的主键
        int i = tClientBusinessMapper.insertSelective(tClientBusiness);
        Map<String, String> map1 = JsonUtils.objectToMap(tClientBusiness);
        try {
            //将用户添加的客户添加到缓存中
            String objJson = objectMapper.writeValueAsString(tClientBusiness);
            logger.info("用户添加的客户，改成json格式：{}",objJson);
            cacheFeign.hMSet(CacheConstants.CACHE_PREFIX_CLIENT + tClientBusiness.getId(), objJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return i;
    }

    @Override
    public int delClientBusiness(Long id) {
        //删除缓存中数据
        cacheFeign.del(CacheConstants.CACHE_PREFIX_CLIENT+id);
        return tClientBusinessMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateClientBusiness(TClientBusiness tClientBusiness) {
        try {
            //将修改后的客户对象放入缓存中
            String json = objectMapper.writeValueAsString(tClientBusiness);
            logger.info("json对象：{}",json);
            cacheFeign.hMSet(CacheConstants.CACHE_PREFIX_CLIENT+tClientBusiness.getId(),json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        int i = tClientBusinessMapper.updateByPrimaryKey(tClientBusiness);
        return i;
    }

    @Override
    public TClientBusiness findById(Long id) {
        return tClientBusinessMapper.selectByPrimaryKey(id);
    }


    @Override
    public List<TClientBusiness> findAll() {
        return tClientBusinessMapper.selectByExample(null);
    }

    @Override
    public DataGridResult findByPage(QueryDTO queryDTO) {
        PageHelper.offsetPage(queryDTO.getOffset(), queryDTO.getLimit());
        TClientBusinessExample example = new TClientBusinessExample();
        String sort = queryDTO.getSort();
        if (!StringUtils.isEmpty(sort)) {
            example.setOrderByClause("id");
        }
        List<TClientBusiness> tClientBusinesses = tClientBusinessMapper.selectByExample(example);
        for (TClientBusiness tClientBusiness : tClientBusinesses) {
            Long id = tClientBusiness.getId();
//            Integer paidValueStr  = (Integer)cacheService.getObject("CUSTOMER_FEE:" +id);
//            if(!StringUtils.isEmpty(paidValueStr)){
//                int i = paidValueStr/1000;
//                tClientBusiness.setPaidValueStr(i+"元");
//            }
        }
        PageInfo<TClientBusiness> info = new PageInfo<>(tClientBusinesses);
        long total = info.getTotal();
        DataGridResult result = new DataGridResult(total, tClientBusinesses);
        return result;
    }


}
