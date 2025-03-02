package com.jcv.hyperclean.repository;

import com.jcv.hyperclean.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
