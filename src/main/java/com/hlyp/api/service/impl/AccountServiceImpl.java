package com.hlyp.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.parser.impl.HsqldbParser;
import com.hlyp.api.bean.*;
import com.hlyp.api.dao.*;
import com.hlyp.api.param.account.AccountParam;
import com.hlyp.api.param.order.HesoCustomNeedParam;
import com.hlyp.api.service.AccountService;
import com.hlyp.api.utils.weixin.PayUtil;
import com.hlyp.api.utils.weixin.WXCore;
import com.hlyp.api.utils.weixin.config.WxPayConfig;
import com.hlyp.api.utils.weixin.vo.OAuthJsToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.weixin4j.WeixinException;
import org.weixin4j.http.HttpsClient;
import org.weixin4j.http.Response;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private HesoAccountMapper accountMapper;

    private final Logger logger =  LoggerFactory.getLogger(AccountServiceImpl.class);

    @Override
    public HesoAccount decodeUserInfo(String encryptedData, String iv, String sessionKey,String openId) throws  Exception {
        HesoAccount account = new HesoAccount();
        String result = "";
        result = new WXCore().decodeUserInfo(encryptedData,iv,sessionKey);
        HesoAccountExample example = new HesoAccountExample();
        HesoAccountExample.Criteria criteria = example.createCriteria();
        criteria.andMobileEqualTo(result);
        List<HesoAccount> list = accountMapper.selectByExample(example);
        if(list==null || list.size()==0){
            String xml = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                    "<message>\n" +
                    "<head>\n" +
                    "<type>001000</type>\n" +
                    "<messageId>1</messageId>\n" +
                    "<agentId>001</agentId>\n" +
                    "<digest>MD5数字签名</digest>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    " <userId>" +
                    result +
                    "</userId>\n" +
                    "<password>e9af3de161c9038cd81014f31f8278fd</password>\n" +
                    "<userType>1</userType>\n" +
                    "<mobile>" +
                    result +
                    "</mobile>\n" +
                    "<mobileCode>274378</mobileCode>\n" +
                    "<registerType>0</registerType>\n" +
                    "<openid></openid>\n" +
                    "<channel></channel>\n" +
                    "<sex>1</sex>\n" +
                    "</body>\n" +
                    "</message>";
            String xmlresult = PayUtil.httpRequest(WxPayConfig.hlyp_old_url, "POST", xml);
            Map map = PayUtil.doXMLParse(xmlresult);
            String returnCode = (String) map.get("body");
            int t = returnCode.indexOf("交易成功");
            if(t>0){
                list = accountMapper.selectByExample(example);
                account = list.get(0);
                HesoAccount hesoAccount = accountMapper.selectByPrimaryKey(account.getAccount());
                HesoAccount newHesoAccount = new HesoAccount();
                newHesoAccount.setAccount(hesoAccount.getAccount());
                newHesoAccount.setOpenid(openId);
                newHesoAccount.setComment(sessionKey);
                accountMapper.updateByPrimaryKeySelective(newHesoAccount);
                account = accountMapper.selectByPrimaryKey(account.getAccount());

            }else {
                return  null;
            }
        }else {
            account = list.get(0);
            HesoAccount hesoAccount = accountMapper.selectByPrimaryKey(account.getAccount());
            HesoAccount newHesoAccount = new HesoAccount();
            newHesoAccount.setAccount(hesoAccount.getAccount());
            newHesoAccount.setOpenid(openId);
            newHesoAccount.setComment(sessionKey);
            accountMapper.updateByPrimaryKeySelective(newHesoAccount);
            account = accountMapper.selectByPrimaryKey(account.getAccount());
        }

        return account;
    }

    @Override
    public Boolean findAccount(AccountParam param) {
        HesoAccount account = new HesoAccount();
        account.setUserId(param.getUserId());
        /*HesoAccountExample example = new HesoAccountExample();
        HesoAccountExample.Criteria criteria =  example.createCriteria();
        criteria.andUserIdEqualTo(param.getUserId());
        List<HesoAccount> list = accountMapper.selectByExample(example);*/
        HesoAccount hesoAccount = accountMapper.selectByPrimaryKey(param.getAccount());
        if(hesoAccount.getUserId().equals(param.getUserId())){
            return  false;
        }
      /*  if(list!=null && list.size()!=0){
            return false;
        }*/
        BeanUtils.copyProperties(param,account);

        int result = accountMapper.updateByPrimaryKeySelective(account);
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

    @Override
    public HesoAccount selectAccountById(String account) {
       return accountMapper.selectByPrimaryKey(account);

    }

    @Override
    public int updateAccount(HesoAccount account) {
        return  accountMapper.updateByPrimaryKeySelective(account);
    }
}
