package com.jcv.hyperclean.service;

import com.jcv.hyperclean.cache.RedisItemCache;
import com.jcv.hyperclean.cache.RedisListCache;
import com.jcv.hyperclean.dto.CustomerDTO;
import com.jcv.hyperclean.dto.request.CustomerRequestDTO;
import com.jcv.hyperclean.model.Customer;
import com.jcv.hyperclean.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.jcv.hyperclean.util.ListUtils.mapList;

@Service
public class CustomerService extends CacheableService<Customer> {
    private static final String ALL_CUSTOMERS_CACHE_KEY = "allCustomers";
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, RedisItemCache<Customer> customerCache, RedisListCache<Customer> customerListCache) {
        super(customerCache, customerListCache);
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerDTO create(CustomerRequestDTO requestDTO) {
        Customer customer = customerRepository.save(Customer.of(requestDTO));

        putInCache(String.valueOf(customer.getId()), customer);
        invalidateListCache(ALL_CUSTOMERS_CACHE_KEY);
        return CustomerDTO.from(customer);
    }

    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        String cacheKey = String.valueOf(id);
        Optional<Customer> cachedCustomer = getCached(cacheKey);
        if (cachedCustomer.isPresent()) {
            return cachedCustomer.get();
        }

        Customer customer = customerRepository.getReferenceById(id);
        putInCache(cacheKey, customer);
        return customer;
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> findAll() {
        Optional<List<Customer>> cachedCustomers = getCachedList(ALL_CUSTOMERS_CACHE_KEY);
        if (cachedCustomers.isPresent()) {
            return mapList(cachedCustomers.get(), CustomerDTO::from);
        }

        List<Customer> customers = customerRepository.findAll();
        setListCache(ALL_CUSTOMERS_CACHE_KEY, customers);
        return mapList(customers, CustomerDTO::from);
    }
}
