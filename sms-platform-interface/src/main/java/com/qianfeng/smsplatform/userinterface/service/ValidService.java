package com.qianfeng.smsplatform.userinterface.service;

import com.qianfeng.smsplatform.common.model.Standard_Submit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 2019/12/314:00
 * <p>
 * 未知的事情 永远充满变数
 */
public interface ValidService {

    public List<Standard_Submit> valid(HttpServletRequest request, HttpServletResponse response, String [] split, String content)throws IOException;

}
