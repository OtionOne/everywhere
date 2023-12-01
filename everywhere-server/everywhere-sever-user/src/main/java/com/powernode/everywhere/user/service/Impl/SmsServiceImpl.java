package com.powernode.everywhere.user.service.Impl;

import com.powernode.everywhere.common.redis.utils.RedisCache;
import com.powernode.everywhere.user.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private RedisCache redisCache;

    @Override
    public void sendSms(String phone) {
        String code=this.getCode(6);
        redisCache.setCacheObject("REGISTER:PHONE:"+phone,code);
    }

    public String getCode(int length){
        String ans= UUID.randomUUID().toString().replaceAll("-","").substring(0,length);
        return ans;
    }
}
