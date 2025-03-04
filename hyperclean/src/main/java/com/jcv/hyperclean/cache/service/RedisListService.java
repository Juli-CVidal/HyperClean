package com.jcv.hyperclean.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisListService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisListService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> List<T> get(String cacheKey) {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        List<Object> rawList = listOps.range(cacheKey, 0, -1);
        if (rawList != null) {
            return (List<T>) rawList;
        }
        return List.of();
    }

    public <T> void put(String cacheKey, List<T> values, Duration ttl) {
        redisTemplate.delete(cacheKey);

        ListOperations<String, Object> listOps = redisTemplate.opsForList();

        values.forEach(value -> listOps.rightPush(cacheKey, value));

        redisTemplate.expire(cacheKey, ttl.getSeconds(), TimeUnit.SECONDS);
    }

    public <T> void add(String cacheKey, T value, Duration ttl) {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        listOps.rightPush(cacheKey, value);

        redisTemplate.expire(cacheKey, ttl.getSeconds(), TimeUnit.SECONDS);
    }

    public void invalidate(String cacheKey) {
        redisTemplate.delete(cacheKey);
    }

    public void flushAll() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }
}
