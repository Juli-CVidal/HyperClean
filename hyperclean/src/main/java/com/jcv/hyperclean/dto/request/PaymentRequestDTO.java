package com.jcv.hyperclean.dto.request;

import com.jcv.hyperclean.enums.DateValidationType;
import com.jcv.hyperclean.enums.PaymentType;
import com.jcv.hyperclean.util.DateUtils;
import com.jcv.hyperclean.validator.DateValidation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.jcv.hyperclean.util.DateUtils.localDateTimeToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    @NotNull(message = "Please enter the amount")
    @Min(value = 0L, message = "The amount must be 0 or positive")
    private Double amount;

    @DateValidation(type = DateValidationType.PAST, message = "You cannot pay for future appointments")
    private String paymentDate = DateUtils.now();

    private PaymentType type = PaymentType.CASH;

    @NotNull(message = "You must enter the appointment you are trying to pay")
    @Min(value = 1L, message = "Please enter a valid appointment id")
    private Long appointmentId;

    public PaymentRequestDTO(Double amount, LocalDateTime paymentDate, PaymentType type, Long appointmentId) {
        this.amount = amount;
        this.paymentDate = localDateTimeToString(paymentDate);
        this.type = type;
        this.appointmentId = appointmentId;
    }
}
