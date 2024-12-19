package com.vlad.mapper;

import com.vlad.dto.driver.DriverReadDto;
import com.vlad.entity.Driver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverReadMapper implements Mapper<Driver, DriverReadDto> {

    private final UserReadMapper userReadMapper;

    @Override
    public DriverReadDto map(Driver object) {
        return new DriverReadDto(
                object.getId(),
                userReadMapper.map(object.getCarrier()),
                object.getName(),
                object.getLicenseNumber(),
                object.getPhoneNumber()
        );
    }
}
