package com.jingxiang.seckill.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum  SeckillStatEnum {
    SUCCESS(0,"秒杀成功"),
    END(1,"秒杀结束"),
    INNER_ERROR(2,"系统错误"),
    REPEAT_KILL(3,"重复秒杀"),
    DATA_REWRITE(4,"数据重写");
    private  int code;
    private String statusInfo;
     SeckillStatEnum(int code,String statusInfo){
        this.code = code;
        this.statusInfo = statusInfo;
    }
}
