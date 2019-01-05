package com.hlyp.api.controller;

import com.hlyp.api.bean.HesoOrderPay;
import com.hlyp.api.service.HesoOrderConsumeService;
import com.hlyp.api.service.HesoPayOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Lenovo on 2019/1/4.
 */
@RestController
@RequestMapping("order")
@Api(value = "订单模块")
public class HesoOrderController {
    @Autowired
    private HesoPayOrderService payOrderService;
    @Autowired
    private HesoOrderConsumeService orderConsumeService;

   /* @PostMapping("/getPayOrder")
    @ResponseBody
    @ApiOperation(value = "获取外订单")
    HesoOrderPay getPayOrder(){
        return payOrderService.findByPayOrderId("0000000000005333");
    }*/

}
