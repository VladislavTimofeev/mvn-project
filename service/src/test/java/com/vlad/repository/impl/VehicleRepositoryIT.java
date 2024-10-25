package com.vlad.repository.impl;

import com.vlad.BaseIT;
import com.vlad.entity.Role;
import com.vlad.entity.User;
import com.vlad.entity.Vehicle;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class VehicleRepositoryIT extends BaseIT {

    private VehicleRepository vehicleRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository(entityManager);
        vehicleRepository = new VehicleRepository(entityManager);
    }

    @Test
    void deleteVehicle() {
        User user = getUser();
        userRepository.save(user);
        Vehicle vehicle = getVehicle(user);
        vehicleRepository.save(vehicle);

        vehicleRepository.delete(vehicle);

        entityManager.clear();
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

        entityManager.flush();
        entityManager.clear();
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
        entityManager.flush();
        entityManager.clear();

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

        entityManager.flush();
        entityManager.clear();
        Optional<Vehicle> actualResult = vehicleRepository.findById(vehicle.getId());
        assertTrue(actualResult.isPresent());
        assertEquals(vehicle, actualResult.get());
    }

    private static @NotNull Vehicle getVehicle(User user) {
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
