package com.yang.study.dao;

import com.yang.study.entity.Account;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountDAO {

    /**
     * 1 扣减余额
     * @param account
     * @return
     */
    int update(Account account);
}
