package com.jcv.hyperclean.cache;

import com.jcv.hyperclean.util.ListUtils;
import com.jcv.hyperclean.util.RedisUtils;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.jcv.hyperclean.util.ListUtils.mapList;

public class RedisListCache<T> {

    private final RedisTemplate<String, T> redisTemplate;
    private final Duration duration;
    private final Class<T> clazz;

    public RedisListCache(RedisTemplate<String, T> redisTemplate, Duration duration, Class<T> clazz) {
        this.redisTemplate = redisTemplate;
        this.duration = duration;
        this.clazz = clazz;
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
        List<T> list = listOps.range(cacheKey, 0, -1);
        if (!ListUtils.isEmpty(list)) {
            return mapList(list, value ->RedisUtils.convertFromMap(value, clazz));
        }
        return new ArrayList<>();
    }

    public void invalidate(String cacheKey) {
        redisTemplate.delete(cacheKey);
    }

    public void flushAll() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }
}