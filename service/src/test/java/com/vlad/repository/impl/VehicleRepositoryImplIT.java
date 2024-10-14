package com.vlad.repository.impl;

import com.vlad.BaseIT;
import com.vlad.entity.Role;
import com.vlad.entity.User;
import com.vlad.entity.Vehicle;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class VehicleRepositoryImplIT extends BaseIT {

    private VehicleRepositoryImpl vehicleRepository;
    private UserRepositoryImpl userRepository;

    @Test
    void deleteVehicle() {
        userRepository = new UserRepositoryImpl(entityManager);
        vehicleRepository = new VehicleRepositoryImpl(entityManager);
        User user = getUser();
        userRepository.save(user);
        Vehicle vehicle = new Vehicle();
        vehicle.setCarrier(user);
        vehicle.setLicensePlate("AA1234-5");
        vehicle.setCapacity(BigDecimal.valueOf(25.90).setScale(2, RoundingMode.HALF_UP));
        vehicle.setPalletCapacity(25);
        vehicle.setRefrigerated(true);
        vehicle.setModel("ISUZU");
        vehicleRepository.save(vehicle);
        vehicleRepository.delete(vehicle.getId());
        entityManager.clear();

        Optional<Vehicle> actualResult = vehicleRepository.findById(vehicle.getId());

        assertFalse(actualResult.isPresent());
    }

    @Test
    void updateVehicle() {
        userRepository = new UserRepositoryImpl(entityManager);
        vehicleRepository = new VehicleRepositoryImpl(entityManager);
        User user = getUser();
        userRepository.save(user);
        Vehicle vehicle = new Vehicle();
        vehicle.setCarrier(user);
        vehicle.setLicensePlate("AA1234-5");
        vehicle.setCapacity(BigDecimal.valueOf(25.90).setScale(2, RoundingMode.HALF_UP));
        vehicle.setPalletCapacity(25);
        vehicle.setRefrigerated(true);
        vehicle.setModel("ISUZU");
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
        userRepository = new UserRepositoryImpl(entityManager);
        vehicleRepository = new VehicleRepositoryImpl(entityManager);
        User user = getUser();
        userRepository.save(user);
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setCarrier(user);
        vehicle1.setLicensePlate("AA1234-5");
        vehicle1.setCapacity(BigDecimal.valueOf(25.90).setScale(2, RoundingMode.HALF_UP));
        vehicle1.setPalletCapacity(25);
        vehicle1.setRefrigerated(true);
        vehicle1.setModel("ISUZU");
        vehicleRepository.save(vehicle1);
        Vehicle vehicle2 = new Vehicle();
        vehicle2.setCarrier(user);
        vehicle2.setLicensePlate("AA1234-5");
        vehicle2.setCapacity(BigDecimal.valueOf(25.90).setScale(2, RoundingMode.HALF_UP));
        vehicle2.setPalletCapacity(25);
        vehicle2.setRefrigerated(true);
        vehicle2.setModel("ISUZU");
        vehicleRepository.save(vehicle2);
        entityManager.flush();
        entityManager.clear();

        List<Vehicle> actualResult = vehicleRepository.findAll();

        assertEquals(2, actualResult.size());
        assertThat(actualResult).containsExactlyInAnyOrder(vehicle1, vehicle2);
    }

    @Test
    void saveVehicle() {
        userRepository = new UserRepositoryImpl(entityManager);
        vehicleRepository = new VehicleRepositoryImpl(entityManager);
        User user = getUser();
        userRepository.save(user);
        Vehicle vehicle = new Vehicle();
        vehicle.setCarrier(user);
        vehicle.setLicensePlate("AA1234-5");
        vehicle.setCapacity(BigDecimal.valueOf(25.90).setScale(2, RoundingMode.HALF_UP));
        vehicle.setPalletCapacity(25);
        vehicle.setRefrigerated(true);
        vehicle.setModel("ISUZU");
        vehicleRepository.save(vehicle);
        entityManager.flush();
        entityManager.clear();

        Optional<Vehicle> actualResult = vehicleRepository.findById(vehicle.getId());

        assertTrue(actualResult.isPresent());
        assertEquals(vehicle, actualResult.get());
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
