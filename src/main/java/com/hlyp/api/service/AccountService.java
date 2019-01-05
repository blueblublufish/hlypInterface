package com.hlyp.api.service;


import com.hlyp.api.bean.HesoCurrency;
import com.hlyp.api.bean.HesoOrderConsume;
import com.hlyp.api.bean.HesoProduct;
import com.hlyp.api.param.account.AccountParam;
import com.hlyp.api.param.order.HesoCustomNeedParam;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * Created by Lenovo on 2019/1/4.
 */
public interface AccountService {
    Boolean findAccount(AccountParam param);

    int bookOrder(HesoCustomNeedParam param);

    HesoProduct findProductImage(String productId);

    HesoCurrency fingCurrency(String account);
}
