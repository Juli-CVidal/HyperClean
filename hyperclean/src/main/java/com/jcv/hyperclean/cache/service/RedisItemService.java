package com.jcv.hyperclean.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class RedisItemService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisItemService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> T get(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(value) || !type.isInstance(value)) {
            return null;
        }
        return type.cast(value);
    }

    public <T> void put(String key, T value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout.getSeconds(), TimeUnit.SECONDS);
    }

    public void invalidate(String key) {
        redisTemplate.delete(key);
    }

    public void flushAll() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }
}