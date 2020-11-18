package com.yang.study;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Properties;

@SpringBootApplication
@EnableDiscoveryClient
public class NacosApplication7000 {

    public static void main(String[] args) {
        SpringApplication.run(NacosApplication7000.class, args);

//        // 代码获取配置
//        // nacos 地址        
//        String serverAddr = "127.0.0.1:8848";
//        // Data Id        
//        String dataId = "nacos_simple_demo";
//        // Group        
//        String group = "DEFAULT_GROUP";
//        Properties properties = new Properties();
//        properties.put("serverAddr", serverAddr);
//        ConfigService configService = NacosFactory.createConfigService(properties);
//        // 获取配置,String dataId, String group, long timeoutMs         
//        String content = configService.getConfig(dataId, group, 5000);
//        System.out.println(content);

    }
}
