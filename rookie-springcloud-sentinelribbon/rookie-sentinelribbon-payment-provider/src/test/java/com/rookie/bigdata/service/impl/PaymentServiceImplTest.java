//package com.rookie.bigdata.service.impl;
//
//
//import com.rookie.bigdata.entities.Payment;
//import com.rookie.bigdata.service.PaymentService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * @Classname PaymentServiceImplTest
// * @Description TODO
// * @Author rookie
// * @Date 2022/4/6 14:57
// * @Version 1.0
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
////@ActiveProfiles("uat")
//public class PaymentServiceImplTest {
//
//    @Autowired
//    private PaymentService paymentService;
//
//    @Test
//    public void create() {
//
//        Payment payment=new Payment();
//        payment.setSerial("张三");
//        payment.setId(1L);
//        int i = paymentService.create(payment);
//        System.out.println(i);
//
//
//    }
//
//    @Test
//    public void getPaymentById() {
//    }
//}