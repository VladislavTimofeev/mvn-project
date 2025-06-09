package com.vlad.dto.trip;

import com.vlad.entity.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripEditDto {
    private Long requestId;
    private Long vehicleId;
    private Long driverId;
    private LocalDate departureTime;
    private LocalDate arrivalTime;
    private TripStatus status;
}
