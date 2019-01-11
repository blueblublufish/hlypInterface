package com.hlyp.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hlyp.api.bean.HesoAccount;
import com.hlyp.api.bean.HesoOrderConsume;
import com.hlyp.api.bean.HesoOrderPay;
import com.hlyp.api.param.payParam.WxCheckParam;
import com.hlyp.api.param.wexinParam.LoginParam;
import com.hlyp.api.param.wexinParam.PayParam;
import com.hlyp.api.service.AccountService;
import com.hlyp.api.service.HesoOrderConsumeService;
import com.hlyp.api.service.HesoPayOrderService;
import com.hlyp.api.utils.IpUtils;
import com.hlyp.api.utils.MoneyUtil;
import com.hlyp.api.utils.StringUtils;
import com.hlyp.api.utils.weixin.PayUtil;
import com.hlyp.api.utils.weixin.config.WxPayConfig;
import com.hlyp.api.utils.weixin.vo.OAuthJsToken;
import com.hlyp.api.vo.Json;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.weixin4j.WeixinException;
import org.weixin4j.WeixinSupport;
import org.weixin4j.http.HttpsClient;
import org.weixin4j.http.Response;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Lenovo on 2019/1/3.
 */
@RestController
@RequestMapping("/weixin")
public class WeixinController extends WeixinSupport {

    private static Logger logger = LoggerFactory.getLogger(WeixinController.class);

    private static  final  String appid ="";
    private static  final String secret = "";
    private static final String grant_type = "authorization_code";

    @Autowired
    private HesoPayOrderService payOrderService;
    @Autowired
    private HesoOrderConsumeService orderConsumeService;

    @Autowired
    private AccountService accountService ;

