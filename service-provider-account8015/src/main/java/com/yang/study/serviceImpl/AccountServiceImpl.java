package com.yang.study.serviceImpl;

import com.yang.study.dao.AccountDAO;
import com.yang.study.entity.Account;
import com.yang.study.service.AccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountDAO accountDAO;


    @Override
    public int update(Account account) {
        return accountDAO.update(account);
    }
}
