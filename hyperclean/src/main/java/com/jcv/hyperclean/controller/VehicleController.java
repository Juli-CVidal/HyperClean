package com.jcv.hyperclean.controller;

import com.jcv.hyperclean.dto.VehicleDTO;
import com.jcv.hyperclean.dto.request.VehicleRequestDTO;
import com.jcv.hyperclean.model.Vehicle;
import com.jcv.hyperclean.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jcv.hyperclean.util.ListUtils.mapList;

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
        return ResponseEntity.ok(VehicleDTO.from(vehicleService.create(requestDTO)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(VehicleDTO.from(vehicleService.findById(id)));
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<List<VehicleDTO>> getByCustomer(@PathVariable Long customerId) {
        List<Vehicle> vehicles = vehicleService.findByCustomerId(customerId);
        return ResponseEntity.ok(mapList(vehicles, VehicleDTO::from));
    }

    @PutMapping("/{id}/assign-to-customer/{customerId}")
    public ResponseEntity<VehicleDTO> assignVehicle(@PathVariable Long id, @PathVariable Long customerId) {
        return ResponseEntity.ok(VehicleDTO.from(vehicleService.assignToCustomer(id, customerId)));
    }
}
