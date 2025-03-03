package com.jcv.hyperclean.cache;

import java.time.Duration;

public class RedisItemCache<T> {

    private final RedisService redisService;
    private final Class<T> type;
    private final Duration duration;

    public RedisItemCache(RedisService redisService, Class<T> type, Duration duration) {
        this.redisService = redisService;
        this.type = type;
        this.duration = duration;
    }

    public T get(String key) {
        return redisService.get(key, type);
    }

    public void put(String key, T value) {
        redisService.put(key, value, duration);
    }

    public void invalidate(String key) {
        redisService.evict(key);
    }
}