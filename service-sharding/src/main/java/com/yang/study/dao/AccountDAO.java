package com.yang.study.dao;

import com.yang.study.entity.Account;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountDAO {

    int add(Account account);

    int addDefault(Account account);

}
