package com.jcv.hyperclean.dto.request;

import com.jcv.hyperclean.enums.AppointmentStatus;
import com.jcv.hyperclean.enums.ServiceType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {
    @NotNull(message = "You must enter the appointment date")
    @Future(message = "You can't make an appointment using previous dates")
    private LocalDateTime appointmentDate;

    private AppointmentStatus status = AppointmentStatus.PENDING;

    @NotNull(message = "You must enter the type of service (EXTERIOR, INTERIOR, COMPLETE)")
    private ServiceType type;

    @NotNull(message = "You must assign the appointment to a vehicle")
    @Min(value = 1L, message = "Please enter a valid vehicle id")
    private Long vehicleId;
}
