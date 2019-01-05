package com.hlyp.api.service.impl;

import com.hlyp.api.bean.HesoAccount;
import com.hlyp.api.bean.HesoCurrency;
import com.hlyp.api.bean.HesoCustomNeed;
import com.hlyp.api.bean.HesoProduct;
import com.hlyp.api.dao.AccountDao;
import com.hlyp.api.dao.HesoCurrencyDao;
import com.hlyp.api.dao.HesoCustomNeedDao;
import com.hlyp.api.dao.HesoProductDao;
import com.hlyp.api.param.account.AccountParam;
import com.hlyp.api.param.order.HesoCustomNeedParam;
import com.hlyp.api.service.AccountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Lenovo on 2019/1/4.
 */
@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private HesoProductDao productDao;
    @Autowired
    private HesoCustomNeedDao customNeedDao;
    @Autowired
    private HesoCurrencyDao currencyDao;
    @Override
    public Boolean findAccount(AccountParam param) {
        HesoAccount account = new HesoAccount();
        account.setUserId(param.getUserId());
        List<HesoAccount> accountByName  = accountDao.select(account);
        if(accountByName!=null&&accountByName.size()!=0){
            return false;
        }
        BeanUtils.copyProperties(param,account);

        int result = accountDao.updateByPrimaryKeySelective(account);
        if(result == 1){
            return true;
        }
        return false;
    }

    @Override
    public int bookOrder(HesoCustomNeedParam param) {
        String productId = param.getProductId();
        HesoProduct hesoProduct = new HesoProduct();
        hesoProduct.setProductId(productId);
        hesoProduct = productDao.selectOne(hesoProduct);
        HesoCustomNeed hesoCustomNeed = new HesoCustomNeed();
        BeanUtils.copyProperties(param,hesoCustomNeed);
        hesoCustomNeed.setProductImg(hesoProduct.getImgFront());
        hesoCustomNeed.setState("0");
        Date now = new Date();
        hesoCustomNeed.setUpdateTime(now);
        int result = customNeedDao.insert(hesoCustomNeed);

        return result;
    }

    @Override
    public HesoProduct findProductImage(String productId) {

        return null;
    }

    @Override
    public HesoCurrency fingCurrency(String account) {
        HesoCurrency hesoCurrency = new HesoCurrency();
        hesoCurrency.setAccount(account);
        hesoCurrency = currencyDao.selectOne(hesoCurrency);
        return hesoCurrency;
    }
}
