package com.hlyp.api.service.impl;

import com.hlyp.api.bean.HesoAccount;
import com.hlyp.api.bean.HesoOrderConsume;
import com.hlyp.api.bean.HesoOrderConsumeDetail;
import com.hlyp.api.bean.HesoOrderConsumeDetailExample;
import com.hlyp.api.dao.HesoAccountMapper;
import com.hlyp.api.dao.HesoOrderConsumeDao;
import com.hlyp.api.dao.HesoOrderConsumeDetailMapper;
import com.hlyp.api.dao.HesoOrderConsumeMapper;
import com.hlyp.api.service.HesoOrderConsumeService;
import com.hlyp.api.utils.order.AMANIUtil;
import com.hlyp.api.utils.order.MyHttpClient;
import com.hlyp.api.utils.weixin.PayUtil;
import com.hlyp.api.utils.weixin.config.WxPayConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lenovo on 2019/1/4.
 */
@Service
public class HesoOrderConsumeServiceImpl implements HesoOrderConsumeService {
    @Autowired
    private HesoOrderConsumeDao dao;
    @Autowired
    private HesoOrderConsumeMapper orderConsumeMapper;
    @Autowired
    private HesoOrderConsumeDetailMapper detailMapper;

    @Autowired
    private HesoAccountMapper accountMapper;


    private final Logger logger =  LoggerFactory.getLogger(HesoOrderConsumeServiceImpl.class);

    @Override
    public Map<String,String> packToAmani(String orderId)  {
        Map<String,String> map = new HashMap<>();
        HttpClient httpClient =  new MyHttpClient().getHttpsClient();
        String url = "http://api.rcmtm.cn/api/order";
        String string = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><message><head><type>001010</type><messageId>1</messageId><agentId>001</agentId><digest>MD5数字签名</digest></head><body><productId>D17O0002</productId></body></message>";
        HttpPost post = new HttpPost(url);
        String result = "";
        try {
           /* SSLContext sslcontext = createIgnoreVerifySSL();
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslcontext))
                    .build();
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            HttpClients.custom().setConnectionManager(connManager);*/


            post.setHeader("Content-Type", "application/xml");
            post.setHeader("charset", "utf-8");
            post.setHeader("user", "SAX8");
            post.setHeader("pwd", "17ef02940ada86e5e42fe51522e115e1");
            post.setHeader("accept", "application/json");
            post.setHeader("lan", "zh");
            String xmlData = "";

            HesoOrderConsumeDetailExample example = new HesoOrderConsumeDetailExample();
            HesoOrderConsumeDetailExample.Criteria criteria = example.createCriteria();
            criteria.andOrderIdEqualTo(orderId);
            List<HesoOrderConsumeDetail> detailList = detailMapper.selectByExample(example);
            HesoOrderConsume hesoOrderConsume = orderConsumeMapper.selectByPrimaryKey(orderId);
            HesoAccount account = accountMapper.selectByPrimaryKey(hesoOrderConsume.getAccount());
            String xml = new AMANIUtil().packToXmlByOrder(hesoOrderConsume, detailList, account);
            logger.info("xml封装数据:" + xml);
           // System.out.println();
            post.setEntity(new StringEntity(xml, "utf-8"));
            // List<BasicNameValuePair> parameters = new ArrayList<>();
            //  parameters.add(new BasicNameValuePair("xml", xmlData));
            //post.setEntity(new UrlEncodedFormEntity(parameters,"UTF-8"));
            HttpResponse response = httpClient.execute(post);

            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            logger.info("amani返回数据："+result);
            //修改订单状态，录入阿玛尼数据
            HesoOrderConsume orderConsume = new HesoOrderConsume();
            orderConsume.setOrderId(orderId);
            orderConsume.setCheckStatus("1");
            orderConsume.setRemarks("");
            String rescode = "103";
            if(result.contains("101")){
                rescode = "101";
            }else {
                rescode = "102";
            }
            String srt[] = result.split(",");
            String d = srt[1];
            String resdesc = d.substring(d.indexOf(":")+1,d.indexOf("}"));
            orderConsume.setResCode(rescode);
            orderConsume.setResDesc(resdesc);
            orderConsumeMapper.updateByPrimaryKeySelective(orderConsume);
            map.put("rescode",rescode);
            map.put("resdesc",resdesc);

        }catch (Exception e){
            logger.info(e.getMessage());
        }

        return map;
    }

    @Override
    public HesoOrderConsume findOrderConsumeById(String orderId) {
        HesoOrderConsume hesoOrderConsume = orderConsumeMapper.selectByPrimaryKey(orderId);

        return hesoOrderConsume;
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


    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[] { trustManager }, null);
        return sc;
    }
}
