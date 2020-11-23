package com.yang.study.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.yang.study.exception.CommonBlockerHandler;
import com.yang.study.exception.CommonFallbackHandler;
import com.yang.study.service.TestFeignService;
import com.yang.study.service.TestStringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestString {

    @Autowired
    private TestFeignService testFeignService;

    @Autowired
    private TestStringService testStringService;

    @RequestMapping("/testString")
    @SentinelResource(value = "testString", blockHandlerClass = CommonBlockerHandler.class, blockHandler = "process", fallbackClass = CommonFallbackHandler.class, fallback = "process")
    public String testString() {
        int i = 1 / 0;
        return testFeignService.testString();
    }

    @RequestMapping("/testString2")
//    @SentinelResource(value = "testString2", fallbackClass = CommonFallbackHandler.class, fallback = "process")
    public String testString2() {
//        int i = 1 / 0;
//        return testFeignService.testString();
        return testStringService.test1();
    }


}
