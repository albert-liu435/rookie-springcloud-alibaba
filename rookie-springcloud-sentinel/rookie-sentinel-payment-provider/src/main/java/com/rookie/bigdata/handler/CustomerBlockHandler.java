package com.rookie.bigdata.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.rookie.bigdata.entities.CommonResult;

/**
 * @Classname CustomerBlockHandler
 * @Description TODO
 * @Author rookie
 * @Date 2022/4/15 16:41
 * @Version 1.0
 */
public class CustomerBlockHandler {
    public static CommonResult handlerException(BlockException exception) {
        return new CommonResult(4444, "按客戶自定义,global handlerException----1");
    }

    public static CommonResult handlerException2(BlockException exception) {
        return new CommonResult(4444, "按客戶自定义,global handlerException----2");
    }
}

