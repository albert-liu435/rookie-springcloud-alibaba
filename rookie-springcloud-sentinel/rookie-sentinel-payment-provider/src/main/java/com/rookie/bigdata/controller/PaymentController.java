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

public class PaymentController {

    protected final Logger log = LoggerFactory.getLogger(PaymentController.class);

    //流控设置线程数为1
//    @GetMapping("/testA")
//    public String testA() {
//
//        try {
//            Thread.sleep(800);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return "------testA";
//    }

    @GetMapping("/testA")
    public String testA() {

        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "------testA";
    }

    @GetMapping("/testB")
    public String testB() {
        return "------testB";
    }


}
