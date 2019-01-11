package com.hlyp.api.service;


import com.hlyp.api.bean.HesoAccount;
import com.hlyp.api.bean.HesoCurrency;
import com.hlyp.api.bean.HesoOrderConsume;
import com.hlyp.api.bean.HesoProduct;
import com.hlyp.api.param.account.AccountParam;
import com.hlyp.api.param.order.HesoCustomNeedParam;

/**
 * Created by Lenovo on 2019/1/4.
 */
public interface AccountService {
    Boolean findAccount(AccountParam param);

    int bookOrder(HesoCustomNeedParam param);

    HesoProduct findProductImage(String productId);

    HesoCurrency fingCurrency(String account);

    HesoAccount decodeUserInfo(String encryptedData, String iv, String sessionKey ,String openId) throws Exception;

    HesoAccount selectAccountById(String account);

    int updateAccount(HesoAccount account);
}
