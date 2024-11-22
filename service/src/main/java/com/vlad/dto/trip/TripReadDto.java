package com.vlad.dto.trip;

import com.vlad.dto.driver.DriverReadDto;
import com.vlad.dto.request.RequestReadDto;
import com.vlad.dto.vehicle.VehicleReadDto;
import com.vlad.entity.TripStatus;
import lombok.Value;

import java.time.LocalDate;

@Value
public class TripReadDto {
    RequestReadDto request;
    VehicleReadDto vehicle;
    DriverReadDto driver;
    LocalDate departureTime;
    LocalDate arrivalTime;
    TripStatus status;
}
