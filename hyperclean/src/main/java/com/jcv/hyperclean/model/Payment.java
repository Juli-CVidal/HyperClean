package com.jcv.hyperclean.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

    private Double price;
    private LocalDateTime paymentDate;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
}
