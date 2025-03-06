package com.jcv.hyperclean.exception;

import com.jcv.hyperclean.dto.AppointmentDTO;
import lombok.Getter;

@Getter
public class HCVehicleTimeSlotOccupiedException extends Exception {
    private final AppointmentDTO conflictingAppointment;

    public HCVehicleTimeSlotOccupiedException(AppointmentDTO conflictingAppointment, String message) {
        super(message);
        this.conflictingAppointment = conflictingAppointment;
    }
}
