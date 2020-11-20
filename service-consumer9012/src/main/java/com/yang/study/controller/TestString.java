package com.yang.study.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.yang.study.service.TestFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestString {

    @Autowired
    private TestFeignService testFeignService;

    @RequestMapping("/testString")
    @SentinelResource(value = "getSentinelConfigByNacos", blockHandler = "getSentinelConfigByNacosBlock")
    public String testString() {
        return testFeignService.testString();
    }

    public String getSentinelConfigByNacosBlock(BlockException be) {
        return "getSentinelConfigByNacos 接口访问已经达到上限了";
    }

    public String getSentinelConfigByNacosFallback() {
        return "访问频率太高，执行降级";
    }
}
