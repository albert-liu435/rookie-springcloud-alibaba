package com.rookie.bigdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Classname PaymentApplication
 * @Description TODO
 * @Author rookie
 * @Date 2022/4/6 13:59
 * @Version 1.0
 */
@EnableDiscoveryClient
@SpringBootApplication
public class OrderNacosApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderNacosApplication.class, args);

    }
}
