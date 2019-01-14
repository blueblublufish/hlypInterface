package com.hlyp.api.utils.Weather;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hlyp.api.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lenovo on 2018/12/25.
 */
@Service
public class WeatherApi {
    public static  String   getWeather(String day,String place) {
        String host = "http://saweather.market.alicloudapi.com";
        String path = "/day15";
        String method = "GET";
        String appcode = "dccfe2d8c5ee40d7a60702d733d10f99";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("area", place);
        //querys.put("areaid", "101230506");

        String result = "";
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            //System.out.println(response.toString());
            //获取response的body
            String weathereString = EntityUtils.toString(response.getEntity());

            JsonElement  je = new JsonParser().parse(weathereString);
            //System.out.println(je.isJsonObject());
            String weatherClearString = je.getAsJsonObject().get("showapi_res_body").getAsJsonObject().get("dayList").toString();
            String [] strs = weatherClearString.split("},");
            //System.out.println(strs.length);
            String weatherToday = "";
            for(int i = 0;i<strs.length;i++){
                String weatherDay = strs[i];
                if(weatherDay.contains(day)){
                    weatherToday = strs[i];
                }
            }
            String strStart = "day_air_temperature\":\"";
            String strEnd = "\",\"day_wind_direction\"";
            int strStartIndex = weatherToday.indexOf(strStart);
            int strEndIndex = weatherToday.indexOf(strEnd);
            //System.out.println(weatherToday);
            result = weatherToday.substring(strStartIndex, strEndIndex).substring(strStart.length());
            //System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static void main(String args[]) throws  Exception{
        String re = getWeather("20181228","广州");
        System.out.println(re);
    }

}
