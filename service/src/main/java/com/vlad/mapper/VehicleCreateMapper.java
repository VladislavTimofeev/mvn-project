package com.vlad.mapper;

import com.vlad.dto.vehicle.VehicleCreateDto;
import com.vlad.entity.User;
import com.vlad.entity.Vehicle;
import com.vlad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VehicleCreateMapper implements Mapper<VehicleCreateDto, Vehicle> {

    private final UserRepository userRepository;

    @Override
    public Vehicle map(VehicleCreateDto object) {
        Vehicle vehicle = new Vehicle();
        vehicle.setCarrier(getCarrier(object.getCarrierId()));
        vehicle.setLicensePlate(object.getLicensePlate());
        vehicle.setCapacity(object.getCapacity());
        vehicle.setPalletCapacity(object.getPalletCapacity());
        vehicle.setRefrigerated(object.getRefrigerated());
        vehicle.setModel(object.getModel());
        return vehicle;
    }

    private User getCarrier(Long id) {
        return Optional.ofNullable(id)
                .flatMap(userRepository::findById)
                .orElse(null);
    }
}
