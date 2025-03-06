package com.jcv.hyperclean.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jcv.hyperclean.dto.request.VehicleRequestDTO;
import com.jcv.hyperclean.enums.VehicleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "vehicle")
public class Vehicle extends BasicModel {
    private String model;

    @Column(unique = true)
    private String licensePlate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    public static Vehicle of(VehicleRequestDTO requestDTO) {
        return Vehicle.builder()
                .model(requestDTO.getModel())
                .licensePlate(requestDTO.getLicensePlate())
                .type(requestDTO.getType())
                .build();
    }
}
