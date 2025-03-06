package com.jcv.hyperclean.controller;

import com.jcv.hyperclean.dto.ResponseDTO;
import com.jcv.hyperclean.dto.VehicleDTO;
import com.jcv.hyperclean.dto.request.VehicleRequestDTO;
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
    public ResponseEntity<ResponseDTO<VehicleDTO>> create(@Valid @RequestBody VehicleRequestDTO requestDTO) {
        VehicleDTO dto = VehicleDTO.from(vehicleService.create(requestDTO));
        return ResponseEntity.ok(ResponseDTO.of(dto, "The vehicle was created successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<VehicleDTO>> get(@PathVariable Long id) {
        VehicleDTO dto = VehicleDTO.from(vehicleService.findById(id));
        return ResponseEntity.ok(ResponseDTO.of(dto, "The vehicle was found successfully."));
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<ResponseDTO<List<VehicleDTO>>> getByCustomer(@PathVariable Long customerId) {
        List<VehicleDTO> vehicles = mapList(vehicleService.findByCustomerId(customerId), VehicleDTO::from);
        return ResponseEntity.ok(ResponseDTO.of(vehicles, String.format("Found %d vehicle(s)", vehicles.size())));
    }

    @PutMapping("/{id}/assign-to-customer/{customerId}")
    public ResponseEntity<ResponseDTO<VehicleDTO>> assignVehicle(@PathVariable Long id, @PathVariable Long customerId) {
        VehicleDTO dto = VehicleDTO.from(vehicleService.assignToCustomer(id, customerId));
        return ResponseEntity.ok(ResponseDTO.of(dto, "The vehicle was been assigned to the customer."));
    }
}
