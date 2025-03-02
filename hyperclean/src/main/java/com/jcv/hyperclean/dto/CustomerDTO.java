package com.jcv.hyperclean.dto;

import com.jcv.hyperclean.model.Customer;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerDTO extends BasicDTO {
    private String name;
    private String email;
    private String phone;

    public static CustomerDTO from(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .build();
    }

    public Customer toModel() {
        return Customer.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .phone(this.phone)
                .build();
    }
}
