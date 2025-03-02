package com.jcv.hyperclean.controller;

import com.jcv.hyperclean.dto.CustomerDTO;
import com.jcv.hyperclean.dto.request.CustomerRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    @PostMapping
    public ResponseEntity<CustomerRequestDTO> create(@RequestBody CustomerRequestDTO requestDTO) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> get(@PathVariable Long id) {
        return null;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAll() {
        return null;
    }
}
