package com.yang.study.service;

import com.yang.study.entity.Account;

public interface AccountService {

    /**
     * 扣减余额
     */
    int update(Account account);
}