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
    private final String cacheKey;
    private final Duration duration;

    public RedisListCache(RedisConnectionFactory connectionFactory, String cacheKey, Duration duration) {
        this.redisTemplate = setupRedisTemplate(connectionFactory);
        this.cacheKey = cacheKey;
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

    public void set(List<T> values) {
        redisTemplate.delete(cacheKey);
        redisTemplate.opsForList().rightPushAll(cacheKey, values);
        redisTemplate.expire(cacheKey, duration.getSeconds(), TimeUnit.SECONDS);
    }

    public void add(T value) {
        ListOperations<String, T> listOps = redisTemplate.opsForList();
        listOps.rightPush(cacheKey, value);
        redisTemplate.expire(cacheKey, duration.getSeconds(), TimeUnit.SECONDS);
    }

    public List<T> get() {
        ListOperations<String, T> listOps = redisTemplate.opsForList();
        return listOps.range(cacheKey, 0, -1);
    }

    public void invalidate() {
        redisTemplate.delete(cacheKey);
    }

    public long size() {
        ListOperations<String, T> listOps = redisTemplate.opsForList();
        Long size = listOps.size(cacheKey);
        return size != null ? size : 0;
    }
}