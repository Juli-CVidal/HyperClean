package com.jcv.hyperclean.controller;

import com.jcv.hyperclean.dto.AppointmentDTO;
import com.jcv.hyperclean.dto.ResponseDTO;
import com.jcv.hyperclean.dto.request.AppointmentRequestDTO;
import com.jcv.hyperclean.exception.HCInvalidDateTimeFormat;
import com.jcv.hyperclean.exception.HCValidationFailedException;
import com.jcv.hyperclean.exception.HCVehicleTimeSlotOccupiedException;
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
    public ResponseEntity<ResponseDTO<AppointmentDTO>> create(@Valid @RequestBody AppointmentRequestDTO requestDTO) throws HCInvalidDateTimeFormat, HCValidationFailedException, HCVehicleTimeSlotOccupiedException {
        AppointmentDTO appointmentDTO = AppointmentDTO.from(appointmentService.create(requestDTO));
        return ResponseEntity.ok(ResponseDTO.of(appointmentDTO, "Appointment created successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<AppointmentDTO>> get(@PathVariable Long id) {
        AppointmentDTO appointmentDTO = AppointmentDTO.from(appointmentService.findById(id));
        return ResponseEntity.ok(ResponseDTO.of(appointmentDTO, "The appointment was found successfully."));
    }

    @GetMapping("/by-vehicle/{vehicleId}")
    public ResponseEntity<ResponseDTO<List<AppointmentDTO>>> getByVehicle(@PathVariable Long vehicleId) {
        List<AppointmentDTO> appointments = mapList(appointmentService.findByVehicleId(vehicleId), AppointmentDTO::from);
        return ResponseEntity.ok(ResponseDTO.of(appointments, String.format("Found %d appointment(s)", appointments.size())));
    }

    @PutMapping("/{id}/mark-as-in-progress")
    public ResponseEntity<ResponseDTO<AppointmentDTO>> markAsInProgress(@PathVariable Long id) throws HCValidationFailedException {
        AppointmentDTO appointmentDTO = appointmentService.markAsInProgress(id);
        return ResponseEntity.ok(ResponseDTO.of(appointmentDTO, "The appointment was marked as in progress."));
    }

    @PutMapping("/{id}/mark-as-finished")
    public ResponseEntity<ResponseDTO<AppointmentDTO>> markAsFinished(@PathVariable Long id) throws HCValidationFailedException {
        AppointmentDTO appointmentDTO = appointmentService.markAsFinished(id);
        return ResponseEntity.ok(ResponseDTO.of(appointmentDTO, "The appointment was marked as finished."));
    }
}
