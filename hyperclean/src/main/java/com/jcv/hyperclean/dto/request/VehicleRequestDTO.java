package com.jcv.hyperclean.dto.request;


import com.jcv.hyperclean.enums.VehicleType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequestDTO {
    private static final String OLD_PLATE_FORMAT = "^[A-Z]{3}\\d{3}$"; // AAA000
    private static final String NEW_PLATE_FORMAT = "^[A-Z]{2}\\d{3}[A-Z]{2}$"; //AA00AA
    private static final String COMBINED_FORMAT = OLD_PLATE_FORMAT + "|" + NEW_PLATE_FORMAT; // one or the other

    @NotBlank(message = "Please enter the model of the vehicle")
    private String model;

    @NotNull(message = "You must enter the license plate")
    @Pattern(regexp = COMBINED_FORMAT, message = "Please enter a valid license plate. Must be either old format (XXX000) or new format (XX00XX)")
    private String licensePlate;

    @Min(value = 1, message = "Please enter a valid customer id")
    @NotNull(message = "You must enter the customer id")
    private Long customerId;

    @NotNull(message = "You must enter the type of the vehicle (SEDAN, SUV, PICKUP, SUPERCAR)")
    private VehicleType type;
}
