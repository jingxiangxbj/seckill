package com.jingxiang.seckill.mapper;

import com.jingxiang.seckill.entity.SeckillOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface SeckillOrderMapper {
    List<SeckillOrder> findAll();
    SeckillOrder findById(@Param("seckillId") Long seckillId, @Param("userPhone") Long userPhone);
    int insertSeckillOrder(@Param("seckillId") Long seckillId, @Param("userPhone") Long userPhone, @Param("money") BigDecimal money);
}
