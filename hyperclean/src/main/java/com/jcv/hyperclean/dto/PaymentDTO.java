package com.jcv.hyperclean.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentDTO extends BasicDTO {
    private Double price;
    private LocalDateTime paymentDate;
    private AppointmentDTO appointment;
}
