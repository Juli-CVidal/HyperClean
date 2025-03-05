package com.jcv.hyperclean.dto;


import com.jcv.hyperclean.enums.PaymentType;
import com.jcv.hyperclean.model.Payment;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentDTO extends BasicDTO {
    private Double amount;
    private LocalDateTime paymentDate;
    private Long appointmentId;
    private PaymentType type;


    public static PaymentDTO from(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .type(payment.getType())
                .appointmentId(payment.getAppointment().getId())
                .build();
    }
}
