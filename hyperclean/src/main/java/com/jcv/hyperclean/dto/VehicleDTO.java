package com.jcv.hyperclean.dto;


import com.jcv.hyperclean.enums.VehicleType;
import com.jcv.hyperclean.model.Vehicle;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VehicleDTO extends BasicDTO {
    private String model;
    private String licensePlate;
    private Long customerId;
    private VehicleType type;

    public static VehicleDTO from(Vehicle vehicle) {
        return VehicleDTO.builder()
                .id(vehicle.getId())
                .licensePlate(vehicle.getLicensePlate())
                .customerId(vehicle.getCustomer().getId())
                .type(vehicle.getType())
                .build();
    }
}
