package com.hlyp.api.controller;

import com.hlyp.api.bean.HesoOrderPay;
import com.hlyp.api.param.order.OrderCheckParam;
import com.hlyp.api.service.HesoOrderConsumeService;
import com.hlyp.api.service.HesoPayOrderService;
import com.hlyp.api.vo.Json;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("/orderToAmani")
    @ResponseBody
    @ApiOperation(value = "审核向阿玛尼下单")
    public   Json getPayOrder(@RequestBody OrderCheckParam param) throws  Exception{
        Json json = new Json();
        json.setSuccess(true);
        json.setMsg("操作成功");

        Map<String,String> map =orderConsumeService.packToAmani(param.getOrderId());

        json.setData(map);
        return  json;
    }

}
