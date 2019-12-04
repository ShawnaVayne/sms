package com.qianfeng.smsplatform.search.test;

import com.qianfeng.smsplatform.search.SearchServiceApplication;
import com.qianfeng.smsplatform.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

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

    @Test
    public void testCreate() throws IOException {
        boolean index = searchService.createIndex();
        log.error("处理结果：{}",index);
    }
    @Test
    public void testDelete() throws IOException {
        boolean result = searchService.deleteIndex("eleven_sms_submit_log");
        log.error("删除结果：{}",result);
    }
}
