package com.vlad.dto.filter;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VehicleFilterDto {
    Integer palletCapacity;
    Boolean refrigerated;
    String model;
}
