package com.jcv.hyperclean.model;


import com.jcv.hyperclean.dto.request.CustomerRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "customer")
public class Customer extends BasicModel {
    private String name;
    private String email;
    private String phone;

    public static Customer of(@Valid CustomerRequestDTO customerDTO) {
        return Customer.builder()
                .name(customerDTO.getName())
                .email(customerDTO.getEmail())
                .phone(customerDTO.getPhone())
                .build();
    }
}
