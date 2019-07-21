package com.jingxiang.seckill.exception;

public class SeckillException  extends RuntimeException{
    public SeckillException(String message){
        super(message);
    }
    public SeckillException(String message,Throwable throwable){
        super(message,throwable);
    }
}
