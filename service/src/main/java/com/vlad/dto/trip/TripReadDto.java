package com.vlad.dto.trip;

import com.vlad.dto.driver.DriverReadDto;
import com.vlad.dto.request.RequestReadDto;
import com.vlad.dto.vehicle.VehicleReadDto;
import com.vlad.entity.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripReadDto {
    private Long id;
    private RequestReadDto request;
    private VehicleReadDto vehicle;
    private DriverReadDto driver;
    private LocalDate departureTime;
    private LocalDate arrivalTime;
    private TripStatus status;
}
