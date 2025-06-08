package com.vlad.dto.request;

import com.vlad.dto.user.UserReadDto;
import com.vlad.entity.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestReadDto {
    private Long id;
    private UserReadDto customer;
    private RequestStatus status;
    private String cargoDetails;
    private BigDecimal weight;
    private Integer palletCount;
    private Boolean refrigerated;
    private String pickupAddress;
    private String deliveryAddress;
    private LocalDate creationDate;
    private UserReadDto carrier;
}
