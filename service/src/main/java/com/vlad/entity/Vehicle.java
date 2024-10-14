package com.vlad.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@NamedEntityGraph(
        name = "VehicleWithCarrier",
        attributeNodes = {
                @NamedAttributeNode("carrier")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"licensePlate", "capacity", "palletCapacity", "refrigerated", "model"})
@ToString(of = {"licensePlate", "capacity", "palletCapacity", "refrigerated", "model"})
@Builder
@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrier_id")
    private User carrier;

    private String licensePlate;

    @Column(precision = 10, scale = 2)
    private BigDecimal capacity;
    private Integer palletCapacity;
    private Boolean refrigerated;
    private String model;
}
