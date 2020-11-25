package com.yang.study.dao;

import com.yang.study.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDAO {

    /**
     * 1 新建订单
     * @param order
     * @return
     */
    int create(Order order);
}
