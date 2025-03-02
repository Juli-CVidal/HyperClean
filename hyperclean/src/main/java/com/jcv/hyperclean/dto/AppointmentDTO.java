package com.jcv.hyperclean.dto;

import com.jcv.hyperclean.enums.AppointmentStatus;
import com.jcv.hyperclean.enums.ServiceType;
import com.jcv.hyperclean.model.Appointment;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AppointmentDTO extends BasicDTO {
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private ServiceType type;
    private VehicleDTO vehicle;

    public static AppointmentDTO from(Appointment appointment) {
        return AppointmentDTO.builder()
                .id(appointment.getId())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus())
                .type(appointment.getType())
                .vehicle(VehicleDTO.from(appointment.getVehicle()))
                .build();
    }

    public Appointment toModel() {
        return Appointment.builder()
                .id(this.id)
                .appointmentDate(this.appointmentDate)
                .status(this.status)
                .type(this.type)
                .vehicle(this.vehicle.toModel())
                .build();
    }
}
