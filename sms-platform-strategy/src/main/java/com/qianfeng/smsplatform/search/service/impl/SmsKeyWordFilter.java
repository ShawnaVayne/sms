package com.qianfeng.smsplatform.search.service.impl;

import com.qianfeng.smsplatform.common.constants.CacheConstants;
import com.qianfeng.smsplatform.common.constants.StrategyConstants;
import com.qianfeng.smsplatform.common.model.Standard_Report;
import com.qianfeng.smsplatform.common.model.Standard_Submit;
import com.qianfeng.smsplatform.search.feign.CacheFeignClient;
import com.qianfeng.smsplatform.search.service.MyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Set;

/**
 * @Author 徐胜涵
 */
@Service
public class SmsKeyWordFilter implements MyFilter {

    @Autowired
    private CacheFeignClient cacheFeignClient;

    @Override
    public void doFilter(Standard_Submit submit, Standard_Report report) throws IOException {
        StringReader stringReader = new StringReader(submit.getMessageContent());
        IKSegmenter ikSegmenter = new IKSegmenter(stringReader, true);
        Lexeme lex=null;
        while ((lex = ikSegmenter.next()) != null) {
            Set<String> keyWords = cacheFeignClient.getKey(CacheConstants.CACHE_PREFIX_DIRTYWORDS + lex.getLexemeText());
            if (keyWords.size() > 0) {
                System.err.println("敏感词");
                report.setErrorCode(StrategyConstants.STRATEGY_ERROR_DIRTYWORDS);
                report.setState(2);
                break;
            }
        }
    }
}
