package com.yang.study.service;

import com.yang.study.entity.Storage;
import com.yang.study.vo.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-provider-storage8014")
public interface StorageFeignService {


    /***
     * 扣减库存
     * @param storage
     * @return
     */
    @PostMapping("/storage/minisStorage")
    CommonResult minisStorage(@RequestBody Storage storage);

}
