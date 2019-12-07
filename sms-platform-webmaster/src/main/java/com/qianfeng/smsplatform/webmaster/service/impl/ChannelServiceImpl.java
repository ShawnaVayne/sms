package com.qianfeng.smsplatform.webmaster.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.webmaster.dao.TChannelMapper;
import com.qianfeng.smsplatform.webmaster.dto.DataGridResult;
import com.qianfeng.smsplatform.webmaster.dto.QueryDTO;
import com.qianfeng.smsplatform.webmaster.feign.CacheFeign;
import com.qianfeng.smsplatform.webmaster.pojo.TChannel;
import com.qianfeng.smsplatform.webmaster.pojo.TChannelExample;
import com.qianfeng.smsplatform.webmaster.pojo.TPhase;
import com.qianfeng.smsplatform.webmaster.pojo.TPhaseExample;
import com.qianfeng.smsplatform.webmaster.service.ChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private TChannelMapper tChannelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CacheFeign cacheFeign;

    private Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);

    @Override
    public int addChannel(TChannel tChannel) {
        int i = tChannelMapper.insertSelective(tChannel);
        try {
            //向缓存中添加用户添加的通道对象的信息
            String json = objectMapper.writeValueAsString(tChannel);
            cacheFeign.hMSet(CacheConstants.CACHE_PREFIX_ROUTER+tChannel.getId(),json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return i;
    }

    @Override
    public int delChannel(Long id) {
        //删除缓存中通道信息
        cacheFeign.del(CacheConstants.CACHE_PREFIX_ROUTER+id);
        return tChannelMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateChannel(TChannel tChannel) {
        //修改通道信息
        Long id = tChannel.getId();
        try {
            String json = objectMapper.writeValueAsString(tChannel);
            cacheFeign.hMSet(CacheConstants.CACHE_PREFIX_ROUTER+id,json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return tChannelMapper.updateByPrimaryKey(tChannel);
    }

    @Override
    public TChannel findById(Long id) {
        return tChannelMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TChannel> findALL() {
        return tChannelMapper.selectByExample(null);
    }

    @Override
    public DataGridResult findByPage(QueryDTO queryDTO) {
        PageHelper.offsetPage(queryDTO.getOffset(), queryDTO.getLimit());
        TChannelExample example = new TChannelExample();
        String sort = queryDTO.getSort();
        if (!StringUtils.isEmpty(sort)) {
            example.setOrderByClause("id");
        }
        List<TChannel> tChannels = tChannelMapper.selectByExample(example);
        PageInfo<TChannel> info = new PageInfo<>(tChannels);
        long total = info.getTotal();
        DataGridResult result = new DataGridResult(total, tChannels);
        return result;
    }

}
