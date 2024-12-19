package com.vlad.dto.vehicle;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class VehicleEditDto {
    Long carrierId;
    String licensePlate;
    BigDecimal capacity;
    Integer palletCapacity;
    Boolean refrigerated;
    String model;
}
