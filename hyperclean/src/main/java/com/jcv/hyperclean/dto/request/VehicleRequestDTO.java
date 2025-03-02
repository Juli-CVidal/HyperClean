package com.jcv.hyperclean.dto.request;


import com.jcv.hyperclean.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehicleRequestDTO extends BasicRequestDTO {
    @NotBlank(message = "Please enter the model of the vehicle")
    private String model;

    @NotBlank(message = "You must enter the license plate")
    private String licensePlate;

    @NotNull(message = "Please enter the id of the customer")
    private Long customerId;

    @NotBlank(message = "Please enter the type of the vehicle")
    private VehicleType type;
}
