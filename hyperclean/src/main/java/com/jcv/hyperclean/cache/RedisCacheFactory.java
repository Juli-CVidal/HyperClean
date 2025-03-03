package com.jcv.hyperclean.cache;

import com.jcv.hyperclean.model.Appointment;
import com.jcv.hyperclean.model.Customer;
import com.jcv.hyperclean.model.Payment;
import com.jcv.hyperclean.model.Vehicle;
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

    private <T> RedisItemCache<T> createCache(Class<T> type, Duration duration) {
        return new RedisItemCache<>(redisService, type, duration);
    }

    private <T> RedisItemCache<T> createCache(Class<T> type) {
        return createCache(type, DEFAULT_EXPIRATION);
    }

    private <T> RedisListCache<T> createListCache(Class<T> type, Duration duration) {
        return new RedisListCache<>(redisConnectionFactory, duration);
    }

    private <T> RedisListCache<T> createListCache(Class<T> type) {
        return createListCache(type, DEFAULT_EXPIRATION);
    }

    @Bean
    public RedisItemCache<Appointment> appointmentCache() {
        return createCache(Appointment.class);
    }

    @Bean
    public RedisListCache<Appointment> appointmentListCache() {
        return createListCache(Appointment.class);
    }

    @Bean
    public RedisItemCache<Customer> customerCache() {
        return createCache(Customer.class);
    }

    @Bean
    public RedisListCache<Customer> customerListCache() {
        return createListCache(Customer.class);
    }

    @Bean
    public RedisItemCache<Vehicle> vehicleCache() {
        return createCache(Vehicle.class);
    }

    @Bean
    public RedisListCache<Vehicle> vehicleListCache() {
        return createListCache(Vehicle.class);
    }

    @Bean
    public RedisItemCache<Payment> paymentCache() {
        return createCache(Payment.class);
    }

    @Bean
    public RedisListCache<Payment> paymentListCache() {
        return createListCache(Payment.class);
    }
}