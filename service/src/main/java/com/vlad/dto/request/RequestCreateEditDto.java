package com.vlad.dto.request;

import com.vlad.entity.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
public class RequestCreateEditDto {
    @NotNull
    Long customerId;
    RequestStatus status;
    String cargoDetails;
    BigDecimal weight;
    Integer palletCount;
    Boolean refrigerated;
    String pickupAddress;
    String deliveryAddress;
    LocalDate creationDate;
    @NotNull
    Long carrierId;
}
