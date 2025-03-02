package com.jcv.hyperclean.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment extends BasicModel {

    private double price;
    private LocalDate paymentDate;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
}
