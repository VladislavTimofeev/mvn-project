package com.vlad.repository.impl;

import com.vlad.annotation.IT;
import com.vlad.dto.filter.VehicleFilterDto;
import com.vlad.entity.Role;
import com.vlad.entity.User;
import com.vlad.entity.Vehicle;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IT
@RequiredArgsConstructor
class VehicleRepositoryIT {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    @Test
    void getVehicleByFilter() {
        User carrier = getUser();
        userRepository.save(carrier);
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setCarrier(carrier);
        vehicle1.setLicensePlate("AA1234-5");
        vehicle1.setCapacity(BigDecimal.valueOf(33.55).setScale(2, RoundingMode.HALF_UP));
        vehicle1.setPalletCapacity(25);
        vehicle1.setRefrigerated(true);
        vehicle1.setModel("ISUZU");
        vehicleRepository.save(vehicle1);
        Vehicle vehicle2 = new Vehicle();
        vehicle2.setCarrier(carrier);
        vehicle2.setLicensePlate("BB1234-6");
        vehicle2.setCapacity(BigDecimal.valueOf(25.90).setScale(2, RoundingMode.HALF_UP));
        vehicle2.setPalletCapacity(30);
        vehicle2.setRefrigerated(false);
        vehicle2.setModel("TOYOTA");
        vehicleRepository.save(vehicle2);
        VehicleFilterDto filter = VehicleFilterDto.builder()
                .palletCapacity(25)
                .refrigerated(true)
                .model("ISUZU")
                .build();

        List<Vehicle> actualResult = vehicleRepository.getVehicleByFilter(filter);

        assertEquals(vehicle1, actualResult.get(0));
    }

    @Test
    void deleteVehicle() {
        User user = getUser();
        userRepository.save(user);
        Vehicle vehicle = getVehicle(user);
        vehicleRepository.save(vehicle);

        vehicleRepository.delete(vehicle);

        Optional<Vehicle> actualResult = vehicleRepository.findById(vehicle.getId());
        assertFalse(actualResult.isPresent());
    }

    @Test
    void updateVehicle() {
        User user = getUser();
        userRepository.save(user);
        Vehicle vehicle = getVehicle(user);
        vehicleRepository.save(vehicle);
        vehicle.setModel("FORD");

        vehicleRepository.update(vehicle);

        Optional<Vehicle> actualResult = vehicleRepository.findById(vehicle.getId());
        assertTrue(actualResult.isPresent());
        assertEquals("FORD", actualResult.get().getModel());
    }

    @Test
    void getAllVehicles() {
        User user = getUser();
        userRepository.save(user);
        Vehicle vehicle1 = getVehicle(user);
        vehicleRepository.save(vehicle1);
        Vehicle vehicle2 = getVehicle(user);
        vehicleRepository.save(vehicle2);

        List<Vehicle> actualResult = vehicleRepository.findAll();

        assertEquals(2, actualResult.size());
        assertThat(actualResult).containsExactlyInAnyOrder(vehicle1, vehicle2);
    }

    @Test
    void saveVehicle() {
        User user = getUser();
        userRepository.save(user);
        Vehicle vehicle = getVehicle(user);

        vehicleRepository.save(vehicle);

        Optional<Vehicle> actualResult = vehicleRepository.findById(vehicle.getId());
        assertTrue(actualResult.isPresent());
        assertEquals(vehicle, actualResult.get());
    }

    private static Vehicle getVehicle(User user) {
        Vehicle vehicle = new Vehicle();
        vehicle.setCarrier(user);
        vehicle.setLicensePlate("AA1234-5");
        vehicle.setCapacity(BigDecimal.valueOf(25.90).setScale(2, RoundingMode.HALF_UP));
        vehicle.setPalletCapacity(25);
        vehicle.setRefrigerated(true);
        vehicle.setModel("ISUZU");
        return vehicle;
    }

    private static User getUser() {
        User user = new User();
        user.setUsername("MickyMouse");
        user.setPassword("123MickyMouse123");
        user.setRole(Role.CARRIER);
        user.setName("Martin");
        user.setContactInfo("martin@gmail.com");
        user.setAddress("Mazurova 4-56");
        return user;
    }

}
