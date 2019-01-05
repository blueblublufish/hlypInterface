package com.hlyp.api.service.impl;

import com.hlyp.api.bean.HesoOrderPay;
import com.hlyp.api.dao.HesoOrderPayDao;
import com.hlyp.api.service.HesoPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by Lenovo on 2019/1/4.
 */
@Service
public class HesoPayOrderServiceImpl implements HesoPayOrderService {
    @Autowired
    HesoOrderPayDao hesoOrderPayDao;
    @Override
    public HesoOrderPay findByPayOrderId(String payOrderId) {
        HesoOrderPay hesoOrderPay = new HesoOrderPay();
        hesoOrderPay.setWaiOrder(payOrderId);
        HesoOrderPay result = hesoOrderPayDao.selectOne(hesoOrderPay);

        return result;
    }
}
