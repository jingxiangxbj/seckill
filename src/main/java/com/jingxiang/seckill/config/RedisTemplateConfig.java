package com.jingxiang.seckill.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisTemplateConfig {

private Logger logger = LoggerFactory.getLogger(RedisTemplateConfig.class);

@Bean
public RedisTemplate<Object,Object> redisTemplateConfig(RedisConnectionFactory redisConnectionFactory){
   RedisTemplate<Object,Object> template = new RedisTemplate<>();
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
    ObjectMapper objectMapper = new ObjectMapper();
     objectMapper.setVisibility(PropertyAccessor.ALL,JsonAutoDetect.Visibility.ANY);
     objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
     jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
     template.setKeySerializer(jackson2JsonRedisSerializer);
     template.setHashKeySerializer(jackson2JsonRedisSerializer);
     template.setValueSerializer(jackson2JsonRedisSerializer);
     template.setHashValueSerializer(jackson2JsonRedisSerializer);
     template.setConnectionFactory(redisConnectionFactory);
     template.afterPropertiesSet();
     logger.info("redistemplate序列化配置，转换方式："+jackson2JsonRedisSerializer.getClass().getName());
     return template;
}
}
