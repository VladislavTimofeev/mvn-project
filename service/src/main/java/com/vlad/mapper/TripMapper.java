package com.vlad.mapper;

import com.vlad.dto.trip.TripCreateDto;
import com.vlad.dto.trip.TripEditDto;
import com.vlad.dto.trip.TripReadDto;
import com.vlad.entity.Driver;
import com.vlad.entity.Request;
import com.vlad.entity.Trip;
import com.vlad.entity.Vehicle;
import com.vlad.repository.DriverRepository;
import com.vlad.repository.RequestRepository;
import com.vlad.repository.VehicleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {RequestMapper.class, VehicleMapper.class, DriverMapper.class})
public interface TripMapper {

    TripReadDto toDto(Trip trip);

    @Mapping(target = "request", source = "requestId", qualifiedByName = "mapRequestIdToRequest")
    @Mapping(target = "vehicle", source = "vehicleId", qualifiedByName = "mapVehicleIdToVehicle")
    @Mapping(target = "driver", source = "driverId", qualifiedByName = "mapDriverIdToDriver")
    Trip toEntity(TripCreateDto dto, RequestRepository requestRepository, VehicleRepository vehicleRepository, DriverRepository driverRepository);

    @Mapping(target = "request", source = "requestId", qualifiedByName = "mapRequestIdToRequest")
    @Mapping(target = "vehicle", source = "vehicleId", qualifiedByName = "mapVehicleIdToVehicle")
    @Mapping(target = "driver", source = "driverId", qualifiedByName = "mapDriverIdToDriver")
    void updateEntityFromDto(TripEditDto dto, @MappingTarget Trip trip, RequestRepository requestRepository, VehicleRepository vehicleRepository, DriverRepository driverRepository);

    @Named("mapRequestIdToRequest")
    default Request mapRequestIdToRequest(Long id, RequestRepository requestRepository) {
        return id == null ? null : requestRepository.findById(id).orElse(null);
    }

    @Named("mapVehicleIdToVehicle")
    default Vehicle mapVehicleIdToVehicle(Long id, VehicleRepository vehicleRepository) {
        return id == null ? null : vehicleRepository.findById(id).orElse(null);
    }

    @Named("mapDriverIdToDriver")
    default Driver mapDriverIdToDriver(Long id, DriverRepository driverRepository) {
        return id == null ? null : driverRepository.findById(id).orElse(null);
    }
}
