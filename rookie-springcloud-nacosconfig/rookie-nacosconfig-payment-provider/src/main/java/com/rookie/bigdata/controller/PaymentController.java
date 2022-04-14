package com.rookie.bigdata.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;


/**
 * @Classname PaymentController
 * @Description TODO
 * @Author rookie
 * @Date 2022/4/6 14:45
 * @Version 1.0
 */
@RestController
@RefreshScope //在控制器类加入@RefreshScope注解使当前类下的配置支持Nacos的动态刷新功能。
public class PaymentController {

    protected final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/config/info")
    public String getConfigInfo() {
        return configInfo;
    }


}
