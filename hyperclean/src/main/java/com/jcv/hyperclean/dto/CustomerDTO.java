package com.jcv.hyperclean.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerDTO extends BasicDTO {
    private String name;
    private String email;
    private String phone;
}
