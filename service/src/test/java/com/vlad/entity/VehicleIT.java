package com.vlad.entity;

import com.vlad.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


class VehicleIT {

    private static SessionFactory sessionFactory;
    private Session session;

    @BeforeAll
    static void createSessionFactory() {
        sessionFactory = HibernateUtil.buildSessionFactory();
    }

    @BeforeEach
    void openSession() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @Test
    void deleteVehicle() {
        User carrier = getCarrier();
        Vehicle vehicle = Vehicle.builder()
                .carrierId(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.9))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
                .build();

        session.persist(vehicle);
        session.remove(vehicle);

        Vehicle actualResult = session.get(Vehicle.class, vehicle.getId());
        assertNull(actualResult);
    }

    @Test
    void updateVehicle() {
        User carrier = getCarrier();
        Vehicle vehicle = Vehicle.builder()
                .carrierId(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.9))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
                .build();

        session.persist(vehicle);
        vehicle.setLicensePlate("9999");
        session.merge(vehicle);

        Vehicle actualResult = session.get(Vehicle.class, vehicle.getId());
        assertEquals("9999", actualResult.getLicensePlate());
    }

    @Test
    void getVehicle() {
        User carrier = getCarrier();
        Vehicle vehicle = Vehicle.builder()
                .carrierId(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.9))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
                .build();

        session.persist(vehicle);
        Vehicle actualResult = session.get(Vehicle.class, vehicle.getId());

        assertEquals(vehicle.getId(), actualResult.getId());
    }

    @Test
    void createVehicle() {
        User carrier = getCarrier();
        Vehicle vehicle = Vehicle.builder()
                .carrierId(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.9))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
                .build();

        session.persist(vehicle);

        assertNotNull(vehicle.getId());
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

    @AfterEach
    void closeSession() {
        session.getTransaction().rollback();
        session.close();
    }

    @AfterAll
    static void closeSessionFactory() {
        sessionFactory.close();
    }

}
