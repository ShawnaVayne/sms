package com.qianfeng.smsplatform.search.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.smsplatform.search.SearchServiceApplication;
import com.qianfeng.smsplatform.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author simon
 * 2019/12/4 17:09
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SearchServiceApplication.class)
@Slf4j
public class SearchTest {
    @Autowired
    private SearchService searchService;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void testCreate() throws IOException {
        boolean index = searchService.createIndexSubmit();
        log.error("处理结果：{}",index);
    }
    @Test
    public void testDelete() throws IOException {
        boolean result = searchService.deleteIndex("eleven_sms_submit_log");
        log.error("删除结果：{}",result);
    }
    @Test
    public void testSearch() throws IOException, ParseException {
        Map<String,String> map = new HashMap<>(16);
        map.put("keyword","你好");
        /*map.put("clientID","2");*/
        /*map.put("start","2");
        map.put("rows","5");*/
        /*map.put("startTime","2019-12-05 08:00:00");
        map.put("endTime","2019-12-07 08:00:00");*/
        String s = objectMapper.writeValueAsString(map);
        List<Map> search = searchService.search(s);
        for (Map search1 : search) {
            System.err.println(search1);
        }
    }
    @Test
    public void testState() throws IOException, ParseException {
        Map<String,String> map = new HashMap<>(16);
        map.put("clientID","2");
        String s = objectMapper.writeValueAsString(map);
        Map<String, Long> count = searchService.getState(s);
        System.err.println(count);
    }
    @Test
    public void testCount() throws IOException, ParseException {
        Map<String,String> map = new HashMap<>(16);
        map.put("clientID","2");
        String s = objectMapper.writeValueAsString(map);
        long count = searchService.getCount(s);
        System.err.println(count);
    }
    @Test
    public void testUpdate() throws IOException {
        Map<String,String> map = new HashMap<>(16);
        /*map.put("msgid","73740a57-128b-41d6-aa2f-ff4a9f70b195");*/
        map.put("reportState","0");
        boolean b = searchService.updateLog("eleven_sms_submit_log", "eleven_sms_submit_log_type", "73740a57-128b-41d6-aa2f-ff4a9f70b195", objectMapper.writeValueAsString(map));
        String s = objectMapper.writeValueAsString(map);
        System.err.println(s);

    }
}
