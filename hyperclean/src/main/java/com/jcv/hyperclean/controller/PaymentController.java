package com.jcv.hyperclean.controller;

import com.jcv.hyperclean.dto.PaymentDTO;
import com.jcv.hyperclean.dto.request.PaymentRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @PostMapping
    public ResponseEntity<PaymentRequestDTO> create(@RequestBody PaymentRequestDTO requestDTO) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> get(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/by-appointment/{appointmentId}")
    public ResponseEntity<PaymentDTO> getByAppointment(@PathVariable Long appointmentId) {
        return null;
    }
}
