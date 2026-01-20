package com.vlad.dto.filter;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DriverFilterDto {
    String name;
    Long carrierId;
}
