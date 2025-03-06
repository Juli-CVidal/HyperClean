package com.jcv.hyperclean.dto;


import com.jcv.hyperclean.enums.PaymentType;
import com.jcv.hyperclean.model.Payment;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static com.jcv.hyperclean.util.DateUtils.localDateTimeToString;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentDTO extends BasicDTO {
    private Double amount;
    private String paymentDate;
    private Long appointmentId;
    private PaymentType type;


    public static PaymentDTO from(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .paymentDate(localDateTimeToString(payment.getPaymentDate()))
                .type(payment.getType())
                .appointmentId(payment.getAppointment().getId())
                .build();
    }
}
