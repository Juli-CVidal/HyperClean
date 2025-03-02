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
    private String license;
    private CustomerDTO customer;
    private VehicleType type;

    public static VehicleDTO from(Vehicle vehicle) {
        return VehicleDTO.builder()
                .id(vehicle.getId())
                .license(vehicle.getLicense())
                .customer(CustomerDTO.from(vehicle.getCustomer()))
                .type(vehicle.getType())
                .build();
    }

    public Vehicle toModel() {
        return Vehicle.builder()
                .id(this.id)
                .model(this.model)
                .license(this.license)
                .customer(this.customer.toModel())
                .type(this.type)
                .build();
    }
}
