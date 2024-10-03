package com.vlad.entity;

import com.vlad.TestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class DriverIT extends TestBase {

    @Test
    void deleteDriver() {
        User carrier = getUser();
        session.persist(carrier);
        Driver driver = Driver.builder()
                .carrier(carrier)
                .name("Вася Пупкин")
                .licenseNumber("ABC123456")
                .phoneNumber("+1234567890")
                .build();
        session.persist(driver);
        session.remove(driver);
        session.flush();
        session.clear();

        Driver actualResult = session.get(Driver.class, driver.getId());

        assertNull(actualResult);
    }

    @Test
    void updateDriver() {
        User carrier = getUser();
        session.persist(carrier);
        Driver driver = Driver.builder()
                .carrier(carrier)
                .name("Вася Пупкин")
                .licenseNumber("ABC123456")
                .phoneNumber("+1234567890")
                .build();
        session.persist(driver);
        driver.setName("Гоша Пупкин");
        session.merge(driver);
        session.flush();
        session.clear();

        Driver actualResult = session.get(Driver.class, driver.getId());

        assertEquals("Гоша Пупкин", actualResult.getName());
    }

    @Test
    void getDriver() {
        User carrier = getUser();
        session.persist(carrier);
        Driver driver = Driver.builder()
                .carrier(carrier)
                .name("Вася Пупкин")
                .licenseNumber("ABC123456")
                .phoneNumber("+1234567890")
                .build();
        session.persist(driver);
        session.flush();
        session.clear();

        Driver actualResult = session.get(Driver.class, driver.getId());

        assertEquals(driver, actualResult);
    }

    @Test
    void createDriver() {
        User carrier = getUser();
        session.persist(carrier);
        Driver driver = Driver.builder()
                .carrier(carrier)
                .name("Вася Пупкин")
                .licenseNumber("ABC123456")
                .phoneNumber("+1234567890")
                .build();
        session.persist(driver);
        session.flush();
        session.clear();

        Driver actualResult = session.get(Driver.class, driver.getId());

        assertEquals(driver, actualResult);
    }

    private static User getUser() {
        return User.builder()
                .username("MickyMouse")
                .password("1234567")
                .role(Role.CARRIER)
                .name("Viktoria")
                .contactInfo("vika@example.com")
                .address("Mazurova 4")
                .build();
    }

}
