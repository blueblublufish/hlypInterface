package com.hlyp.api.service;

import com.hlyp.api.bean.HesoOrderPay;

/**
 * Created by Lenovo on 2019/1/4.
 */
public interface HesoPayOrderService {
    HesoOrderPay findByPayOrderId(String payOrderId);
}
