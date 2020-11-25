package com.yang.study.serviceImpl;

import com.yang.study.dao.OrderDAO;
import com.yang.study.entity.Account;
import com.yang.study.entity.Order;
import com.yang.study.service.OrderService;
import com.yang.study.service.feign.AccountFeignService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDAO orderDAO;

    @Resource
    private AccountFeignService accountFeignService;

    public int create(String userId, String commodityCode, int orderCount) {

        int orderMoney = calculate(commodityCode, orderCount);

        Account account = new Account();
        account.setMoney(orderMoney);
        account.setUser_id(userId);
        accountFeignService.minusAccountMoney(account);

        Order order = new Order();
        order.setUser_id(userId);
        order.setCommodity_code(commodityCode);
        order.setCount(orderCount);
        order.setMoney(orderMoney);

        // INSERT INTO orders ...
        return orderDAO.create(order);
    }

    private int calculate(String commodityCode, int orderCount) {
        int resultMoney = 0;
        switch (commodityCode) {
            case "1001":
                resultMoney = orderCount;
                break;

            case "1002":
                resultMoney = 2 * orderCount;
                break;

            default:
                resultMoney = 1 * orderCount;
        }
        return resultMoney;
    }

}
