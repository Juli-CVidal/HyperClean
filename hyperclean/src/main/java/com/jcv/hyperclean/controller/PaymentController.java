package com.jcv.hyperclean.controller;

import com.jcv.hyperclean.dto.PaymentDTO;
import com.jcv.hyperclean.dto.request.PaymentRequestDTO;
import com.jcv.hyperclean.exception.HCValidationFailedException;
import com.jcv.hyperclean.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> create(@Valid @RequestBody PaymentRequestDTO requestDTO) throws HCValidationFailedException {
        return ResponseEntity.ok(PaymentDTO.from(paymentService.create(requestDTO)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @GetMapping("/by-appointment/{appointmentId}")
    public ResponseEntity<PaymentDTO> getByAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(paymentService.findByAppointmentId(appointmentId));
    }
}
