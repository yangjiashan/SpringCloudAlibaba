package com.yang.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
@EnableDiscoveryClient
public class NacosApplication7000 {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(NacosApplication7000.class, args);
        System.out.println(run.getBeanFactory().getBean("testConfigProperties"));
        System.out.println(run.getBeanFactory());
    }
}
