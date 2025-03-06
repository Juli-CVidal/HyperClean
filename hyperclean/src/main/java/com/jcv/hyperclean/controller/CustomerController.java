package com.jcv.hyperclean.controller;

import com.jcv.hyperclean.dto.CustomerDTO;
import com.jcv.hyperclean.dto.ResponseDTO;
import com.jcv.hyperclean.dto.request.CustomerRequestDTO;
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
    public ResponseEntity<ResponseDTO<CustomerDTO>> create(@Valid @RequestBody CustomerRequestDTO requestDTO) {
        CustomerDTO dto = CustomerDTO.from(customerService.create(requestDTO));
        return ResponseEntity.ok(ResponseDTO.of(dto, "The customer was created successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<CustomerDTO>> get(@PathVariable Long id) {
        CustomerDTO dto = CustomerDTO.from(customerService.findById(id));
        return ResponseEntity.ok(ResponseDTO.of(dto, "The customer was found successfully."));
    }

    @GetMapping("/by-email")
    public ResponseEntity<ResponseDTO<CustomerDTO>> getByEmail(@RequestParam String email) {
        CustomerDTO dto = CustomerDTO.from(customerService.findByEmail(email));
        return ResponseEntity.ok(ResponseDTO.of(dto, "The customer was found successfully."));
    }

    @GetMapping("/by-phone")
    public ResponseEntity<ResponseDTO<CustomerDTO>> getByPhone(@RequestParam String phone) {
        CustomerDTO dto = CustomerDTO.from(customerService.findByPhone(phone));
        return ResponseEntity.ok(ResponseDTO.of(dto, "The customer was found successfully."));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<CustomerDTO>>> getAll() {
        List<CustomerDTO> customers = customerService.findAll();
        return ResponseEntity.ok(ResponseDTO.of(customers, String.format("Found %d customer(s)", customers.size())));
    }
}
