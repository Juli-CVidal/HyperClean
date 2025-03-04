package com.jcv.hyperclean.dto.request;

import com.jcv.hyperclean.enums.PaymentType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    @NotNull(message = "Please enter the amount")
    @Min(value = 0L, message = "The amount must be 0 or positive")
    private Double amount;

    @PastOrPresent(message = "You cannot pay for future appointments")
    private LocalDateTime paymentDate = LocalDateTime.now();

    private PaymentType type = PaymentType.CASH;

    @NotNull(message = "You must enter the appointment you are trying to pay")
    @Min(value = 1L, message = "Please enter a valid appointment id")
    private Long appointmentId;
}
