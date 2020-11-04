package com.yang.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Provider8012Application {
    public static void main(String[] args) {
        SpringApplication.run(Provider8012Application.class, args);
    }

}
