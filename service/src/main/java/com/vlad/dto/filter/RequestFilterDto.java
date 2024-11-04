package com.vlad.dto.filter;

import com.vlad.entity.RequestStatus;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class RequestFilterDto {
    RequestStatus status;
    String pickupAddress;
    String deliveryAddress;
    LocalDate creationDate;
}
