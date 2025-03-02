package com.jcv.hyperclean.dto;


import com.jcv.hyperclean.enums.VehicleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleDTO extends BasicDTO {
    private String model;
    private String licensePlate;
    private CustomerDTO customer;
    private VehicleType type;
}
