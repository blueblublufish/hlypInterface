package com.hlyp.api.utils.weixin;


import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

import java.util.Map;


/**
 * 封装对外访问方法
 * @author liuyazhuang
 *
 */
public class WXCore {

    private static final String WATERMARK = "watermark";
    private static final String APPID = "appid";
    private static final String PUREPHONENUMBER = "purePhoneNumber";



    public static   String  decodeUserInfo(String encryptedData, String iv, String sessionKey){
        try {
            AES aes = new AES();
            byte[] resultByte = aes.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv));
            if(null != resultByte && resultByte.length > 0){
                String userInfo = new String(resultByte, "UTF-8");
                JSONObject jsonObject = JSONObject.fromObject(userInfo);
                String decryptAppid = jsonObject.getString(PUREPHONENUMBER);
                return decryptAppid;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 解密数据
     * @return
     * @throws Exception
     */
    public  String decrypt(String appId, String encryptedData, String sessionKey, String iv){
        String result = "";
        try {
            AES aes = new AES();
            byte[] resultByte = aes.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv));
            if(null != resultByte && resultByte.length > 0){
                result = new String(WxPKCS7Encoder.decode(resultByte));
                JSONObject jsonObject = JSONObject.fromObject(result);
                String decryptAppid = jsonObject.getJSONObject(WATERMARK).getString(APPID);
                if(!appId.equals(decryptAppid)){
                    result = "";
                }
            }
        } catch (Exception e) {
            result = "";
            e.printStackTrace();
        }
        return result;
    }


    public static void main(String[] args) throws Exception{
        String appId = "IUe3wJ/RFHoFk0GkF0upSA==";
        String encryptedData = "Zlr2rSmO42zU6IIbjP+BJAvjfL5As7hYS6Yas2yV+JohXGt0JO0lMq8l76DjL9bpr89a+v5fA9sN82Al6t6sMiqW5cghc4JrUM3viC1uZvLI8qF31lRbcDxTtP0cOctiUGv8jynKHCwAh2/WP21gdsZB9D+P+gi7Jr2F+FJZ/25K1gu6mQ9v2aiwrBmk2qLVCKvYbqQLJxj48YX8agppkw==";
        String sessionKey = "j3CajUcAD+ixmeSThknBtw==";
        String iv = "p2f8jtie3aORLGEJIPoKCw==";
        String result = decodeUserInfo(encryptedData,iv,sessionKey);
         System.out.println(result);
    }
}