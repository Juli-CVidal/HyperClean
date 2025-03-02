package com.jcv.hyperclean.controller;

import com.jcv.hyperclean.dto.VehicleDTO;
import com.jcv.hyperclean.dto.request.VehicleRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicle")
public class VehicleController {

    @PostMapping
    public ResponseEntity<VehicleRequestDTO> create(@RequestBody VehicleRequestDTO requestDTO) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> get(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<List<VehicleDTO>> getByCustomer(@PathVariable Long customerId) {
        return null;
    }

}
