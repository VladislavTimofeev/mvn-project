package com.vlad.dto.request;

import com.vlad.dto.user.UserReadDto;
import com.vlad.entity.RequestStatus;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
public class RequestReadDto {
    Long id;
    UserReadDto customer;
    RequestStatus status;
    String cargoDetails;
    BigDecimal weight;
    Integer palletCount;
    Boolean refrigerated;
    String pickupAddress;
    String deliveryAddress;
    LocalDate creationDate;
    UserReadDto carrier;
}
