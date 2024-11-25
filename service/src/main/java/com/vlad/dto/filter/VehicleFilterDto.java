package com.vlad.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class VehicleFilterDto {
//    Long requestId;
//    Long vehicleId;
//    Long driverId;
    Integer palletCapacity;
    Boolean refrigerated;
    String model;
}
