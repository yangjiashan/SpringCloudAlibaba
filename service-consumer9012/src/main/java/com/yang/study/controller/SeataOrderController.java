package com.yang.study.controller;

import com.yang.study.entity.Order;
import com.yang.study.entity.Storage;
import com.yang.study.service.OrderFeignService;
import com.yang.study.service.StorageFeignService;
import com.yang.study.vo.CommonResult;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SeataOrderController {

    @Resource
    private OrderFeignService orderFeignService;

    @Resource
    private StorageFeignService storageFeignService;


    @GlobalTransactional
    @RequestMapping(value = "/order/createOrder")
    public CommonResult createOrder() {
        Order order = new Order();
        order.setUser_id("1");
        order.setCount(2);
        order.setCommodity_code("1001");
        orderFeignService.create(order);

        Storage storage = new Storage();
        storage.setCount(2);
        storage.setCommodity_code("1001");

        storageFeignService.minisStorage(storage);

        System.out.println("下单成功");
        return new CommonResult(200, "下单成功");
    }

    @RequestMapping(value = "/order/createOrder2")
    public CommonResult createOrder2() {
        Order order = new Order();
        order.setUser_id("1");
        order.setCount(2);
        order.setCommodity_code("1001");
        orderFeignService.create(order);

        Storage storage = new Storage();
        storage.setCount(2);
        storage.setCommodity_code("1001");

        storageFeignService.minisStorage(storage);

        System.out.println("下单成功");
        return new CommonResult(200, "下单成功");
    }

}
