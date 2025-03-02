package com.jcv.hyperclean.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRequestDTO extends BasicRequestDTO {
    @NotNull(message = "Please enter the price")
    @Min(value = 0L, message = "The price must be 0 or positive")
    private Double price;

    @NotNull(message = "You must enter a payment date")
    private LocalDateTime paymentDate;

    @NotNull(message = "You must enter the appointment you are trying to pay")
    private Long appointmentId;
}
