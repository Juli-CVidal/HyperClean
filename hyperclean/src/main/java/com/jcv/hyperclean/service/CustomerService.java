package com.jcv.hyperclean.service;

import com.jcv.hyperclean.cache.RedisItemCache;
import com.jcv.hyperclean.cache.RedisListCache;
import com.jcv.hyperclean.dto.CustomerDTO;
import com.jcv.hyperclean.dto.request.CustomerRequestDTO;
import com.jcv.hyperclean.exception.HCNotFoundException;
import com.jcv.hyperclean.model.Customer;
import com.jcv.hyperclean.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import java.util.List;

import static com.jcv.hyperclean.util.ListUtils.mapList;

@Service
public class CustomerService extends CacheableService<Customer> {
    private static final String ALL_CUSTOMERS_CACHE_KEY = "allCustomers";
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, RedisItemCache<Customer> customerCache, RedisListCache<Customer> customerListCache, Validator validator) {
        super(customerCache, customerListCache);
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Customer save(Customer customer) {
        invalidateListCache(ALL_CUSTOMERS_CACHE_KEY);
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer create(@Valid CustomerRequestDTO requestDTO) {
        Customer customer = save(Customer.of(requestDTO));

        putInCache(customer.getId(), customer);
        return customer;
    }

    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        try {
        return findBy(id, customerRepository::findById);
        } catch (EntityNotFoundException e) {
            String errorMsg = String.format("Could not find a customer with id: %s ", id);
            throw new HCNotFoundException(errorMsg);
        }
    }

    @Transactional(readOnly = true)
    public Customer findByEmail(String email) {
        try {
        return findBy(email, customerRepository::findByEmail);
        } catch (EntityNotFoundException e) {
            String errorMsg = String.format("Could not find a customer with email: %s ", email);
            throw new HCNotFoundException(errorMsg);
        }
    }

    @Transactional(readOnly = true)
    public Customer findByPhone(String phone) {
        try {
        return findBy(phone, customerRepository::findByPhone);
        } catch (EntityNotFoundException e) {
            String errorMsg = String.format("Could not find a customer with phone: %s ", phone);
            throw new HCNotFoundException(errorMsg);
        }
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> findAll() {
        List<Customer> customers = safeFindAll(ALL_CUSTOMERS_CACHE_KEY, customerRepository::findAll);
        return mapList(customers, CustomerDTO::from);
    }
}
