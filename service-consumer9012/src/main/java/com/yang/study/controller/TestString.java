package com.yang.study.controller;

import com.yang.study.service.TestFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestString {

    @Autowired
    private TestFeignService testFeignService;

    @RequestMapping("/testString")
    public String testString() {
        return testFeignService.testString();
    }
}
