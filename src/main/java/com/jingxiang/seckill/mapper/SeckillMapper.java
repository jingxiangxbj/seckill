package com.jingxiang.seckill.mapper;

import com.jingxiang.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface SeckillMapper {

   List<Seckill> findAll();
   Seckill findById(Long id);
   int disStock(@Param("seckillId") Long seckillId, @Param("killTime") Date killTime);
}
