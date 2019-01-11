package com.hlyp.api.controller;

import com.hlyp.api.param.weather.WeatherParam;
import com.hlyp.api.utils.Weather.WeatherApi;
import com.hlyp.api.vo.Json;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lenovo on 2018/12/27.
 */
@RestController
@RequestMapping("/weather")
@Api(value = "天气模块")
public class WeatherController {
    private final Logger log = LoggerFactory.getLogger(WeatherController.class);
    @PostMapping("/check")
    @ApiOperation(value = "查询天气接口")
    public  Json checkWeather(@RequestBody WeatherParam param){
        Json json = new Json();
        json.setSuccess(true);
        json.setMsg("操作成功");
        Map<String,String> map = new HashMap<String,String>();
        String weather = new WeatherApi().getWeather(param.getDate(),param.getPlace());
         map.put("weather",weather);
        json.setData(map);
        return  json;
    }
 }
