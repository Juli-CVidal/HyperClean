package com.jcv.hyperclean.service;

import com.jcv.hyperclean.dto.CustomerDTO;
import com.jcv.hyperclean.dto.request.CustomerRequestDTO;
import com.jcv.hyperclean.model.Customer;
import com.jcv.hyperclean.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jcv.hyperclean.util.ListUtils.mapList;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerDTO create(CustomerRequestDTO requestDTO) {
        Customer customer = customerRepository.save(Customer.of(requestDTO));
        return CustomerDTO.from(customer);
    }

    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        return customerRepository.getReferenceById(id);
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> findAll() {
        List<Customer> customers = customerRepository.findAll();
        return mapList(customers, CustomerDTO::from);
    }
}
