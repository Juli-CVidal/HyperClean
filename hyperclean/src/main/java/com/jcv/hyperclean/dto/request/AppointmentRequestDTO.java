package com.jcv.hyperclean.dto.request;

import com.jcv.hyperclean.enums.AppointmentStatus;
import com.jcv.hyperclean.enums.DateValidationType;
import com.jcv.hyperclean.enums.ServiceType;
import com.jcv.hyperclean.validator.DateValidation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.jcv.hyperclean.util.DateUtils.localDateTimeToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {
    @NotNull(message = "You must enter the appointment date")
    @DateValidation(type = DateValidationType.FUTURE, message = "You need to enter a future date for the appointment")
    private String appointmentDate;

    private AppointmentStatus status = AppointmentStatus.PENDING;

    @NotNull(message = "You must enter the type of service (EXTERIOR, INTERIOR, COMPLETE)")
    private ServiceType type;

    @NotNull(message = "You must assign the appointment to a vehicle")
    @Min(value = 1L, message = "Please enter a valid vehicle id")
    private Long vehicleId;

    public AppointmentRequestDTO(LocalDateTime appointmentDate, AppointmentStatus status, ServiceType type, Long vehicleId) {
        this.appointmentDate = localDateTimeToString(appointmentDate);
        this.status = status;
        this.type = type;
        this.vehicleId = vehicleId;
    }
}
