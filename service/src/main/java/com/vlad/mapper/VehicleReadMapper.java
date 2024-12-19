package com.vlad.mapper;

import com.vlad.dto.vehicle.VehicleReadDto;
import com.vlad.entity.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleReadMapper implements Mapper<Vehicle, VehicleReadDto> {

    private final UserReadMapper userReadMapper;

    @Override
    public VehicleReadDto map(Vehicle object) {
        return new VehicleReadDto(
                object.getId(),
                userReadMapper.map(object.getCarrier()),
                object.getLicensePlate(),
                object.getCapacity(),
                object.getPalletCapacity(),
                object.getRefrigerated(),
                object.getModel()
        );
    }
}
