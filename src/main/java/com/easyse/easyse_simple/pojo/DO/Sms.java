package com.easyse.easyse_simple.pojo.DO;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.easyse.easyse_simple.utils.RedisCache;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: zky
 * @date: 2022/12/18
 * @description: 短信验证码
 */
@Component
public class Sms {
    @Autowired
    RedisCache redisUtils;


    public static void messagePost(String u_phone, String message){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI5tKNX5PLDxL1Z8x7Da4y", "7cGVonlthF9mCN6ntfNs4PDuFxchYv");
        /** use STS Token
         DefaultProfile profile = DefaultProfile.getProfile(
         "<your-region-id>",           // The region ID
         "<your-access-key-id>",       // The AccessKey ID of the RAM account
         "<your-access-key-secret>",   // The AccessKey Secret of the RAM account
         "<your-sts-token>");          // STS Token
         **/
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers",u_phone);
        request.putQueryParameter("SignName", "easyse");
        request.putQueryParameter("TemplateCode",  "SMS_264580651");
        request.putQueryParameter("TemplateParam", "{\"code\":"+message+"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
