package com.hlyp.api.service.impl;

import com.hlyp.api.bean.HesoOrderConsume;
import com.hlyp.api.dao.HesoOrderConsumeDao;
import com.hlyp.api.service.HesoOrderConsumeService;
import com.hlyp.api.utils.weixin.PayUtil;
import com.hlyp.api.utils.weixin.config.WxPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Lenovo on 2019/1/4.
 */
@Service
public class HesoOrderConsumeServiceImpl implements HesoOrderConsumeService {
    @Autowired
    private HesoOrderConsumeDao dao;

    @Override
    public HesoOrderConsume findOrderConsumeById(String orderId) {
        HesoOrderConsume hesoOrderConsume = new HesoOrderConsume();
        hesoOrderConsume.setOrderId(Integer.parseInt(orderId));
        return dao.selectOne(hesoOrderConsume);
    }

    @Override
    public    String payOver(String orderId,String account) {
        String xml = "<?xml version='1.0' encoding='utf-8'?><message><head><type>001035</type><messageId>1</messageId><agentId>001</agentId><digest>MD5数字签名</digest></head><body>" +
                "<account>" +
                account +
                "</account>" +
                "<orderIds>" +
                orderId +
                "</orderIds>" +
                "<type>000000</type>" +
                "<payTime>2018-2-27 04:54:00</payTime><token>0</token></body></message>\n";

        String result = PayUtil.httpRequest(WxPayConfig.hlyp_old_url, "POST", xml);
        System.out.println(result);
        return null;
    }


}
