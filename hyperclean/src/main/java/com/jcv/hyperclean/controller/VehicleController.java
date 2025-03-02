package com.jcv.hyperclean.controller;

import com.jcv.hyperclean.dto.VehicleDTO;
import com.jcv.hyperclean.dto.request.VehicleRequestDTO;
import com.jcv.hyperclean.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicle")
public class VehicleController {
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<VehicleDTO> create(@Valid @RequestBody VehicleRequestDTO requestDTO) {
        return ResponseEntity.ok(vehicleService.create(requestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(VehicleDTO.from(vehicleService.findById(id)));
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<List<VehicleDTO>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(vehicleService.findByCustomerId(customerId));
    }

    @PutMapping("/{id}/assign-to-customer/{customerId}")
    public ResponseEntity<VehicleDTO> assignVehicle(@PathVariable Long id, @PathVariable Long customerId) {
        return ResponseEntity.ok(vehicleService.assignToCustomer(id, customerId));
    }
}
