package com.qianfeng.smsplatform.search.service;

import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;

public interface MyFilter {

    void doFilter(Standard_Submit submit, Standard_Report report);
}
