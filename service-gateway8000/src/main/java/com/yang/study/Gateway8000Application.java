package com.yang.study;

import com.yang.study.service.TestFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
public class Gateway8000Application {
    public static void main(String[] args) {
        SpringApplication.run(Gateway8000Application.class, args);
    }
    // -Dreactor.netty.http.server.accessLogEnabled=true

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/testString")
                        .filters(f -> f.filters(new TestFilter()))
                        .uri("lb://service-provider8011"))
                .build();
    }

//    @Bean
//    @Order(-1)
//    public GlobalFilter a() {
//        return (exchange, chain) -> {
//            log.info("first pre filter");
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                log.info("third post filter");
//            }));
//        };
//    }
//    @Bean
//    @Order(0)
//    public GlobalFilter b() {
//        return (exchange, chain) -> {
//            log.info("second pre filter");
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                log.info("second post filter");
//            }));
//        };
//    }
//    @Bean
//    @Order(1)
//    public GlobalFilter c() {
//        return (exchange, chain) -> {
//            log.info("third pre filter");
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                log.info("first post filter");
//            }));
//        };
//    }
}
