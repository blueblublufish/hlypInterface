package com.hlyp.api.controller;

import com.hlyp.api.bean.HesoAccount;
import com.hlyp.api.bean.HesoCurrency;
import com.hlyp.api.dao.HesoCurrencyDao;
import com.hlyp.api.dto.account.AccountDto;
import com.hlyp.api.param.account.AccountParam;
import com.hlyp.api.param.account.CurrencyParam;
import com.hlyp.api.param.account.RegisterOrSignInParam;
import com.hlyp.api.param.order.HesoCustomNeedParam;
import com.hlyp.api.service.AccountService;
import com.hlyp.api.vo.Json;
import com.thoughtworks.xstream.converters.extended.RegexPatternConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lenovo on 2019/1/4.
 */
@RequestMapping("/account")
@Api(value = "用户模块")
@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;


    @PostMapping("/registerOrSignIn")
    @ApiOperation(value = "登录或注册")
    public  Json registerOrSignIn(@RequestBody RegisterOrSignInParam param) throws  Exception{

        Json json = new Json();

        HesoAccount account = accountService.decodeUserInfo(param.getEncryptedData(),param.getIv(),param.getSessionKey(),param.getOpenId());
        if(account!=null){
            AccountDto dto = new AccountDto();
            BeanUtils.copyProperties(account,dto);
            dto.setToken(account.getLoginPassword());
            json.setMsg("操作成功");
            json.setSuccess(true);
            json.setData(dto);
        }else {
            json.setMsg("操作失败");
            json.setSuccess(false);

        }
      return json;
    }

    @PostMapping("/updateAccount")
    @ApiOperation(value = "修改用户信息")
    public Json uploadPicture(@RequestBody AccountParam accountParam){
        Json json = new Json();
        Boolean flag = accountService.findAccount(accountParam);
        if(flag){
            json.setMsg("修改成功");
            json.setSuccess(flag);
        }else {
            json.setMsg("用户名已存在");
            json.setSuccess(flag);
        }
        return  json;
    }

    @PostMapping("/addBookOrder")
    @ApiOperation(value = "新增预约订单")
    public Json addBookOrder(@RequestBody HesoCustomNeedParam param){
        Json json = new Json();
        int result = accountService.bookOrder(param);
        if(result == 0){
            json.setMsg("预约失败");
            json.setSuccess(false);
        }else {
            json.setMsg("预约成功");
            json.setSuccess(true);
        }
        return json;
    }


    @PostMapping("/getAccountCurrency")
    @ApiOperation(value = "查询用户余额")
    public Json getAccountCurrency(@RequestBody CurrencyParam param){
        Json json = new Json();
        HesoCurrency hesoCurrency = accountService.fingCurrency(param.getAccount());
        if(hesoCurrency!=null){
            json.setMsg("查询成功");
            json.setSuccess(true);
            json.setData(hesoCurrency);
        }else {
            json.setMsg("查询失败");
            json.setSuccess(false);
        }
        return json;
    }


}
