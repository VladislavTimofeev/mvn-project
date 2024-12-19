package com.vlad.dto.vehicle;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class VehicleCreateDto {
    @NotNull
    Long carrierId;
    String licensePlate;
    BigDecimal capacity;
    Integer palletCapacity;
    Boolean refrigerated;
    String model;
}
