package com.powernode.everywhere.user.service.Impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.powernode.everywhere.common.core.exception.CoolDownException;
import com.powernode.everywhere.common.redis.utils.RedisCache;
//import com.powernode.everywhere.user.config.SmsConfig;
import com.powernode.everywhere.user.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Autowired
    private RedisCache redisCache;

//    @Autowired
//    private Client smsClient;

    @Override
    public void sendSms(String phone) {
        String code = this.getCode(6);
        if(redisCache.hasKey("REGISTER:PHONE:" + phone)){
            //已经发送过了
            throw new CoolDownException("信息过热,冷却中...");
        }
        log.info("验证码为:{}", code);
        redisCache.setCacheObject("REGISTER:PHONE:" + phone, code, 30L, TimeUnit.SECONDS);
//        短信发送
//        假装已经发送成功
//        this.send(code);
    }

    @Override
    public String getCode(int length) {
        String ans = UUID.randomUUID().toString().replaceAll("-", "").substring(0, length);
        return ans;
    }

    @Override
    public boolean checkCode(String phone, String code) {
        String key= "REGISTER:PHONE:" + phone;
        if(redisCache.getCacheObject(key).equals(code)){
            redisCache.deleteObject(key);
            return true;
        }
        return false;
    }

    public void send(String code) {
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName("阿里云短信测试")
                .setTemplateCode("SMS_154950909")
                .setPhoneNumbers("19722753527")
                .setTemplateParam("{\"code\":\""+code+"\"},打死都不要告诉别人哦!");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
//            SendSmsResponse sendSmsResponse = smsClient.sendSmsWithOptions(sendSmsRequest, runtime);
//            sendSmsResponse.getBody();
        } catch (TeaException error) {
            // 错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }
}
