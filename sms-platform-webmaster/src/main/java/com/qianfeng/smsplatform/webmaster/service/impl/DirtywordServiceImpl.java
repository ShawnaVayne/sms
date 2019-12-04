package com.qianfeng.smsplatform.webmaster.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.webmaster.dao.TDirtywordMapper;
import com.qianfeng.smsplatform.webmaster.dto.DataGridResult;
import com.qianfeng.smsplatform.webmaster.dto.QueryDTO;
import com.qianfeng.smsplatform.webmaster.feign.CacheFeign;
import com.qianfeng.smsplatform.webmaster.pojo.TDirtyword;
import com.qianfeng.smsplatform.webmaster.pojo.TDirtywordExample;
import com.qianfeng.smsplatform.webmaster.service.DirtywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DirtywordServiceImpl implements DirtywordService {

    @Autowired
    private TDirtywordMapper tDirtywordMapper;

    //注入缓存feign接口对象
    @Autowired
    private CacheFeign cacheFeign;


    @Override
    public int addDirtyword(TDirtyword tDirtyword) {
        //将用户添加的脏词，存入缓存中
        cacheFeign.set(CacheConstants.CACHE_PREFIX_DIRTYWORDS+tDirtyword.getDirtyword(),"1");

        return tDirtywordMapper.insertSelective(tDirtyword);
    }

    @Override
    public int delDirtyword(Long id) {
        TDirtyword tDirtyword = findById(id);
        //根据key值删除redis中的脏词
        cacheFeign.del(CacheConstants.CACHE_PREFIX_DIRTYWORDS+tDirtyword.getDirtyword());
        return tDirtywordMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateDirtyword(TDirtyword tDirtyword) {
        //根据客户端传来的对象，获取对象的id，并根据id查询数据库中的对象
        Long id = tDirtyword.getId();
        TDirtyword tDirtywordOld = tDirtywordMapper.selectByPrimaryKey(id);
        //删除redis中存储的旧的脏词
        cacheFeign.del(CacheConstants.CACHE_PREFIX_DIRTYWORDS+tDirtywordOld.getDirtyword());
        int i = tDirtywordMapper.updateByPrimaryKey(tDirtyword);
        //添加用户端传来的新的脏词
        cacheFeign.set(CacheConstants.CACHE_PREFIX_DIRTYWORDS+tDirtyword.getDirtyword(),"1");
        return i;
    }

    @Override
    public TDirtyword findById(Long id) {
        return tDirtywordMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TDirtyword> findAll() {
        return tDirtywordMapper.selectByExample(null);
    }

    @Override
    public DataGridResult findByPage(QueryDTO queryDTO) {
        PageHelper.offsetPage(queryDTO.getOffset(),queryDTO.getLimit());
        TDirtywordExample example = new TDirtywordExample();
        String sort = queryDTO.getSort();
        if(!StringUtils.isEmpty(sort)){
            example.setOrderByClause("id");
        }
        List<TDirtyword> tDirtywords = tDirtywordMapper.selectByExample(example);
        PageInfo<TDirtyword> info = new PageInfo<>(tDirtywords);
        long total = info.getTotal();
        DataGridResult result = new DataGridResult(total,tDirtywords);
        return result;
    }


}
