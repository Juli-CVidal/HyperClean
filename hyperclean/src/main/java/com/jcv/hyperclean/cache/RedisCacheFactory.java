package com.jcv.hyperclean.cache;

import com.jcv.hyperclean.cache.service.RedisItemService;
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
    private final RedisItemService redisItemService;
    private final RedisConnectionFactory redisConnectionFactory;
    private static final Duration DEFAULT_EXPIRATION = Duration.ofMinutes(5);

    public RedisCacheFactory(RedisItemService redisItemService, RedisConnectionFactory redisConnectionFactory) {
        this.redisItemService = redisItemService;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    private <T> RedisItemCache<T> createCache(Class<T> type, Duration duration) {
        return new RedisItemCache<>(redisItemService, type, duration);
    }

    private <T> RedisItemCache<T> createCache(Class<T> type) {
        return createCache(type, DEFAULT_EXPIRATION);
    }

    private <T> RedisListCache<T> createListCache(Class<T> type, RedisTemplate<String, T> redisTemplate, Duration duration) {
        return new RedisListCache<>(redisTemplate, duration);
    }

    private <T> RedisListCache<T> createListCache(Class<T> type, RedisTemplate<String, T> redisTemplate) {
        return createListCache(type, redisTemplate, DEFAULT_EXPIRATION);
    }

    @Bean
    public RedisItemCache<Appointment> appointmentCache() {
        return createCache(Appointment.class);
    }

    @Bean
    public RedisListCache<Appointment> appointmentListCache(RedisTemplate<String, Appointment> redisTemplate) {
        return createListCache(Appointment.class, redisTemplate);
    }

    @Bean
    public RedisItemCache<Customer> customerCache() {
        return createCache(Customer.class);
    }

    @Bean
    public RedisListCache<Customer> customerListCache(RedisTemplate<String, Customer> redisTemplate) {
        return createListCache(Customer.class, redisTemplate);
    }

    @Bean
    public RedisItemCache<Vehicle> vehicleCache() {
        return createCache(Vehicle.class);
    }

    @Bean
    public RedisListCache<Vehicle> vehicleListCache(RedisTemplate<String, Vehicle> redisTemplate) {
        return createListCache(Vehicle.class, redisTemplate);
    }

    @Bean
    public RedisItemCache<Payment> paymentCache() {
        return createCache(Payment.class);
    }

    @Bean
    public RedisListCache<Payment> paymentListCache(RedisTemplate<String, Payment> redisTemplate) {
        return createListCache(Payment.class, redisTemplate);
    }
}
