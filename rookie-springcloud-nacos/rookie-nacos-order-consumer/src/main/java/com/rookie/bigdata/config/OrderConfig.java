package com.rookie.bigdata.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Classname OrderConfig
 * @Description TODO
 * @Author rookie
 * @Date 2022/4/6 16:46
 * @Version 1.0
 */
@Configuration
@LoadBalancerClients(defaultConfiguration = {CustomLoadBalancerConfiguration.class})
public class OrderConfig {


    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
