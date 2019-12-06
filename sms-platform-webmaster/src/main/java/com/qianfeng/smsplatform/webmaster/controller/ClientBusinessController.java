package com.qianfeng.smsplatform.webmaster.controller;

import com.qianfeng.smsplatform.webmaster.dto.DataGridResult;
import com.qianfeng.smsplatform.webmaster.dto.QueryDTO;
import com.qianfeng.smsplatform.webmaster.pojo.TClientBusiness;
import com.qianfeng.smsplatform.webmaster.service.ClientBusinessService;
import com.qianfeng.smsplatform.webmaster.util.R;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Log4j2
@Controller
public class ClientBusinessController {

    @Autowired
    private ClientBusinessService clientBusinessService;

    @ResponseBody
    @RequestMapping("/sys/getClientBusiness/{id}")
    public TClientBusiness findClientBusinessById(@PathVariable Long id){
        TClientBusiness client = clientBusinessService.findById(id);
        return client;
    }

    ///////////////////////////////
    @ResponseBody
    @RequestMapping("/sys/clientbusiness/list")
    public DataGridResult findClientBusiness(QueryDTO queryDTO) {
        return clientBusinessService.findByPage(queryDTO);
    }

    @ResponseBody
    @RequestMapping("/sys/clientbusiness/del")
    public R delClientBusiness(@RequestBody List<Long> ids) {
        for (Long id : ids) {
            clientBusinessService.delClientBusiness(id);
        }
        return R.ok();
    }


    @ResponseBody
    @RequestMapping("/sys/clientbusiness/info/{id}")
    public R findById(@PathVariable("id") Long id) {
        log.info("用户传过来的id：{}",id);
        TClientBusiness tClientBusiness = clientBusinessService.findById(id);
        return R.ok().put("clientbusiness", tClientBusiness);
    }

    @ResponseBody
    @RequestMapping("/sys/clientbusiness/save")
    public R addClientBusiness(@RequestBody TClientBusiness tClientBusiness) {
        int i = clientBusinessService.addClientBusiness(tClientBusiness);
        return i > 0 ? R.ok() : R.error("添加失败");
    }

    @ResponseBody
    @RequestMapping("/sys/clientbusiness/update")
    public R updateClientBusiness(@RequestBody TClientBusiness tClientBusiness) {
        int i = clientBusinessService.updateClientBusiness(tClientBusiness);
        return i > 0 ? R.ok() : R.error("修改失败");
    }

}
