package com.hlyp.api.param.payParam;

/**
 * Created by Lenovo on 2019/1/2.
 */
public class WxPayParam {
    private String openId;
    private String orderId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
