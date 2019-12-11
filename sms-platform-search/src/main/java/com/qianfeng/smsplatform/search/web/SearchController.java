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
<<<<<<< HEAD
        System.err.println(jsonParam);
=======
        System.err.println("接收到的接送字符串："+jsonParam);
>>>>>>> 0048517cbb875fc10bc0075bb358bb88aa9d2849
        List<Map> search = searchService.search(jsonParam);
        return search;
    }

    @RequestMapping("/getState/{jsonParam}")
    public Map<String,Long> getState(@PathVariable String jsonParam) throws IOException, ParseException {
        System.err.println("接收到的接送字符串："+jsonParam);
        Map<String, Long> map = searchService.getState(jsonParam);
        return map;
    }

    @RequestMapping("/getCount/{jsonParam}")
    public long getCount(@PathVariable String jsonParam) throws IOException, ParseException {
        System.err.println("接收到的字符串:"+jsonParam);
        long count = searchService.getCount(jsonParam);
        System.err.println("匹配到的总条数："+count);
        return count;
    }

}
