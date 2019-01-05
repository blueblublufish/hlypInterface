package com.hlyp.api.bean;

import java.math.BigDecimal;

/**
 * Created by Lenovo on 2019/1/4.
 */
public class HesoCurrency {
    private String account;
    private BigDecimal balance;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
