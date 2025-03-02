package com.jcv.hyperclean.enums;

import lombok.Getter;

@Getter
public enum VehicleType {
    SEDAN(50.0, 60),
    SUV(75.0, 90),
    PICKUP(100.0, 120),
    SUPERCAR(150.0, 180);

    // The cost is associated to the interior cleaning service (ServiceType.INTERIOR)
    private final double cost;
    private final int cleaningTime;

    VehicleType(double cost, int cleaningTime) {
        this.cost = cost;
        this.cleaningTime = cleaningTime;
    }
}