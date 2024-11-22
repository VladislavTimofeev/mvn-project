package com.vlad.mapper;

import com.vlad.dto.driver.DriverCreateDto;
import com.vlad.entity.Driver;
import com.vlad.entity.User;
import com.vlad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DriverCreateMapper implements Mapper<DriverCreateDto, Driver> {

    private final UserRepository userRepository;

    @Override
    public Driver map(DriverCreateDto object) {
        Driver driver = new Driver();
        driver.setCarrier(getCarrier(object.getCarrierId()));
        driver.setName(object.getName());
        driver.setLicenseNumber(object.getLicenseNumber());
        driver.setPhoneNumber(object.getPhoneNumber());
        return driver;
    }

    private User getCarrier(Long id) {
        return Optional.ofNullable(id)
                .flatMap(userRepository::findById)
                .orElse(null);
    }
}
