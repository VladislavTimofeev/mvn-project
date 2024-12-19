package com.vlad.mapper;

import com.vlad.dto.trip.TripReadDto;
import com.vlad.entity.Trip;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TripReadMapper implements Mapper<Trip, TripReadDto> {

    private final RequestReadMapper requestReadMapper;
    private final VehicleReadMapper vehicleReadMapper;
    private final DriverReadMapper driverReadMapper;

    @Override
    public TripReadDto map(Trip object) {
        return new TripReadDto(
                object.getId(),
                requestReadMapper.map(object.getRequest()),
                vehicleReadMapper.map(object.getVehicle()),
                driverReadMapper.map(object.getDriver()),
                object.getDepartureTime(),
                object.getArrivalTime(),
                object.getStatus()
        );
    }
}
