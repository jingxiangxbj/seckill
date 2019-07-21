package com.jingxiang.seckill.service;

import com.jingxiang.seckill.dto.Explor;
import com.jingxiang.seckill.dto.SeckillExecution;
import com.jingxiang.seckill.entity.Seckill;
import com.jingxiang.seckill.entity.SeckillOrder;
import com.jingxiang.seckill.enums.SeckillStatEnum;
import com.jingxiang.seckill.exception.RepeatSeckillException;
import com.jingxiang.seckill.exception.SeckillCloseException;
import com.jingxiang.seckill.exception.SeckillException;
import com.jingxiang.seckill.mapper.SeckillMapper;
import com.jingxiang.seckill.mapper.SeckillOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService{

    private Logger logger = LoggerFactory.getLogger(SeckillServiceImpl.class);
    //设置盐值字符串，随便定义，用于混淆MD5值
    private final String salt = "sjajaspu-i-2jrfm;sd";
    //设置秒杀redis缓存的key
    private final String key = "seckill";
    @Autowired
    private SeckillMapper seckillMapper;
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Seckill> findAll() {
        List<Seckill> seckillList = redisTemplate.boundHashOps(key).values();
        if (seckillList==null || seckillList.size()==0){
            //说明缓存中没有秒杀列表数据
            //查询数据库中秒杀列表数据，并将列表数据循环放入redis缓存中
           seckillList = seckillMapper.findAll();
            for (Seckill seckill:seckillList
                 ) {
                //将秒杀列表数据依次放入redis缓存中，key:秒杀表的ID值；value:秒杀商品数据
                redisTemplate.boundHashOps(key).put(seckill.getSeckillId(),seckill);
            }
            logger.info("findAll -> 从数据库中读取放入缓存中");
        }
        logger.info("findAll -> 从缓存中读取");
        return seckillList;
    }

    @Override
    public Seckill findById(long seckillId) {
          return  seckillMapper.findById(seckillId);
    }

    @Override
    public Explor getSeckillUrl(long seckillId) {
       Seckill seckill = (Seckill) redisTemplate.boundHashOps(key).get(seckillId);
       if (seckill == null){
           //说明redis缓存中没有此key对应的value
           //查询数据库，并将数据放入缓存中
           seckill =seckillMapper.findById(seckillId);
           if (seckill == null){
               return new Explor(false,seckillId);
           }
           //查询到了，存入redis缓存中。 key:秒杀表的ID值； value:秒杀表数据
           redisTemplate.boundHashOps(key).put(seckillId,seckill);
           logger.info("RedisTemplate -> 从数据库中读取并放入缓存中");
       }
        logger.info("RedisTemplate -> 从缓存中读取");
        Date now = new Date();
        if (now.getTime()<seckill.getStartTime().getTime() || now.getTime()>seckill.getEndTime().getTime()){
            return new Explor(false,seckillId,now,seckill.getStartTime(),seckill.getEndTime());
        }

        return new Explor(true,seckillId,getMd5(seckillId));

    }
    /**
     * 使用注解式事务方法的有优点：开发团队达成了一致约定，明确标注事务方法的编程风格
     * 使用事务控制需要注意：
     * 1.保证事务方法的执行时间尽可能短，不要穿插其他网络操作PRC/HTTP请求（可以将这些请求剥离出来）
     * 2.不是所有的方法都需要事务控制，如只有一条修改的操作、只读操作等是不需要进行事务控制的
     * <p>
     * Spring默认只对运行期异常进行事务的回滚操作，对于编译异常Spring是不进行回滚的，所以对于需要进行事务控制的方法尽可能将可能抛出的异常都转换成运行期异常
     */
    @Override
    @Transactional

    public SeckillExecution executeSeckill(long seckillId, BigDecimal money, String md5, long userPhone) {
        if (md5==null || !getMd5(seckillId).equals(md5)){
            throw new SeckillException("seckill data rewrite");
        }
        //记录秒杀订单信息
        int insertCount = seckillOrderMapper.insertSeckillOrder(seckillId, userPhone, money);
        if (insertCount<=0){
            //唯一性：seckillId,userPhone，保证一个用户只能秒杀一件商品
            throw new RepeatSeckillException("重复秒杀");
        }
        int stockCount = seckillMapper.disStock(seckillId, new Date());
        if (stockCount<=0){
            throw new SeckillCloseException("秒杀结束");
        }
        //执行秒杀逻辑：1.减库存；2.储存秒杀订单
        //秒杀成功
        SeckillOrder seckillOrder = seckillOrderMapper.findById(seckillId, userPhone);
        //更新缓存（更新库存数量）
        Seckill seckill = (Seckill) redisTemplate.boundHashOps(key).get(seckillId);
       seckill.setStockCount(seckill.getStockCount()-1);
        redisTemplate.boundHashOps(key).put(seckillId,seckill);
        return new SeckillExecution(seckillId,SeckillStatEnum.SUCCESS.getCode(),SeckillStatEnum.SUCCESS.getStatusInfo(),seckillOrder);
    }
    //转换特定字符串的过程，不可逆的算法
    //生成MD5值
    public String getMd5(long seckillId){
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
