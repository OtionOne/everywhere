package com.powernode.everywhere.user.controller;

import com.powernode.everywhere.user.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;
    @GetMapping("/send")
    public String sendSms(String phone){
        smsService.sendSms(phone);
        return "success";
    }
}
