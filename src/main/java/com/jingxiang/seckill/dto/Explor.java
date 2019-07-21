package com.jingxiang.seckill.dto;

import lombok.Data;

import java.util.Date;

/**
 * 暴露秒杀地址DTO
 */
@Data
public class Explor {
    //加密措施，避免用户通过抓包拿到秒杀地址
    private String md5;
    private long seckillId;
    private Date now;
    private Date start;
    private Date end;
    //是否开启秒杀
    private  boolean exposed;
    public Explor(boolean exposed,long seckillId){
        this.exposed = exposed;
        this.seckillId = seckillId;
    }
    public Explor(boolean exposed,long seckillId,Date now,Date start,Date end){
        this.seckillId = seckillId;
        this.exposed = exposed;
        this.now = now;
        this.start = start;
        this.end = end;
    }
    public Explor(boolean exposed,long seckillId,String md5){
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.md5 = md5;
    }
}
