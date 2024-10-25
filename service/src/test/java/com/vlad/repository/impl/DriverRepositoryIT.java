package com.vlad.repository.impl;

import com.vlad.BaseIT;
import com.vlad.entity.Driver;
import com.vlad.entity.Role;
import com.vlad.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DriverRepositoryIT extends BaseIT {

    private DriverRepository driverRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = context.getBean(UserRepository.class);
        driverRepository = context.getBean(DriverRepository.class);
    }

    @Test
    void deleteDriver() {
        User user = getUser();
        userRepository.save(user);
        Driver driver = getDriver(user, "Volodimir", "AB1234-7", "2456700");
        driverRepository.save(driver);

        driverRepository.delete(driver);

        entityManager.clear();
        Optional<Driver> actualResult = driverRepository.findById(driver.getId());
        assertFalse(actualResult.isPresent());
    }

    @Test
    void updateDriver() {
        User user = getUser();
        userRepository.save(user);
        Driver driver = getDriver(user, "Volodimir", "AB1234-7", "2456700");
        driverRepository.save(driver);
        driver.setName("PUMBA");

        driverRepository.update(driver);

        entityManager.flush();
        entityManager.clear();
        Optional<Driver> actualResult = driverRepository.findById(driver.getId());
        assertTrue(actualResult.isPresent());
        assertEquals("PUMBA", actualResult.get().getName());
    }

    @Test
    void getAllDrivers() {
        User user = getUser();
        userRepository.save(user);
        Driver driver1 = getDriver(user, "Volodimir", "AB1234-7", "2456700");
        driverRepository.save(driver1);
        Driver driver2 = getDriver(user, "Alexander", "TT-5645-5", "9807865");
        driverRepository.save(driver2);
        entityManager.flush();
        entityManager.clear();

        List<Driver> actualResult = driverRepository.findAll();

        assertEquals(2, actualResult.size());
        assertThat(actualResult).containsExactlyInAnyOrder(driver1, driver2);
    }

    @Test
    void saveDriver() {
        User user = getUser();
        userRepository.save(user);
        Driver driver = getDriver(user, "Volodimir", "AB1234-7", "2456700");

        driverRepository.save(driver);

        entityManager.flush();
        entityManager.clear();
        Optional<Driver> actualResult = driverRepository.findById(driver.getId());
        assertTrue(actualResult.isPresent());
        assertEquals(driver, actualResult.get());
    }

    private static Driver getDriver(User user, String name, String licenseNumber, String number) {
        Driver driver = new Driver();
        driver.setCarrier(user);
        driver.setName(name);
        driver.setLicenseNumber(licenseNumber);
        driver.setPhoneNumber(number);
        return driver;
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
