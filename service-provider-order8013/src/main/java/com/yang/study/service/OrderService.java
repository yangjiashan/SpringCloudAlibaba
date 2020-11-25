package com.yang.study.service;

public interface OrderService {

    /**
     * 创建订单
     */
    int create(String userId, String commodityCode, int orderCount);
}