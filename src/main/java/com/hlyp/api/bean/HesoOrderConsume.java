package com.hlyp.api.bean;

/**
 * Created by Lenovo on 2019/1/4.
 */
public class HesoOrderConsume {
    private Integer orderId;
    private String account;
    private String name;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
