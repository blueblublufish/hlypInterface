package com.hlyp.api.bean;

import java.math.BigDecimal;

/**
 * Created by Lenovo on 2019/1/4.
 */
public class HesoOrderPay {
    private Integer payId;
    private String waiOrder;
    private String orderPay;
    private BigDecimal orderMoney;

    public Integer getPayId() {
        return payId;
    }

    public void setPayId(Integer payId) {
        this.payId = payId;
    }

    public String getWaiOrder() {
        return waiOrder;
    }

    public void setWaiOrder(String waiOrder) {
        this.waiOrder = waiOrder;
    }

    public String getOrderPay() {
        return orderPay;
    }

    public void setOrderPay(String orderPay) {
        this.orderPay = orderPay;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }
}
