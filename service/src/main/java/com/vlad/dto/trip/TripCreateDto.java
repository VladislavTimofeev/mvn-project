package com.vlad.dto.trip;

import com.vlad.entity.TripStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalDate;

@Value
public class TripCreateDto {
    @NotNull
    Long requestId;
    @NotNull
    Long vehicleId;
    @NotNull
    Long driverId;
    LocalDate departureTime;
    LocalDate arrivalTime;
    TripStatus status;
}
