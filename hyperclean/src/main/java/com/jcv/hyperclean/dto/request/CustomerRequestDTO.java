package com.jcv.hyperclean.dto.request;

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
public class CustomerRequestDTO {
    // [a-zA-Z0-9._%+-] name @ [a-zA-Z0-9.-] domain . [a-zA-Z]{2,} topLevelDomain
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    // \d{1,4} area code, \d{8} phone number
    private static final String PHONE_REGEX = "^(\\d{1,4})(\\d{8})$";

    @NotBlank(message = "You must enter the client's name")
    private String name;

    @NotNull(message = "You must enter an email")
    @Pattern(regexp = EMAIL_REGEX, message = "Please enter a valid email. Example: name@domain.com")
    private String email;

    @NotNull(message = "You must enter a phone")
    @Pattern(regexp = PHONE_REGEX, message = "Please enter a valid phone number. Only numbers, include area code before the number. Example: 11xxxxxxxx or 341xxxxxxxx")
    private String phone;
}
