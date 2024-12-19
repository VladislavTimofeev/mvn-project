package com.vlad.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
@AllArgsConstructor
public class VehicleFilterDto {
    String licensePlate;
    BigDecimal capacity;
    Integer palletCapacity;
    Boolean refrigerated;
    String model;
}
