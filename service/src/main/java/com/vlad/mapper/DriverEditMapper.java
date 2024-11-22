package com.vlad.mapper;

import com.vlad.dto.driver.DriverEditDto;
import com.vlad.entity.Driver;
import com.vlad.entity.User;
import com.vlad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DriverEditMapper implements Mapper<DriverEditDto, Driver> {

    private final UserRepository userRepository;

    @Override
    public Driver map(DriverEditDto object) {
        Driver driver = new Driver();
        copy(object, driver);
        return driver;
    }

    @Override
    public Driver map(DriverEditDto fromObject, Driver toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(DriverEditDto object, Driver driver) {
        driver.setCarrier(getCarrier(object.getCarrierId()));
        driver.setName(object.getName());
        driver.setLicenseNumber(object.getLicenseNumber());
        driver.setPhoneNumber(object.getPhoneNumber());
    }

    private User getCarrier(Long id) {
        return Optional.ofNullable(id)
                .flatMap(userRepository::findById)
                .orElse(null);
    }
}
