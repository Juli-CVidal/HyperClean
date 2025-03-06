package com.jcv.hyperclean.cache;

import com.jcv.hyperclean.util.RedisUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RedisItemCache<T> {
    private final RedisTemplate<String, T> redisTemplate;
    private final Duration duration;
    private final Class<T> clazz;

    public RedisItemCache(RedisTemplate<String, T> redisTemplate, Duration duration, Class<T> clazz) {
        this.redisTemplate = redisTemplate;
        this.duration = duration;
        this.clazz = clazz;
    }

    public T get(String key) {
        T value = redisTemplate.opsForValue().get(formatKey(key));
        if (Objects.isNull(value)) {
            return null;
        }
        return RedisUtils.convertFromMap(value, clazz);
    }

    public void put(String key, T value) {
        redisTemplate.opsForValue().set(formatKey(key), value, duration.getSeconds(), TimeUnit.SECONDS);
    }

    public void invalidate(String key) {
        redisTemplate.delete(formatKey(key));
    }

    public void flushAll() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }

    private String formatKey(String key) {
        return String.format("%s:%s", clazz.getSimpleName(), key);
    }
}