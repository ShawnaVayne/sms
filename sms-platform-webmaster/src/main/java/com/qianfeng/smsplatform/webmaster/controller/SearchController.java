package com.qianfeng.smsplatform.webmaster.controller;

import com.qianfeng.smsplatform.webmaster.feign.SearchFeign;
import com.qianfeng.smsplatform.webmaster.pojo.TAdminUser;
import com.qianfeng.smsplatform.webmaster.service.api.SearchService;
import com.qianfeng.smsplatform.webmaster.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;



/**
 * 搜索服务
 *
 */
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private SearchFeign searchFeign;

//    @Autowired
//    private CacheService cacheService;


    @RequestMapping("/sys/search/list")
    public TableData smssearch(SearchPojo criteria) {
        TAdminUser userEntity = ShiroUtils.getUserEntity();
        Integer clientid = userEntity.getClientid();
        if(clientid!=0){//非管理员只能查自己
            criteria.setClientID(clientid);
        }
        criteria.setHighLightPostTag("</font>");
        criteria.setHighLightPreTag("<font style='color:red'>");
        String str = JsonUtil.getJSON(criteria);
        System.err.println("需要传递的对象："+str);
        //Long count = searchService.searchLogCount(str);
        /////////////////////////////////////////////
        Long count = null;
        try {
            System.err.println(str);
            count = searchFeign.getCount(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ////////////////////////////////////////////
        if (count != null && count > 0) {
            //List<Map> list = searchService.searchLog(str);
            ////////////////////////////////////////////
            List<Map> list = null;
            try {
                list = searchFeign.search(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ////////////////////////////////////////////
            for (Map map : list) {
                String clientID = String.valueOf(map.get("clientID"));
//                Map<String, String> hmget = cacheService.hmget("CLIENT:" + clientID);
//                String corpname = hmget.get("corpname");
                String corpname ="";
                map.put("corpname",corpname);
                Object sendTime1 = map.get("sendTime");
                if(!StringUtils.isEmpty(sendTime1)) {
                    Long sendTime = Long.parseLong(sendTime1.toString());
                    String sendTimeStr = DateUtils.longToStr(sendTime);
                    map.put("sendTimeStr", sendTimeStr);
                }else {
                    map.put("sendTimeStr", "");
                }
            }
            return new TableData(count, list);
        }
        return new TableData(0, null);
    }



}
