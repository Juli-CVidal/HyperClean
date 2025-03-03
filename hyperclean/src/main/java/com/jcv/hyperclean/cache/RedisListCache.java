package com.jcv.hyperclean.cache;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedisListCache<T> {

    private final RedisTemplate<String, T> redisTemplate;
    private final Duration duration;

    public RedisListCache(RedisConnectionFactory connectionFactory, Duration duration) {
        this.redisTemplate = setupRedisTemplate(connectionFactory);
        this.duration = duration;
    }

    private RedisTemplate<String, T> setupRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    public void set(String cacheKey, List<T> values) {
        redisTemplate.delete(cacheKey);
        redisTemplate.opsForList().rightPushAll(cacheKey, values);
        redisTemplate.expire(cacheKey, duration.getSeconds(), TimeUnit.SECONDS);
    }

    public void add(String cacheKey, T value) {
        ListOperations<String, T> listOps = redisTemplate.opsForList();
        listOps.rightPush(cacheKey, value);
        redisTemplate.expire(cacheKey, duration.getSeconds(), TimeUnit.SECONDS);
    }

    public List<T> get(String cacheKey) {
        ListOperations<String, T> listOps = redisTemplate.opsForList();
        return listOps.range(cacheKey, 0, -1);
    }

    public void invalidate(String cacheKey) {
        redisTemplate.delete(cacheKey);
    }
}