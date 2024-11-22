package com.vlad.mapper;

import com.vlad.dto.vehicle.VehicleEditDto;
import com.vlad.entity.User;
import com.vlad.entity.Vehicle;
import com.vlad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VehicleEditMapper implements Mapper<VehicleEditDto, Vehicle> {

    private final UserRepository userRepository;

    @Override
    public Vehicle map(VehicleEditDto object) {
        Vehicle vehicle = new Vehicle();
        copy(object, vehicle);
        return vehicle;
    }

    @Override
    public Vehicle map(VehicleEditDto fromObject, Vehicle toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(VehicleEditDto object, Vehicle vehicle) {
        vehicle.setCarrier(getCarrier(object.getCarrierId()));
        vehicle.setLicensePlate(object.getLicensePlate());
        vehicle.setCapacity(object.getCapacity());
        vehicle.setPalletCapacity(object.getPalletCapacity());
        vehicle.setRefrigerated(object.getRefrigerated());
        vehicle.setModel(object.getModel());
    }

    private User getCarrier(Long id) {
        return Optional.ofNullable(id)
                .flatMap(userRepository::findById)
                .orElse(null);
    }
}
