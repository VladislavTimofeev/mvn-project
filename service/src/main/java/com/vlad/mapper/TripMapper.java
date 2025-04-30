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
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {RequestMapper.class, VehicleMapper.class, DriverMapper.class})
@RequiredArgsConstructor
public abstract class TripMapper {

    private final RequestRepository requestRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    public abstract TripReadDto toDto(Trip trip);

    @Mapping(target = "request", source = "requestId", qualifiedByName = "mapRequestIdToRequest")
    @Mapping(target = "vehicle", source = "vehicleId", qualifiedByName = "mapVehicleIdToVehicle")
    @Mapping(target = "driver", source = "driverId", qualifiedByName = "mapDriverIdToDriver")
    public abstract Trip toEntity(TripCreateDto dto);

    @Mapping(target = "request", source = "requestId", qualifiedByName = "mapRequestIdToRequest")
    @Mapping(target = "vehicle", source = "vehicleId", qualifiedByName = "mapVehicleIdToVehicle")
    @Mapping(target = "driver", source = "driverId", qualifiedByName = "mapDriverIdToDriver")
    public abstract void updateEntityFromDto(TripEditDto dto, @MappingTarget Trip trip);

    @Named("mapRequestIdToRequest")
    protected Request mapRequestIdToRequest(Long id) {
        return id == null ? null : requestRepository.findById(id).orElse(null);
    }

    @Named("mapVehicleIdToVehicle")
    protected Vehicle mapVehicleIdToVehicle(Long id) {
        return id == null ? null : vehicleRepository.findById(id).orElse(null);
    }

    @Named("mapDriverIdToDriver")
    protected Driver mapDriverIdToDriver(Long id) {
        return id == null ? null : driverRepository.findById(id).orElse(null);
    }
}
