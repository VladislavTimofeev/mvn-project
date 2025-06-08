package com.vlad.dto.trip;

import com.vlad.entity.TripStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripCreateDto {
    @NotNull
    private Long requestId;
    @NotNull
    private Long vehicleId;
    @NotNull
    private Long driverId;
    private LocalDate departureTime;
    private LocalDate arrivalTime;
    private TripStatus status;
}
