package com.vlad.dto.vehicle;

import com.vlad.dto.user.UserReadDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleReadDto {
    private Long id;
    private UserReadDto carrier;
    private String licensePlate;
    private BigDecimal capacity;
    private Integer palletCapacity;
    private Boolean refrigerated;
    private String model;
}
