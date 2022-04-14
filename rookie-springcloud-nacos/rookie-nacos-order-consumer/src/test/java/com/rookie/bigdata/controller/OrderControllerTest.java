//package com.rookie.bigdata.controller;
//
//import com.google.gson.Gson;
//import com.rookie.bigdata.entities.Payment;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.junit.Assert.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * @Classname OrderControllerTest
// * @Description TODO
// * @Author rookie
// * @Date 2022/4/6 16:51
// * @Version 1.0
// */
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class OrderControllerTest {
//
//    /**
//     * 模拟MVC对象，通过MockMvcBuilders.webAppContextSetup(this.wac).build()初始化。
//     */
//    private MockMvc mockMvc;
//
////    /**
////     * Json 处理类
////     */
////    Gson gson = new Gson();
//
//    /**
//     * 注入WebApplicationContext
//     */
//    @Autowired
//    private WebApplicationContext wac;
//
//
//    @Before
//    public void setUp() throws Exception {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
//    }
//
//
//    @Test
//    public void create() throws Exception{
//
//        Payment payment=new Payment();
//        payment.setId(3L);
//        payment.setSerial("李四");
//
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/consumer/payment/create")
//
//                //设置contentType参数
//                .contentType(MediaType.APPLICATION_JSON)
//                //设置请求体参数
//                 .content(new Gson().toJson(payment)))
//                //模拟向testRest发送get请求
//                .andExpect(status().isOk())
//                //返回执行请求的结果
//                .andReturn();
//        System.out.println(result.getResponse().getContentAsString());
//
//
//    }
//
//    @Test
//    public void getPayment() throws Exception {
//
//
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/consumer/payment/get/1")
//
//                //设置contentType参数
//                .contentType(MediaType.APPLICATION_JSON))
//                //设置请求体参数
//                // .content(new Gson().toJson(requestObj)))
//                //模拟向testRest发送get请求
//                .andExpect(status().isOk())
//                //返回执行请求的结果
//                .andReturn();
//        System.out.println(result.getResponse().getContentAsString());
//    }
//}