package com.powernode.everywhere.user.service;

import java.util.concurrent.ExecutionException;

public interface SmsService {
    void sendSms(String phone);
    String getCode(int length);

    boolean checkCode(String phone, String code);
}
