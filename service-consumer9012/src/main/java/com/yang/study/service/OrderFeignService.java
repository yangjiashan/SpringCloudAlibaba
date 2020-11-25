package com.yang.study.service;

import com.yang.study.entity.Order;
import com.yang.study.vo.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-provider-order8013")
public interface OrderFeignService {

    /**
     * 创建订单
     *
     * @param order
     * @return
     */
    @PostMapping("/order/create")
    CommonResult create(@RequestBody Order order);
}
