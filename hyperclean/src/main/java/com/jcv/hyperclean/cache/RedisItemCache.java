package com.jcv.hyperclean.cache;

import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RedisItemCache<T> {
    private final RedisTemplate<String, T> redisTemplate;
    private final Duration duration;

    public RedisItemCache(RedisTemplate<String, T> redisTemplate, Duration duration) {
        this.redisTemplate = redisTemplate;
        this.duration = duration;
    }

    public T get(String key) {
        T value = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(value)) {
            return null;
        }
        return value;
    }

    public void put(String key, T value) {
        redisTemplate.opsForValue().set(key, value, duration.getSeconds(), TimeUnit.SECONDS);
    }

    public void invalidate(String key) {
        redisTemplate.delete(key);
    }

    public void flushAll() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }
}