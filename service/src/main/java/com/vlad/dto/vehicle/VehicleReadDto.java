package com.vlad.dto.vehicle;

import com.vlad.dto.user.UserReadDto;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class VehicleReadDto {
    Long id;
    UserReadDto carrier;
    String licensePlate;
    BigDecimal capacity;
    Integer palletCapacity;
    Boolean refrigerated;
    String model;
}
