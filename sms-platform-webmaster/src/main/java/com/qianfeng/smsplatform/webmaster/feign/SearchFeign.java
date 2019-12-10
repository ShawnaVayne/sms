package com.qianfeng.smsplatform.webmaster.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @Classname SearchFeign
 * @Description TODO
 * @Date 2019/12/10 14:13
 * @Created by sunjiangwei
 */
@FeignClient("SEARCH-SERVICE")
public interface SearchFeign {
    /**
     * 根据条件获取所有的记录
     * @param jsonParam
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/search/list/{jsonParam}",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    List<Map> search(@PathVariable("jsonParam") String jsonParam) throws IOException, ParseException;


    /**
     * 根据条件获取记录的条数
     * @param jsonParam
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/search/getCount/{jsonParam}",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    long getCount(@PathVariable("jsonParam") String jsonParam) throws IOException, ParseException;
}
