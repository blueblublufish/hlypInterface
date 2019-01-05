package com.hlyp.api.utils.weixin.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * @Description:
 * @Date: 2018/4/8
 * @Author: wcf
 */
public class WxPayConfig {
    //小程序appid
    @Value("${wxPay.mpAppId}")
    public static final String appid = "wx531766c4b68163fa";
    //微信支付的商户id
    @Value("${wxPay.mchId}")
    public static final String mch_id = "1495316782";
    //微信支付的商户密钥
    @Value("${wxPay.mchKey}")
    public static final String key = "HAGO38g12haiG87Tug57syGuy6afy672";
    //支付成功后的服务器回调url
    public static final String notify_url = "https://hlapi.myhlyp.com/hlypInterface/weixin/wxNotify";
    //签名方式
    public static final String SIGNTYPE = "MD5";
    //交易类型
    public static final String TRADETYPE = "JSAPI";
    //微信统一下单接口地址
    public static final String pay_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //微信查询订单地址
    public static final String check_url = "https://api.mch.weixin.qq.com/pay/orderquery";
    //调用hlyp旧系统地址
    public static final String hlyp_old_url = "https://hlapi.myhlyp.com/heso_project_yiersan/servlet/TransactionServlet";
}
