package com.hlyp.api.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Lenovo on 2019/1/2.
 */
public class WechatAccountConfig {

    /**
     * 公众账号ID
     */
    @Value("${wxPay.mpAppId}")
    private String mpAppId;

    /**
     * 商户号
     */
    @Value("${wxPay.mchId}")
    private String mchId;

    /**
     * 商户密钥
     */
    @Value("${wxPay.mchKey}")
    private String mchKey;

    /**
     * 商户证书路径
     */
    @Value("${wxPay.keyPath}")
    private String keyPath;

    /**
     * 微信支付异步通知地址
     */
    @Value("${wxPay.notifyUrl}")
    private String notifyUrl;

    public String getMpAppId() {
        return mpAppId;
    }

    public void setMpAppId(String mpAppId) {
        this.mpAppId = mpAppId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
