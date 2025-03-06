package com.jcv.hyperclean.controller;

import com.jcv.hyperclean.dto.PaymentDTO;
import com.jcv.hyperclean.dto.ResponseDTO;
import com.jcv.hyperclean.dto.request.PaymentRequestDTO;
import com.jcv.hyperclean.exception.HCInvalidDateTimeFormat;
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
    public ResponseEntity<ResponseDTO<PaymentDTO>> create(@Valid @RequestBody PaymentRequestDTO requestDTO)
            throws HCValidationFailedException, HCInvalidDateTimeFormat {
        PaymentDTO dto = PaymentDTO.from(paymentService.create(requestDTO));
        return ResponseEntity.ok(ResponseDTO.of(dto, "The payment was processed successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<PaymentDTO>> get(@PathVariable Long id) {
        PaymentDTO dto = PaymentDTO.from(paymentService.findById(id));
        return ResponseEntity.ok(ResponseDTO.of(dto, "The payment details were retrieved successfully."));
    }

    @GetMapping("/by-appointment/{appointmentId}")
    public ResponseEntity<ResponseDTO<PaymentDTO>> getByAppointment(@PathVariable Long appointmentId) {
        PaymentDTO dto = PaymentDTO.from(paymentService.findByAppointmentId(appointmentId));
        return ResponseEntity.ok(ResponseDTO.of(dto, "The payment linked to the appointment was retrieved successfully."));
    }
}