package com.jcv.hyperclean.cache;

import com.jcv.hyperclean.cache.service.RedisItemService;

import java.time.Duration;

public class RedisItemCache<T> {

    private final RedisItemService redisItemService;
    private final Class<T> type;
    private final Duration duration;

    public RedisItemCache(RedisItemService redisItemService, Class<T> type, Duration duration) {
        this.redisItemService = redisItemService;
        this.type = type;
        this.duration = duration;
    }

    public T get(String key) {
        return redisItemService.get(key, type);
    }

    public void put(String key, T value) {
        redisItemService.put(key, value, duration);
    }

    public void invalidate(String key) {
        redisItemService.invalidate(key);
    }

    public void flushAll() {
        redisItemService.flushAll();
    }
}