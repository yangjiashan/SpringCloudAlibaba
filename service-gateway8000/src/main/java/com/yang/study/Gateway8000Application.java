package com.yang.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Gateway8000Application {
    public static void main(String[] args) {
        SpringApplication.run(Gateway8000Application.class, args);
    }
}
