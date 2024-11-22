package com.vlad.dto.filter;

import com.vlad.entity.TripStatus;
import lombok.Value;

@Value
public class TripFilterDto {
    TripStatus status;
}
