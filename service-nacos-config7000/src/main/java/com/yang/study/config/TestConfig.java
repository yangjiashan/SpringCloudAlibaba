package com.yang.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public TestConfigProperties testConfigProperties() {
        return new TestConfigProperties();
    }
}
