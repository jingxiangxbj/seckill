package com.jingxiang.seckill.dto;

import com.jingxiang.seckill.entity.SeckillOrder;
import lombok.Data;

/**
 * 封装执行秒杀后的结果
 */
@Data
public class SeckillExecution {
    private long seckillId;
    //秒杀执行结果状态
    private int status;
    //状态表示
    private String statusInfo;
    //秒杀成功的订单对象
    private SeckillOrder seckillOrder;
    public SeckillExecution(long seckillId,int status,String statusInfo,SeckillOrder seckillOrder){
        this.seckillId = seckillId;
        this.status = status;
        this.statusInfo = statusInfo;
        this.seckillOrder = seckillOrder;
    }
}
