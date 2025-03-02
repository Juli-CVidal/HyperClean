package com.jcv.hyperclean.dto.request;

import com.jcv.hyperclean.enums.AppointmentStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentRequestDTO extends BasicRequestDTO {
    @FutureOrPresent(message = "You can't make an appointment  using previous dates")
    private LocalDateTime appointmentDate;

    private AppointmentStatus status;

    @NotNull(message = "You must assign the appointment to a vehicle")
    private Long vehicleId;
}
