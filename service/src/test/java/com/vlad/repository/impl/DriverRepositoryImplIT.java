package com.vlad.repository.impl;

import com.vlad.BaseIT;
import com.vlad.entity.Driver;
import com.vlad.entity.Role;
import com.vlad.entity.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DriverRepositoryImplIT extends BaseIT {

    private DriverRepositoryImpl driverRepositoryImpl;
    private UserRepositoryImpl userRepository;

    @Test
    void deleteDriver() {
        userRepository = new UserRepositoryImpl(entityManager);
        driverRepositoryImpl = new DriverRepositoryImpl(entityManager);
        User user = getUser();
        userRepository.save(user);
        Driver driver = new Driver();
        driver.setCarrier(user);
        driver.setName("Volodimir");
        driver.setLicenseNumber("AB1234-7");
        driver.setPhoneNumber("2456700");
        driverRepositoryImpl.save(driver);
        driverRepositoryImpl.delete(driver.getId());
        entityManager.clear();

        Optional<Driver> actualResult = driverRepositoryImpl.findById(driver.getId());

        assertFalse(actualResult.isPresent());
    }

    @Test
    void updateDriver() {
        userRepository = new UserRepositoryImpl(entityManager);
        driverRepositoryImpl = new DriverRepositoryImpl(entityManager);
        User user = getUser();
        userRepository.save(user);
        Driver driver = new Driver();
        driver.setCarrier(user);
        driver.setName("Volodimir");
        driver.setLicenseNumber("AB1234-7");
        driver.setPhoneNumber("2456700");
        driverRepositoryImpl.save(driver);
        driver.setName("PUMBA");
        driverRepositoryImpl.update(driver);
        entityManager.flush();
        entityManager.clear();

        Optional<Driver> actualResult = driverRepositoryImpl.findById(driver.getId());

        assertTrue(actualResult.isPresent());
        assertEquals("PUMBA", actualResult.get().getName());
    }

    @Test
    void getAllDrivers() {
        userRepository = new UserRepositoryImpl(entityManager);
        driverRepositoryImpl = new DriverRepositoryImpl(entityManager);
        User user = getUser();
        userRepository.save(user);
        Driver driver1 = new Driver();
        driver1.setCarrier(user);
        driver1.setName("Volodimir");
        driver1.setLicenseNumber("AB1234-7");
        driver1.setPhoneNumber("2456700");
        driverRepositoryImpl.save(driver1);
        Driver driver2 = new Driver();
        driver2.setCarrier(user);
        driver2.setName("Alexander");
        driver2.setLicenseNumber("TT-5645-5");
        driver2.setPhoneNumber("9807865");
        driverRepositoryImpl.save(driver2);
        entityManager.flush();
        entityManager.clear();

        List<Driver> actualResult = driverRepositoryImpl.findAll();

        assertEquals(2, actualResult.size());
        assertThat(actualResult).containsExactlyInAnyOrder(driver1, driver2);
    }

    @Test
    void saveDriver() {
        userRepository = new UserRepositoryImpl(entityManager);
        driverRepositoryImpl = new DriverRepositoryImpl(entityManager);
        User user = getUser();
        userRepository.save(user);
        Driver driver = new Driver();
        driver.setCarrier(user);
        driver.setName("Volodimir");
        driver.setLicenseNumber("AB1234-7");
        driver.setPhoneNumber("2456700");
        driverRepositoryImpl.save(driver);
        entityManager.flush();
        entityManager.clear();

        Optional<Driver> actualResult = driverRepositoryImpl.findById(driver.getId());

        assertTrue(actualResult.isPresent());
        assertEquals(driver, actualResult.get());
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
