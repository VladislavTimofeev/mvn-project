package com.vlad.entity;

import com.vlad.TestBase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class VehicleIT extends TestBase {

    @Test
    void deleteVehicle() {
        User carrier = getCarrier();
        session.persist(carrier);
        Vehicle vehicle = Vehicle.builder()
                .carrier(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.9))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
                .build();
        session.persist(vehicle);
        session.remove(vehicle);
        session.flush();
        session.clear();

        Vehicle actualResult = session.get(Vehicle.class, vehicle.getId());

        assertNull(actualResult);
    }

    @Test
    void updateVehicle() {
        User carrier = getCarrier();
        session.persist(carrier);
        Vehicle vehicle = Vehicle.builder()
                .carrier(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.9))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
                .build();
        session.persist(vehicle);
        vehicle.setLicensePlate("9999");
        session.merge(vehicle);
        session.flush();
        session.clear();

        Vehicle actualResult = session.get(Vehicle.class, vehicle.getId());

        assertEquals("9999", actualResult.getLicensePlate());
    }

    @Test
    void getVehicle() {
        User carrier = getCarrier();
        session.persist(carrier);
        Vehicle vehicle = Vehicle.builder()
                .carrier(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.9))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
                .build();
        session.persist(vehicle);
        session.flush();
        session.clear();

        Vehicle actualResult = session.get(Vehicle.class, vehicle.getId());

        assertEquals(vehicle, actualResult);
    }

    @Test
    void createVehicle() {
        User carrier = getCarrier();
        session.persist(carrier);
        Vehicle vehicle = Vehicle.builder()
                .carrier(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.9))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
                .build();
        session.persist(vehicle);
        session.flush();
        session.clear();

        Vehicle actualResult = session.get(Vehicle.class, vehicle.getId());

        assertEquals(vehicle, actualResult);
    }

    private static User getCarrier() {
        return User.builder()
                .username("MilkyWay")
                .password("1234567")
                .role(Role.CARRIER)
                .name("Alexandra")
                .contactInfo("alexandra@example.com")
                .address("Lobonka 44")
                .build();
    }

}