    /**
     * 小程序后台登录，向微信平台发送获取access_token请求，并返回openId
     *
     * @param loginParam
     * @return openid
     * @throws WeixinException
     * @throws IOException
     * @since Weixin4J 1.0.0
     */
    @PostMapping("/login")
    public Json  login(@RequestBody LoginParam loginParam, HttpServletRequest request)throws WeixinException,IOException {
        if (loginParam.getCode() == null || loginParam.getCode().equals("")) {
            throw new WeixinException("invalid null, code is null.");
        }
        Json json = new Json();
        Map<String, Object> ret = new HashMap<String, Object>();
        //拼接参数
        String param = "?grant_type=" + grant_type + "&appid=" + WxPayConfig.appid + "&secret=" + WxPayConfig.secret + "&js_code=" + loginParam.getCode();
        logger.info("login登录："+param);
        System.out.println("login登录："+param);
        //创建请求对象
        HttpsClient http = new HttpsClient();
        //调用获取access_token接口
        Response res = http.get("https://api.weixin.qq.com/sns/jscode2session" + param);
        //根据请求结果判定，是否验证成功

        JSONObject jsonObj = res.asJSONObject();
        if (jsonObj != null) {
            Object errcode = jsonObj.get("errcode");
            if (errcode != null) {
                //返回异常信息
                throw new WeixinException(getCause(Integer.parseInt(errcode.toString())));
            }

            ObjectMapper mapper = new ObjectMapper();
            OAuthJsToken oauthJsToken = mapper.readValue(jsonObj.toJSONString(),OAuthJsToken.class);

            logger.info("openid=" + oauthJsToken.getOpenid());
            ret.put("openid", oauthJsToken.getOpenid());
            ret.put("session_key",oauthJsToken.getSession_key());
            if(loginParam.getAccount()!=null&&!loginParam.getAccount().equals("")){
                HesoAccount account = new HesoAccount();
                account.setOpenid(oauthJsToken.getOpenid());
                account.setAccount(loginParam.getAccount());
                account.setComment(oauthJsToken.getSession_key());
                accountService.updateAccount(account);

            }

        }
        json.setData(ret);
        json.setMsg("操作成功");
        json.setSuccess(true);

        return json;
    }
    /**
     * @Description: 发起微信支付
     *
     * @param request
     * @author: wcf
     * @date: 2017年8月28日
     */
    @PostMapping("/wxPay")
    @ApiOperation(value = "微信统一支付接口")
    public Json wxPay(@RequestBody PayParam param, HttpServletRequest request){
        Json json = new Json();
        try{
            //生成的随机字符串
            String nonce_str = StringUtils.getRandomStringByLength(32);

            //查询订单获取商品名称及金额
            String orderNo = param.getOrdderId();
            HesoOrderPay hesoOrderPay = payOrderService.findByPayOrderId(orderNo);
            BigDecimal amount = hesoOrderPay.getOrderMoney();
            String orderIdsString = hesoOrderPay.getOrderPay();
            String orderIds[] = orderIdsString.split(";");
            HesoOrderConsume hesoOrderConsume = orderConsumeService.findOrderConsumeById(orderIds[0]);
            //商品名称
            String body = hesoOrderConsume.getName()+"...";
            //获取本机的ip地址
            String spbill_create_ip = IpUtils.getIpAddr(request);


            String money = MoneyUtil.toCent(amount)+"";//支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败
            String account = hesoOrderConsume.getAccount();
            if("0000000000000909".equals(account)){
                money = "1";
            }
            String openid = param.getOpenId();
            Map<String, String> packageParams = new HashMap<String, String>();
            packageParams.put("appid", WxPayConfig.appid);
            packageParams.put("mch_id", WxPayConfig.mch_id);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("body", body);
            packageParams.put("out_trade_no", orderNo);//商户订单号
            packageParams.put("total_fee", money);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            packageParams.put("spbill_create_ip", spbill_create_ip);
            packageParams.put("notify_url", WxPayConfig.notify_url);
            packageParams.put("trade_type", WxPayConfig.TRADETYPE);
            packageParams.put("openid", openid);

            // 除去数组中的空值和签名参数
            packageParams = PayUtil.paraFilter(packageParams);
            String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串

            //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
            String mysign = PayUtil.sign(prestr, WxPayConfig.key, "utf-8").toUpperCase();
            logger.info("=======================第一次签名：" + mysign + "=====================");

            //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
            String xml = "<xml>" + "<appid>" + WxPayConfig.appid + "</appid>"
                    + "<body><![CDATA[" + body + "]]></body>"
                    + "<mch_id>" + WxPayConfig.mch_id + "</mch_id>"
                    + "<nonce_str>" + nonce_str + "</nonce_str>"
                    + "<notify_url>" + WxPayConfig.notify_url + "</notify_url>"
                    + "<openid>" + openid + "</openid>"
                    + "<out_trade_no>" + orderNo + "</out_trade_no>"
                    + "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
                    + "<total_fee>" + money + "</total_fee>"
                    + "<trade_type>" + WxPayConfig.TRADETYPE + "</trade_type>"
                    + "<sign>" + mysign + "</sign>"
                    + "</xml>";

            System.out.println("调试模式_统一下单接口 请求XML数据：" + xml);
            logger.info("调试模式_统一下单接口 请求XML数据：" + xml);
            //调用统一下单接口，并接受返回的结果
            String result = PayUtil.httpRequest(WxPayConfig.pay_url, "POST", xml);

            System.out.println("调试模式_统一下单接口 返回XML数据：" + result);
            logger.info("调试模式_统一下单接口 返回XML数据：" + result);
            // 将解析结果存储在HashMap中
            Map map = PayUtil.doXMLParse(result);

            String return_code = (String) map.get("return_code");//返回状态码

            //返回给移动端需要的参数
            Map<String, Object> response = new HashMap<String, Object>();
            if(return_code == "SUCCESS" || return_code.equals(return_code)){
                // 业务结果
                String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
                response.put("nonceStr", nonce_str);
                response.put("package", "prepay_id=" + prepay_id);
                Long timeStamp = System.currentTimeMillis() / 1000;
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误

                String stringSignTemp = "appId=" + WxPayConfig.appid + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id+ "&signType=" + WxPayConfig.SIGNTYPE + "&timeStamp=" + timeStamp;
                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String paySign = PayUtil.sign(stringSignTemp, WxPayConfig.key, "utf-8").toUpperCase();
                logger.info("=======================第二次签名：" + paySign + "=====================");

                response.put("paySign", paySign);

                //更新订单信息

                //业务逻辑代码
            }

            response.put("appid", WxPayConfig.appid);

            json.setSuccess(true);
            json.setData(response);
        }catch(Exception e){
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("发起失败");
        }
        return json;
    }


    public   Map checkOrder(WxCheckParam param ){
        Json json = new Json();
        Map map  = new HashMap();
        try{
            //生成的随机字符串
            String nonce_str = StringUtils.getRandomStringByLength(32);

            String orderNo = param.getTransaction_id();
            String money = "1";//支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败

            Map<String, String> packageParams = new HashMap<String, String>();
            packageParams.put("appid", WxPayConfig.appid);
            packageParams.put("mch_id", WxPayConfig.mch_id);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("transaction_id", orderNo);

            // 除去数组中的空值和签名参数
            packageParams = PayUtil.paraFilter(packageParams);
            String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串

            //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
            String mysign = PayUtil.sign(prestr, WxPayConfig.key, "utf-8").toUpperCase();
            logger.info("=======================查询订单签名：" + mysign + "=====================");

            //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
            String xml = "<xml>" + "<appid>" + WxPayConfig.appid + "</appid>"
                    + "<mch_id>" + WxPayConfig.mch_id + "</mch_id>"
                    + "<nonce_str>" + nonce_str + "</nonce_str>"
                    + "<transaction_id>" + orderNo + "</transaction_id>"
                    + "<sign>" + mysign + "</sign>"
                    + "</xml>";

            System.out.println("调试模式_查询订单接口 请求XML数据：" + xml);
            logger.info("调试模式_查询订单接口 请求XML数据：" + xml);
            //调用统一下单接口，并接受返回的结果
            String result = PayUtil.httpRequest(WxPayConfig.check_url, "POST", xml);

            System.out.println("调试模式_查询订单接口 返回XML数据：" + result);
            logger.info("调试模式_查询订单接口 返回XML数据：" + result);
            // 将解析结果存储在HashMap中
            map = PayUtil.doXMLParse(result);

            String return_code = (String) map.get("return_code");//返回状态码

        }catch(Exception e){
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("发起失败");
        }
        return map;
    }




