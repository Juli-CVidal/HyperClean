package com.jcv.hyperclean.enums;

import lombok.Getter;

@Getter
public enum ServiceType {
    EXTERIOR(0.75),
    INTERIOR(1.0),
    COMPLETE(1.5);

    private final double multiplier;

    ServiceType(double multiplier) {
        this.multiplier = multiplier;
    }

    public double calculateCost(VehicleType vehicleType) {
        return vehicleType.getCost() * this.multiplier;
    }

    // In minutes
    public int calculateCleaningTime(VehicleType vehicleType) {
        return (int) (vehicleType.getCleaningTime() * this.multiplier);
    }
}