package com.yang.study.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "service-gateway8000")
public interface TestFeignService {

    @RequestMapping("/testString")
    String testString();

}