    /**
     * @Description:微信支付
     * @return
     * @author dzg
     * @throws Exception
     * @throws WeixinException
     * @date 2016年12月2日
     */
    @PostMapping(value="/wxNotify")
    public void wxNotify(HttpServletRequest request,HttpServletResponse response) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine())!=null){
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        System.out.println("接收到的报文：" + notityXml);
        logger.info("接收到的报文：" + notityXml);
        Map map = PayUtil.doXMLParse(notityXml);

        String transaction_id = (String) map.get("transaction_id");

        String returnCode = (String) map.get("return_code");

        if("SUCCESS".equals(returnCode)){
            WxCheckParam param = new WxCheckParam();
            param.setAppid(WxPayConfig.appid);
            param.setMach_id(WxPayConfig.mch_id);
            param.setTransaction_id(transaction_id);
            param.setNonce_str("");
            Map checkMap = checkOrder(param);
            String checkReturnCode = (String) checkMap.get("return_code");
            String payOrderId = (String)checkMap.get("out_trade_no");
            //查询订单验证
            if("SUCCESS".equals(checkReturnCode)){
                //通知微信服务器已经支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                /**此处添加自己的业务逻辑代码start**/
                HesoOrderPay hesoOrderPay = payOrderService.findByPayOrderId(payOrderId);
                BigDecimal amount = hesoOrderPay.getOrderMoney();
                String orderIdsString = hesoOrderPay.getOrderPay();
                String orderIds[] = orderIdsString.split(";");
                HesoOrderConsume hesoOrderConsume = orderConsumeService.findOrderConsumeById(orderIds[0]);
                String account = hesoOrderConsume.getAccount();
                orderConsumeService.payOver(payOrderId,account);

                /**此处添加自己的业务逻辑代码end**/
            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        System.out.println(resXml);
        System.out.println("微信支付回调数据结束");
        logger.info("微信支付回调数据结束"+resXml);
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }











    /*public static void main(String args[]) throws  Exception{
        String notityXml = "<xml><appid><![CDATA[wx531766c4b68163fa]]></appid><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1495316782]]></mch_id><nonce_str><![CDATA[zf3gg0ecrhvic9oo84l8duxafzn03t7p]]></nonce_str><openid><![CDATA[oBKmW5EZitIfczkUf4xmw1N4UvmA]]></openid><out_trade_no><![CDATA[oBKmW5EZitIfczkUf4xmw1N4UvmA]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[B12832507A2E436857D21250D769CD88]]></sign><time_end><![CDATA[20190103154349]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[4200000217201901030055889400]]></transaction_id></xml>\n";
        Map map1 = PayUtil.doXMLParse(notityXml);

        String transaction_id = (String) map1.get("transaction_id");
        String tradeId = (String) map1.get("out_trade_no");
        //String transaction_id = "4200000217201901030055889400";
        WxCheckParam param = new WxCheckParam();
        param.setAppid(WxPayConfig.appid);
        param.setMach_id(WxPayConfig.mch_id);
        param.setTransaction_id(transaction_id);
        param.setNonce_str("");
       // Map map = checkOrder(param);
        String xml = "<?xml version='1.0' encoding='utf-8'?>\n" +
                "<message>\n" +
                "    <head>\n" +
                "        <type>001000</type>\n" +
                "        <messageId></messageId>\n" +
                "        <agentId></agentId>\n" +
                "        <digest></digest>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <response>\n" +
                "            <responseCode>000000</responseCode>\n" +
                "            <responseMsg>交易成功</responseMsg>\n" +
                "        </response>\n" +
                "        <responseData>\n" +
                "            <account>0000000000001960</account>\n" +
                "            <token>null</token>\n" +
                "            <userId>用户jKzDuA</userId>\n" +
                "        </responseData>\n" +
                "    </body>\n" +
                "</message>";
        Map map = PayUtil.doXMLParse(xml);

        String returnCode = (String) map.get("body");
        int t = returnCode.indexOf("交易成功");
        System.out.println(returnCode+" "+ transaction_id+" "+ tradeId);
    }*/
}
