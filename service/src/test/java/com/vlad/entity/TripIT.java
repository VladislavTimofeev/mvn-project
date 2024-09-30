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
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


class TripIT {

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
    void deleteTrip(){
        Request request = getRequest();
        Trip trip = Trip.builder()
                .requestId(request)
                .vehicleId(getVehicle())
                .driverId(getDriver())
                .departureTime(LocalDate.now())
                .arrivalTime(LocalDate.now())
                .status(TripStatus.PENDING)
                .build();

        session.persist(trip);
        session.remove(trip);

        Trip actualResult = session.get(Trip.class, trip.getId());
        assertNull(actualResult);
    }

    @Test
    void updateTrip(){
        Request request = getRequest();
        Trip trip = Trip.builder()
                .requestId(request)
                .vehicleId(getVehicle())
                .driverId(getDriver())
                .departureTime(LocalDate.now())
                .arrivalTime(LocalDate.now())
                .status(TripStatus.PENDING)
                .build();

        session.persist(trip);
        trip.setStatus(TripStatus.IN_PROGRESS);
        session.merge(trip);

        Trip actualResult = session.get(Trip.class, trip.getId());
        assertEquals(TripStatus.IN_PROGRESS, actualResult.getStatus());
    }

    @Test
    void getTrip(){
        Request request = getRequest();
        Trip trip = Trip.builder()
                .requestId(request)
                .vehicleId(getVehicle())
                .driverId(getDriver())
                .departureTime(LocalDate.now())
                .arrivalTime(LocalDate.now())
                .status(TripStatus.PENDING)
                .build();

        session.persist(trip);
        Trip actualResult = session.get(Trip.class, trip.getId());

        assertEquals(trip.getId(), actualResult.getId());
    }

    @Test
    void createTrip() {
        Request request = getRequest();
        Trip trip = Trip.builder()
                .requestId(request)
                .vehicleId(getVehicle())
                .driverId(getDriver())
                .departureTime(LocalDate.now())
                .arrivalTime(LocalDate.now())
                .status(TripStatus.PENDING)
                .build();

        session.persist(trip);

        assertNotNull(trip.getId());

    }

    private static Driver getDriver() {
        return Driver.builder()
                .carrierId(getCarrier())
                .name("Вася Пупкин")
                .licenseNumber("ABC123456")
                .phoneNumber("+1234567890")
                .build();
    }

    private static Vehicle getVehicle() {
        return Vehicle.builder()
                .carrierId(getCarrier())
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.9))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
                .build();
    }

    private static Request getRequest() {
        return Request.builder()
                .customerId(getCustomer())
                .status(RequestStatus.PENDING)
                .cargoDetails("Fresh Fish")
                .weight(BigDecimal.valueOf(100.59))
                .palletCount(20)
                .refrigerated(true)
                .pickupAddress("Selitskogo 21")
                .deliveryAddress("Matusevicha 39")
                .creationDate(LocalDate.now())
                .carrierId(getCarrier())
                .build();
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

    private static User getCustomer() {
        return User.builder()
                .username("MickyMouse")
                .password("1234567")
                .role(Role.CUSTOMER)
                .name("Viktoria")
                .contactInfo("vika@example.com")
                .address("Mazurova 4")
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