package com.vlad.dto.request;

import com.vlad.entity.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateEditDto {
    @NotNull
    private Long customerId;
    private RequestStatus status;
    private String cargoDetails;
    private BigDecimal weight;
    private Integer palletCount;
    private Boolean refrigerated;
    private String pickupAddress;
    private String deliveryAddress;
    private LocalDate creationDate;
    @NotNull
    private Long carrierId;
}
