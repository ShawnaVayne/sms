package com.qianfeng.smsplatform.webmaster.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.webmaster.dao.TBlackListMapper;
import com.qianfeng.smsplatform.webmaster.dto.DataGridResult;
import com.qianfeng.smsplatform.webmaster.dto.QueryDTO;
import com.qianfeng.smsplatform.webmaster.feign.CacheFeign;
import com.qianfeng.smsplatform.webmaster.pojo.TBlackList;
import com.qianfeng.smsplatform.webmaster.pojo.TBlackListExample;
import com.qianfeng.smsplatform.webmaster.service.BlackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class BlackServiceImpl implements BlackService {
    private Logger logger = LoggerFactory.getLogger(BlackServiceImpl.class);

    @Autowired
    private TBlackListMapper tBlackListMapper;

    //自动注入Cache模块的接口对象
    @Autowired
    private CacheFeign cacheFeign;


    @Override
    public int addBlack(TBlackList tBlackList) {
        logger.info("要插入的key：{}", CacheConstants.CACHE_PREFIX_BLACK + tBlackList.getMobile());
        //调用cacheFeign对象中的方法，实现往缓存中黑名单中添加手机号
        cacheFeign.set(CacheConstants.CACHE_PREFIX_BLACK + tBlackList.getMobile(), "1");
        return tBlackListMapper.insertSelective(tBlackList);
    }

    @Override
    public int delBlack(Long id) {
        TBlackList tBlackList = findById(id);
        //根据id删除黑名单中的某个手机号
        cacheFeign.del(CacheConstants.CACHE_PREFIX_BLACK + tBlackList.getMobile());
        return tBlackListMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateBlack(TBlackList tBlackList) {
        Long id = tBlackList.getId();
        //查询数据库原始的对象，为对手机号进行修改
        TBlackList tBlackListOld = tBlackListMapper.selectByPrimaryKey(id);
        //将为修改的手机号从黑名单中删除
        cacheFeign.del(CacheConstants.CACHE_PREFIX_BLACK + tBlackListOld.getMobile());

        int i = tBlackListMapper.updateByPrimaryKey(tBlackList);

        //将修改后的手机号放入黑名单中
        cacheFeign.set(CacheConstants.CACHE_PREFIX_BLACK + tBlackList.getMobile(), "1");
        return i;
    }

    @Override
    public TBlackList findById(Long id) {
        return tBlackListMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TBlackList> findAll() {
        return tBlackListMapper.selectByExample(null);
    }

    @Override
    public DataGridResult findByPage(QueryDTO queryDTO) {
        PageHelper.offsetPage(queryDTO.getOffset(), queryDTO.getLimit());
        TBlackListExample example = new TBlackListExample();
        String sort = queryDTO.getSort();
        if (!StringUtils.isEmpty(sort)) {
            example.setOrderByClause("id");
        }
        List<TBlackList> tBlackLists = tBlackListMapper.selectByExample(example);
        PageInfo<TBlackList> info = new PageInfo<>(tBlackLists);
        long total = info.getTotal();
        DataGridResult result = new DataGridResult(total, tBlackLists);
        return result;
    }
}
