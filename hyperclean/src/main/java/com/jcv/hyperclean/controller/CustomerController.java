package com.jcv.hyperclean.controller;

import com.jcv.hyperclean.dto.CustomerDTO;
import com.jcv.hyperclean.dto.request.CustomerRequestDTO;
import com.jcv.hyperclean.model.Customer;
import com.jcv.hyperclean.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> create(@Valid @RequestBody CustomerRequestDTO requestDTO) {
        Customer customer = customerService.create(requestDTO);
        return ResponseEntity.ok(CustomerDTO.from(customer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(CustomerDTO.from(customerService.findById(id)));
    }

    @GetMapping("/by-email")
    public ResponseEntity<CustomerDTO> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(CustomerDTO.from(customerService.findByEmail(email)));
    }

    @GetMapping("/by-phone")
    public ResponseEntity<CustomerDTO> getByPhone(@RequestParam String phone) {
        return ResponseEntity.ok(CustomerDTO.from(customerService.findByPhone(phone)));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAll() {
        return ResponseEntity.ok(customerService.findAll());
    }
}
