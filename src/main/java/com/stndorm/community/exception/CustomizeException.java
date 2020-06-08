package com.stndorm.community.exception;

//继承RuntimeException这样只用在Handler中trycatch，其他地方不需要
public class CustomizeException extends RuntimeException{
    private String message;

    public CustomizeException(ICustomizeErrorCode errorCode){
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}

