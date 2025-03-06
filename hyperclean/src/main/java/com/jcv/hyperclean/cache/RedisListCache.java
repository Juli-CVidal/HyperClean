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

    public void set(String key, List<T> values) {
        redisTemplate.delete(formatKey(key));
        redisTemplate.opsForList().rightPushAll(formatKey(key), values);
        redisTemplate.expire(formatKey(key), duration.getSeconds(), TimeUnit.SECONDS);
    }

    public void add(String key, T value) {
        ListOperations<String, T> listOps = redisTemplate.opsForList();
        listOps.rightPush(formatKey(key), value);
        redisTemplate.expire(formatKey(key), duration.getSeconds(), TimeUnit.SECONDS);
    }

    public List<T> get(String key) {
        ListOperations<String, T> listOps = redisTemplate.opsForList();
        List<T> list = listOps.range(formatKey(key), 0, -1);
        if (!ListUtils.isEmpty(list)) {
            return mapList(list, value ->RedisUtils.convertFromMap(value, clazz));
        }
        return new ArrayList<>();
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