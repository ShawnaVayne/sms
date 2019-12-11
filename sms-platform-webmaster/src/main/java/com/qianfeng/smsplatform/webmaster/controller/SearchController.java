package com.qianfeng.smsplatform.webmaster.controller;

import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.webmaster.feign.CacheFeign;
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
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private CacheFeign cacheFeign;

    @Autowired
    private SearchFeign searchFeign;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");



    @RequestMapping("/sys/search/list")
    public TableData smssearch(SearchPojo criteria) throws ParseException {
        TAdminUser userEntity = ShiroUtils.getUserEntity();
        Integer clientid = userEntity.getClientid();
        if(clientid!=0){//非管理员只能查自己
            criteria.setClientID(clientid);
        }
        //criteria.setHighLightPostTag("</font>");
        //criteria.setHighLightPreTag("<font style='color:red'>");
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
            //System.err.println("集合长度为："+list.size());
            ////////////////////////////////////////////
            for (Map map : list) {
                //System.err.println(map);
                String clientID = String.valueOf(map.get("clientID"));
                Map<Object, Object> objMap = cacheFeign.hMGet(CacheConstants.CACHE_PREFIX_CLIENT + clientID);
                String corpName ="";
                corpName = (String) objMap.get("corpName");
                map.put("corpname",corpName);
                String sendTime = (String) map.get("sendTime");
                if(sendTime != null && !"".equals(sendTime.trim())){
                    Date sendTimeDate = sdf.parse(sendTime.split("\\+")[0].replace("T"," "));
                    map.put("sendTimeStr", sendTimeDate.toLocaleString());
                }else{
                    map.put("sendTimeStr", "");
                }
            }
            return new TableData(count, list);
        }
        return new TableData(0, null);
    }



}
