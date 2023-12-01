package com.powernode.everywhere.common.core.exception;

public class CoolDownException extends RuntimeException{
    private String code;
    public CoolDownException(String message){
        super(message);
    }
    public CoolDownException(String message,String code){
        super(message);
        this.code=code;
    }
}
