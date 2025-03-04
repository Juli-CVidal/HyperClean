package com.jcv.hyperclean.cache;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedisListCache<T> {

    private final RedisTemplate<String, T> redisTemplate;
    private final Duration duration;

    public RedisListCache(RedisTemplate<String, T> redisTemplate, Duration duration) {
        this.redisTemplate = redisTemplate;
        this.duration = duration;
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