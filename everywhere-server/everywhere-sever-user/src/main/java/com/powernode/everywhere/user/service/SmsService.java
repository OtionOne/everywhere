package com.powernode.everywhere.user.service;

public interface SmsService {
    void sendSms(String phone);
    String getCode();
}
