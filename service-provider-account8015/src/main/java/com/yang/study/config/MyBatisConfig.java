package com.yang.study.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.yang.study.dao"})
public class MyBatisConfig {


}
