package com.yang.study.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.yang.study.exception.CommonFallbackHandler;
import org.springframework.stereotype.Component;

@Component
public class TestStringService {

    @SentinelResource(value = "testString2", fallbackClass = CommonFallbackHandler.class, fallback = "process")
    public String test1() {
        int i = 1 / 0;
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return "ok";
    }

}
