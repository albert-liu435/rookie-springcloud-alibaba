//package com.rookie.bigdata.controller;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.junit.Assert.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * @Classname PaymentControllerTest
// * @Description TODO
// * @Author rookie
// * @Date 2022/4/6 15:25
// * @Version 1.0
// */
//@SpringBootTest
//@RunWith(SpringRunner.class)
////@ActiveProfiles("uat")
//public class PaymentControllerTest {
//
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
////    private void sendTestRequest(ReplyAbnormalOrderVop requestObj) throws Exception {
////        MvcResult result = mockMvc.perform(post("/callOut_CS/replyAbnormalOrderVop")
////                //设置contentType参数
////                .contentType(MediaType.APPLICATION_JSON)
////                //设置请求体参数
////                .content(new Gson().toJson(requestObj)))
////                //模拟向testRest发送get请求
////                .andExpect(status().isOk())
////                //返回执行请求的结果
////                .andReturn();
////        System.out.println((result.getResponse().getContentAsString()));
////
////
////        // return gson.fromJson(result.getResponse().getContentAsString(), CallIn_CRM_MemberQuery_ControllerTest.ResponseJson1.class);
////    }
//
//
////    @Test
////    public void queryeplyAbnormalOrderVop() throws Exception{
////
////        ReplyAbnormalOrderVop replyAbnormalOrderVop=new ReplyAbnormalOrderVop();
////        replyAbnormalOrderVop.setShopCode("OZZZ");
////        replyAbnormalOrderVop.setId(48988473L);
////        replyAbnormalOrderVop.setAbnormal_order_sn("XWLS210318426925");
////        // replyAbnormalOrderVop.setLack_num();
////        replyAbnormalOrderVop.setReason_first("未交接给承运商");
////        replyAbnormalOrderVop.setReason_second("已打包，承运未揽收");
////        replyAbnormalOrderVop.setHandle_time("2021-03-19 09:00:00");
////        String s = gson.toJson(replyAbnormalOrderVop);
////        System.out.println(s);
////        this.sendTestRequest(replyAbnormalOrderVop);
////
////
////    }
//
//
//
//    @Test
//    public void create() {
//    }
//
//    @Test
//    public void getPaymentById() throws Exception{
//
//
//                MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/payment/get/1")
//
//                //设置contentType参数
//                .contentType(MediaType.APPLICATION_JSON))
//                //设置请求体参数
//               // .content(new Gson().toJson(requestObj)))
//                //模拟向testRest发送get请求
//                .andExpect(status().isOk())
//                //返回执行请求的结果
//                .andReturn();
//        System.out.println(result.getResponse().getContentAsString());
//
//    }
//}