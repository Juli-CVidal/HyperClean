package com.jcv.hyperclean.exception;

public class HCVehicleTimeSlotOccupiedException extends Exception{

    public HCVehicleTimeSlotOccupiedException() {
        super("The vehicle will be occupied on the specified time slot, please select a different time");
    }
}
