package com.qianfeng.smsplatform.search.web;

import com.qianfeng.smsplatform.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author simon
 * 2019/12/6 19:14
 */
@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @RequestMapping("/list/{jsonParam}")
    public List<Map> search(@PathVariable String jsonParam) throws IOException, ParseException {
        System.err.println("接收到的接送字符串："+jsonParam);
        List<Map> search = searchService.search(jsonParam);
        return search;
    }

    @RequestMapping("/getCount/{jsonParam}")
    public Map<String,Long> getCount(@PathVariable String jsonParam) throws IOException, ParseException {
        System.err.println("接收到的接送字符串："+jsonParam);
        Map<String, Long> count = searchService.getCount(jsonParam);
        return count;
    }
}
