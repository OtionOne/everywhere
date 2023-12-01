package com.powernode.everywhere.user.controller;

import com.powernode.everywhere.common.core.util.JsonResult;
import com.powernode.everywhere.user.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/user/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;
    @GetMapping("/send")
    public JsonResult sendSms(String phone){
        smsService.sendSms(phone);
        return JsonResult.success();
    }

    @GetMapping("/check")
    public JsonResult checkCode(String phone,String code){
        boolean ans=smsService.checkCode(phone,code);
        if(ans){
            return JsonResult.success();
        }else{
            return JsonResult.error(JsonResult.CODE_ERROR,"验证码错误",null);
        }
    }
}
