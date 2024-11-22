package com.vlad.dto.trip;

import com.vlad.entity.TripStatus;
import lombok.Value;

import java.time.LocalDate;

@Value
public class TripCreateDto {
    Long requestId;
    Long vehicleId;
    Long driverId;
    LocalDate departureTime;
    LocalDate arrivalTime;
    TripStatus status;
}
