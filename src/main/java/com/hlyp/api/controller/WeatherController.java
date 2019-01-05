package com.hlyp.api.controller;

import com.hlyp.api.param.weather.WeatherParam;
import com.hlyp.api.utils.Weather.WeatherApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public  String checkWeather(@RequestBody WeatherParam param){

        return  new WeatherApi().getWeather(param.getDate(),param.getPlace());
    }
 }
