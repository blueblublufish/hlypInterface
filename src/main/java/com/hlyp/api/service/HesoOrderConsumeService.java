package com.hlyp.api.service;

import com.hlyp.api.bean.HesoOrderConsume;

import java.util.Map;

/**
 * Created by Lenovo on 2019/1/4.
 */
public interface HesoOrderConsumeService {
    HesoOrderConsume findOrderConsumeById(String orderId);
    String payOver(String orderId,String account);
    Map<String,String> packToAmani(String orderId) ;
}
