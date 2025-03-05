package com.jcv.hyperclean.cache;

import com.jcv.hyperclean.model.Appointment;
import com.jcv.hyperclean.model.Customer;
import com.jcv.hyperclean.model.Payment;
import com.jcv.hyperclean.model.Vehicle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Configuration
public class RedisCacheFactory {
    private final RedisConnectionFactory redisConnectionFactory;
    private static final Duration DEFAULT_EXPIRATION = Duration.ofMinutes(5);

    public RedisCacheFactory(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    private <T> RedisItemCache<T> createItemCache(RedisTemplate<String, T> redisTemplate, Duration duration) {
        return new RedisItemCache<>(redisTemplate, duration);
    }

    private <T> RedisItemCache<T> createItemCache(RedisTemplate<String, T> redisTemplate) {
        return createItemCache(redisTemplate, DEFAULT_EXPIRATION);
    }

    private <T> RedisListCache<T> createListCache(RedisTemplate<String, T> redisTemplate, Duration duration) {
        return new RedisListCache<>(redisTemplate, duration);
    }

    private <T> RedisListCache<T> createListCache( RedisTemplate<String, T> redisTemplate) {
        return createListCache(redisTemplate, DEFAULT_EXPIRATION);
    }

    @Bean
    public RedisItemCache<Appointment> appointmentCache(RedisTemplate<String, Appointment> redisTemplate) {
        return createItemCache(redisTemplate);
    }

    @Bean
    public RedisListCache<Appointment> appointmentListCache(RedisTemplate<String, Appointment> redisTemplate) {
        return createListCache(redisTemplate);
    }

    @Bean
    public RedisItemCache<Customer> customerCache(RedisTemplate<String, Customer> redisTemplate) {
        return createItemCache(redisTemplate);
    }

    @Bean
    public RedisListCache<Customer> customerListCache(RedisTemplate<String, Customer> redisTemplate) {
        return createListCache(redisTemplate);
    }

    @Bean
    public RedisItemCache<Vehicle> vehicleCache(RedisTemplate<String, Vehicle> redisTemplate) {
        return createItemCache(redisTemplate);
    }

    @Bean
    public RedisListCache<Vehicle> vehicleListCache(RedisTemplate<String, Vehicle> redisTemplate) {
        return createListCache(redisTemplate);
    }

    @Bean
    public RedisItemCache<Payment> paymentCache(RedisTemplate<String, Payment> redisTemplate) {
        return createItemCache(redisTemplate);
    }

    @Bean
    public RedisListCache<Payment> paymentListCache(RedisTemplate<String, Payment> redisTemplate) {
        return createListCache(redisTemplate);
    }
}
