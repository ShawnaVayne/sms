package com.qianfeng.smsplatform.webmaster.test;

import com.qianfeng.smsplatform.webmaster.SmsPlatformWebManagerApplication;
import com.qianfeng.smsplatform.webmaster.feign.SearchFeign;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Classname SearchFeignTest
 * @Description TODO
 * @Date 2019/12/10 15:47
 * @Created by sunjiangwei
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmsPlatformWebManagerApplication.class)
public class SearchFeignTest {
    @Autowired
    private SearchFeign searchFeign;

    @Test
    public void testSearchList() throws IOException, ParseException {
        String str = "\"keyword\":\"你好\",\"clientId\":2";
        List<Map> search = searchFeign.search(str);
        for (Map map : search) {
            System.err.println(map);
        }
    }

    @Test
    public void test(){
        Date date = new Date();
        System.err.println(date.toInstant());
    }
}
