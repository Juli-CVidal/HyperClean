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
    private CustomerDTO customer;
    private VehicleType type;

    public static VehicleDTO from(Vehicle vehicle) {
        return VehicleDTO.builder()
                .id(vehicle.getId())
                .licensePlate(vehicle.getLicensePlate())
                .customer(CustomerDTO.from(vehicle.getCustomer()))
                .type(vehicle.getType())
                .build();
    }

    public Vehicle toModel() {
        return Vehicle.builder()
                .id(this.id)
                .model(this.model)
                .licensePlate(this.licensePlate)
                .customer(this.customer.toModel())
                .type(this.type)
                .build();
    }
}
