package com.jingxiang.seckill.controller;

import com.jingxiang.seckill.dto.Explor;
import com.jingxiang.seckill.dto.SeckillExecution;
import com.jingxiang.seckill.dto.SeckillResult;
import com.jingxiang.seckill.entity.Seckill;
import com.jingxiang.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("seckill")
public class SeckillController {
    @Autowired
    private SeckillService seckillService;
    @RequestMapping("list")
    public String index(){
        return "page/seckill";
    }
    @RequestMapping(value = "seckillList",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<List<Seckill>> seckillList(){
        try {
            List<Seckill> seckillList = seckillService.findAll();
            return new SeckillResult<List<Seckill>>(true,seckillList);
        }catch (Exception e){
            e.printStackTrace();
            return new SeckillResult<List<Seckill>>(false,"获取秒杀列表失败");
        }
    }
    @RequestMapping(value = "seckillDetail",method = RequestMethod.POST)
    public SeckillResult<Seckill> seckillDetail(long seckillId){
        try {
            Seckill seckill = seckillService.findById(seckillId);
            return new SeckillResult<Seckill>(true,seckill);
        }catch (Exception e){
            e.printStackTrace();
            return new SeckillResult<Seckill>(false,"查询秒杀详情失败");
        }
    }
    @RequestMapping(value = "seckillUrl",method = RequestMethod.POST)
    public SeckillResult<Explor> seckillUrl(long seckillId){
        try {
            Explor seckillUrl = seckillService.getSeckillUrl(seckillId);
            return new SeckillResult<Explor>(true,seckillUrl);
        }catch (Exception e){
            return new SeckillResult<Explor>(false,"获取秒杀地址失败");
        }
    }
    @RequestMapping("seckillExcute")
    public SeckillResult<SeckillExecution> seckillExcute(@RequestParam("seckillId") long seckillId,
                                                         @RequestParam("userPhone") long userPhone,
                                                         @RequestParam("money")BigDecimal money,
                                                         @RequestParam("md5") String md5){
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, money, md5, userPhone);
            return new SeckillResult<SeckillExecution>(true,seckillExecution);
        }catch (Exception e){
            e.printStackTrace();
            return new SeckillResult<>(false,"秒杀失败");
        }
    }

}
