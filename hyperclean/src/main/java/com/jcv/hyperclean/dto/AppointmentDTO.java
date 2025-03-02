package com.jcv.hyperclean.dto;

import com.jcv.hyperclean.enums.AppointmentStatus;
import com.jcv.hyperclean.enums.ServiceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentDTO extends BasicDTO {
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private ServiceType type;
    private VehicleDTO vehicle;
}
