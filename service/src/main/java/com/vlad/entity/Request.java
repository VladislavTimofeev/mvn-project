package com.vlad.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customerId;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private String cargoDetails;
    private BigDecimal weight;
    private Integer palletCount;
    private Boolean refrigerated;
    private String pickupAddress;
    private String deliveryAddress;
    private LocalDate creationDate;

    @ManyToOne
    @JoinColumn(name = "carrier_id")
    private User carrierId;
}
