package com.jcv.hyperclean.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jcv.hyperclean.dto.request.PaymentRequestDTO;
import com.jcv.hyperclean.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "payment")
public class Payment extends BasicModel {
    private Double amount;
    private LocalDateTime paymentDate;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    @JsonIgnore
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    private PaymentType type;

    public static Payment of(PaymentRequestDTO requestDTO) {
        return Payment.builder()
                .amount(requestDTO.getAmount())
                .type(requestDTO.getType())
                .paymentDate(requestDTO.getPaymentDate())
                .build();
    }
}
