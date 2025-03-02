package com.jcv.hyperclean.dto;


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
    private Double price;
    private LocalDateTime paymentDate;
    private AppointmentDTO appointment;


    public static PaymentDTO from(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .price(payment.getPrice())
                .paymentDate(payment.getPaymentDate())
                .appointment(AppointmentDTO.from(payment.getAppointment()))
                .build();
    }

    public Payment toModel() {
        return Payment.builder()
                .id(this.id)
                .price(this.price)
                .paymentDate(this.paymentDate)
                .appointment(this.appointment.toModel())
                .build();
    }
}
