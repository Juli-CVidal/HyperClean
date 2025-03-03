package com.jcv.hyperclean.cache;

import com.jcv.hyperclean.model.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

@Configuration
public class RedisCacheFactory {
    private final RedisService redisService;
    private final RedisConnectionFactory redisConnectionFactory;

    private static final Duration DEFAULT_EXPIRATION = Duration.ofMinutes(5);

    @Autowired
    public RedisCacheFactory(RedisService redisService, RedisConnectionFactory redisTemplate) {
        this.redisService = redisService;
        this.redisConnectionFactory = redisTemplate;
    }

    private <T> RedisCache<T> createCache(Class<T> type, Duration duration) {
        return new RedisCache<>(redisService, type, duration);
    }

    private <T> RedisCache<T> createCache(Class<T> type) {
        return createCache(type, DEFAULT_EXPIRATION);
    }

    private <T> RedisListCache<T> createListCache(Class<T> type, Duration duration) {
        return new RedisListCache<>(redisConnectionFactory, type.getSimpleName(), duration);
    }

    private <T> RedisListCache<T> createListCache(Class<T> type) {
        return createListCache(type, DEFAULT_EXPIRATION);
    }

    @Bean
    public RedisCache<Appointment> appointmentDTOCache() {
        return createCache(Appointment.class);
    }

    @Bean
    public RedisListCache<Appointment> appointmentDTOListCache() {
        return createListCache(Appointment.class);
    }
}