package com.vlad.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User customer;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private String cargoDetails;
    private BigDecimal weight;
    private Integer palletCount;
    private Boolean refrigerated;
    private String pickupAddress;
    private String deliveryAddress;
    private LocalDate creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrier_id")
    private User carrier;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(cargoDetails, request.cargoDetails) && Objects.equals(palletCount, request.palletCount) && Objects.equals(refrigerated, request.refrigerated) && Objects.equals(pickupAddress, request.pickupAddress) && Objects.equals(deliveryAddress, request.deliveryAddress) && Objects.equals(creationDate, request.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cargoDetails, palletCount, refrigerated, pickupAddress, deliveryAddress, creationDate);
    }

    @Override
    public String toString() {
        return "Request{" +
               "cargoDetails='" + cargoDetails + '\'' +
               ", palletCount=" + palletCount +
               ", refrigerated=" + refrigerated +
               ", pickupAddress='" + pickupAddress + '\'' +
               ", deliveryAddress='" + deliveryAddress + '\'' +
               ", creationDate=" + creationDate +
               '}';
    }
}
