package com.jcv.hyperclean.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer extends BasicModel{
    private String name;
    private String email;
    private String phone;
}
