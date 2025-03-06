package com.jcv.hyperclean.controller;

import com.jcv.hyperclean.dto.AppointmentDTO;
import com.jcv.hyperclean.dto.request.AppointmentRequestDTO;
import com.jcv.hyperclean.exception.HCValidationFailedException;
import com.jcv.hyperclean.model.Appointment;
import com.jcv.hyperclean.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jcv.hyperclean.util.ListUtils.mapList;

@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> create(@Valid @RequestBody AppointmentRequestDTO requestDTO) {
        return ResponseEntity.ok(AppointmentDTO.from(appointmentService.create(requestDTO)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(AppointmentDTO.from(appointmentService.findById(id)));
    }

    @GetMapping("/by-vehicle/{vehicleId}")
    public ResponseEntity<List<AppointmentDTO>> getByVehicle(@PathVariable Long vehicleId) {
        List<Appointment> appointments = appointmentService.findByVehicleId(vehicleId);
        return ResponseEntity.ok(mapList(appointments, AppointmentDTO::from));
    }

    @PatchMapping("/{id}/mark-as-in-progress")
    public ResponseEntity<AppointmentDTO> markAsInProgress(@PathVariable Long id) throws HCValidationFailedException {
        return ResponseEntity.ok(appointmentService.markAsInProgress(id));
    }

    @PatchMapping("/{id}/mark-as-finished")
    public ResponseEntity<AppointmentDTO> markAsFinished(@PathVariable Long id) throws HCValidationFailedException {
        return ResponseEntity.ok(appointmentService.markAsFinished(id));
    }
}
