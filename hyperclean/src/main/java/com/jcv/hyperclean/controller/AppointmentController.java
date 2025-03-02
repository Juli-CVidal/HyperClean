package com.jcv.hyperclean.controller;

import com.jcv.hyperclean.dto.AppointmentDTO;
import com.jcv.hyperclean.dto.request.AppointmentRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController {

    @PostMapping
    public ResponseEntity<AppointmentRequestDTO> create(@RequestBody AppointmentRequestDTO requestDTO) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> get(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/by-vehicle/{vehicleId}")
    public ResponseEntity<List<AppointmentDTO>> getByVehicle(@PathVariable Long vehicleId) {
        return null;
    }
}
