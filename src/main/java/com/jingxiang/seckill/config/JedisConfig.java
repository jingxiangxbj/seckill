package com.jingxiang.seckill.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisConfig {
    private Logger logger = LoggerFactory.getLogger(JedisConfig.class);
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.database}")
    private int database;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWail;
    @Bean
    public JedisPool jedisPoolFactory(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxWaitMillis(maxWail);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMinIdle(minIdle);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,host,port,timeout,null);
        logger.info("jedisPool注入成功！");
        logger.info("redis地址"+host+":"+port);
        return jedisPool;
    }
}
