package com.vlad.dto.vehicle;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleCreateDto {
    @NotNull
    private Long carrierId;
    private String licensePlate;
    private BigDecimal capacity;
    private Integer palletCapacity;
    private Boolean refrigerated;
    private String model;
}
