package com.jcv.hyperclean.cache;

import com.jcv.hyperclean.model.Appointment;
import com.jcv.hyperclean.model.Customer;
import com.jcv.hyperclean.model.Payment;
import com.jcv.hyperclean.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Configuration
public class RedisCacheFactory {
    private static final Duration DEFAULT_EXPIRATION = Duration.ofMinutes(5);

    @Autowired private RedisTemplate<String, Appointment> appointmentTemplate;
    @Autowired private RedisTemplate<String, Customer> customerTemplate;
    @Autowired private RedisTemplate<String, Payment> paymentTemplate;
    @Autowired private RedisTemplate<String, Vehicle> vehicleTemplate;

    @Autowired private RedisTemplate<String, Appointment> appointmentListTemplate;
    @Autowired private RedisTemplate<String, Customer> customerListTemplate;
    @Autowired private RedisTemplate<String, Payment> paymentListTemplate;
    @Autowired private RedisTemplate<String, Vehicle> vehicleListTemplate;

    private <T> RedisItemCache<T> createItemCache(RedisTemplate<String, T> redisTemplate, Duration duration, Class<T> clazz) {
        return new RedisItemCache<>(redisTemplate, duration, clazz);
    }

    private <T> RedisItemCache<T> createItemCache(RedisTemplate<String, T> redisTemplate, Class<T> clazz) {
        return createItemCache(redisTemplate, DEFAULT_EXPIRATION, clazz);
    }

    private <T> RedisListCache<T> createListCache(RedisTemplate<String, T> redisTemplate, Duration duration, Class<T> clazz) {
        return new RedisListCache<>(redisTemplate, duration, clazz);
    }

    private <T> RedisListCache<T> createListCache(RedisTemplate<String, T> redisTemplate, Class<T> clazz) {
        return createListCache(redisTemplate, DEFAULT_EXPIRATION, clazz);
    }

    @Bean
    public RedisItemCache<Appointment> appointmentCache() {
        return createItemCache(appointmentTemplate, Appointment.class);
    }

    @Bean
    public RedisListCache<Appointment> appointmentListCache() {
        return createListCache(appointmentListTemplate, Appointment.class);
    }

    @Bean
    public RedisItemCache<Customer> customerCache() {
        return createItemCache(customerTemplate, Customer.class);
    }

    @Bean
    public RedisListCache<Customer> customerListCache() {
        return createListCache(customerListTemplate, Customer.class);
    }

    @Bean
    public RedisItemCache<Vehicle> vehicleCache() {
        return createItemCache(vehicleTemplate, Vehicle.class);
    }

    @Bean
    public RedisListCache<Vehicle> vehicleListCache() {
        return createListCache(vehicleListTemplate, Vehicle.class);
    }

    @Bean
    public RedisItemCache<Payment> paymentCache() {
        return createItemCache(paymentTemplate, Payment.class);
    }

    @Bean
    public RedisListCache<Payment> paymentListCache() {
        return createListCache(paymentListTemplate, Payment.class);
    }
}
