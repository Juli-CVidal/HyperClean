package com.jcv.hyperclean.model;

import com.jcv.hyperclean.dto.request.AppointmentRequestDTO;
import com.jcv.hyperclean.enums.AppointmentStatus;
import com.jcv.hyperclean.enums.ServiceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "appointment")
public class Appointment extends BasicModel {
    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)
    private ServiceType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    public static Appointment of(AppointmentRequestDTO requestDTO) {
        return Appointment.builder()
                .appointmentDate(requestDTO.getAppointmentDate())
                .status(Optional.ofNullable(requestDTO.getStatus()).orElse(AppointmentStatus.PENDING))
                .type(requestDTO.getType())
                .build();
    }

    public boolean isPending() {
        return status == AppointmentStatus.PENDING;
    }

    public boolean isInProgress() {
        return status == AppointmentStatus.IN_PROGRESS;
    }

    public boolean wasPaid() {
        return status == AppointmentStatus.PAID;
    }

    public boolean wasMarkedAsFinished() {
        return status == AppointmentStatus.FINISHED;
    }

    public boolean hasFinished() {
        LocalDateTime timeOfFinish = appointmentDate.plusMinutes(getCleaningTime());
        return LocalDateTime.now().isAfter(timeOfFinish);
    }

    public boolean isApplicableForPayment() {
        return hasFinished() && wasMarkedAsFinished();
    }

    public Double getCostOfCleaning() {
        return type.calculateCost(vehicle.getType());
    }

    public Integer getCleaningTime() {
        return type.calculateCleaningTime(vehicle.getType());
    }
}
