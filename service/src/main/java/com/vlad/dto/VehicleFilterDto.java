package com.vlad.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VehicleFilterDto {
    Integer palletCapacity;
    Boolean refrigerated;
}
