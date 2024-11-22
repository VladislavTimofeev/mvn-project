package com.vlad.mapper;

import com.vlad.dto.trip.TripEditDto;
import com.vlad.entity.Driver;
import com.vlad.entity.Request;
import com.vlad.entity.Trip;
import com.vlad.entity.Vehicle;
import com.vlad.repository.DriverRepository;
import com.vlad.repository.RequestRepository;
import com.vlad.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TripEditMapper implements Mapper<TripEditDto, Trip> {

    private final RequestRepository requestRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    @Override
    public Trip map(TripEditDto object) {
        Trip trip = new Trip();
        copy(object, trip);
        return trip;
    }

    @Override
    public Trip map(TripEditDto fromObject, Trip toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(TripEditDto object, Trip trip) {
        trip.setRequest(getRequest(object.getRequestId()));
        trip.setVehicle(getVehicle(object.getVehicleId()));
        trip.setDriver(getDriver(object.getDriverId()));
        trip.setDepartureTime(object.getDepartureTime());
        trip.setArrivalTime(object.getArrivalTime());
        trip.setStatus(object.getStatus());
    }

    private Request getRequest(Long id) {
        return Optional.ofNullable(id)
                .flatMap(requestRepository::findById)
                .orElseThrow(() -> new IllegalArgumentException("Request with id " + id + " not found"));
    }

    private Vehicle getVehicle(Long id) {
        return Optional.ofNullable(id)
                .flatMap(vehicleRepository::findById)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle with id " + id + " not found"));
    }

    private Driver getDriver(Long id) {
        return Optional.ofNullable(id)
                .flatMap(driverRepository::findById)
                .orElseThrow(() -> new IllegalArgumentException("Driver with id " + id + " not found"));
    }
}
