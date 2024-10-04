package com.vlad.entity;

import com.vlad.TestBase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


class TripIT extends TestBase {

    @Test
    void deleteTrip() {
        User customer = getCustomer();
        session.persist(customer);
        User carrier = getCarrier();
        session.persist(carrier);
        Request request = Request.builder()
                .customer(customer)
                .status(RequestStatus.PENDING)
                .cargoDetails("Fresh Fish")
                .weight(BigDecimal.valueOf(100.59))
                .palletCount(20)
                .refrigerated(true)
                .pickupAddress("Selitskogo 21")
                .deliveryAddress("Matusevicha 39")
                .creationDate(LocalDate.now())
                .carrier(carrier)
                .build();
        session.persist(request);
        Driver driver = Driver.builder()
                .carrier(carrier)
                .name("Вася Пупкин")
                .licenseNumber("ABC123456")
                .phoneNumber("+1234567890")
                .build();
        session.persist(driver);
        Vehicle vehicle = Vehicle.builder()
                .carrier(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.9))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
                .build();
        session.persist(vehicle);
        Trip trip = Trip.builder()
                .request(request)
                .vehicle(vehicle)
                .driver(driver)
                .departureTime(LocalDate.now())
                .arrivalTime(LocalDate.now())
                .status(TripStatus.PENDING)
                .build();
        session.persist(trip);
        session.remove(trip);
        session.flush();
        session.clear();

        Trip actualResult = session.get(Trip.class, trip.getId());

        assertNull(actualResult);
    }

    @Test
    void updateTrip() {
        User customer = getCustomer();
        session.persist(customer);
        User carrier = getCarrier();
        session.persist(carrier);
        Request request = Request.builder()
                .customer(customer)
                .status(RequestStatus.PENDING)
                .cargoDetails("Fresh Fish")
                .weight(BigDecimal.valueOf(100.59))
                .palletCount(20)
                .refrigerated(true)
                .pickupAddress("Selitskogo 21")
                .deliveryAddress("Matusevicha 39")
                .creationDate(LocalDate.now())
                .carrier(carrier)
                .build();
        session.persist(request);
        Driver driver = Driver.builder()
                .carrier(carrier)
                .name("Вася Пупкин")
                .licenseNumber("ABC123456")
                .phoneNumber("+1234567890")
                .build();
        session.persist(driver);
        Vehicle vehicle = Vehicle.builder()
                .carrier(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.9))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
                .build();
        session.persist(vehicle);
        Trip trip = Trip.builder()
                .request(request)
                .vehicle(vehicle)
                .driver(driver)
                .departureTime(LocalDate.now())
                .arrivalTime(LocalDate.now())
                .status(TripStatus.PENDING)
                .build();
        session.persist(trip);
        trip.setStatus(TripStatus.IN_PROGRESS);
        session.merge(trip);
        session.flush();
        session.clear();

        Trip actualResult = session.get(Trip.class, trip.getId());

        assertEquals(TripStatus.IN_PROGRESS, actualResult.getStatus());
    }

    @Test
    void getTrip() {
        User customer = getCustomer();
        session.persist(customer);
        User carrier = getCarrier();
        session.persist(carrier);
        Request request = Request.builder()
                .customer(customer)
                .status(RequestStatus.PENDING)
                .cargoDetails("Fresh Fish")
                .weight(BigDecimal.valueOf(100.59))
                .palletCount(20)
                .refrigerated(true)
                .pickupAddress("Selitskogo 21")
                .deliveryAddress("Matusevicha 39")
                .creationDate(LocalDate.now())
                .carrier(carrier)
                .build();
        session.persist(request);
        Driver driver = Driver.builder()
                .carrier(carrier)
                .name("Вася Пупкин")
                .licenseNumber("ABC123456")
                .phoneNumber("+1234567890")
                .build();
        session.persist(driver);
        Vehicle vehicle = Vehicle.builder()
                .carrier(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.9))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
                .build();
        session.persist(vehicle);
        Trip trip = Trip.builder()
                .request(request)
                .vehicle(vehicle)
                .driver(driver)
                .departureTime(LocalDate.now())
                .arrivalTime(LocalDate.now())
                .status(TripStatus.PENDING)
                .build();
        session.persist(trip);
        session.flush();
        session.clear();

        Trip actualResult = session.get(Trip.class, trip.getId());

        assertNotNull(actualResult);
    }

    @Test
    void createTrip() {
        User customer = getCustomer();
        session.persist(customer);
        User carrier = getCarrier();
        session.persist(carrier);
        Request request = Request.builder()
                .customer(customer)
                .status(RequestStatus.PENDING)
                .cargoDetails("Fresh Fish")
                .weight(BigDecimal.valueOf(100.59))
                .palletCount(20)
                .refrigerated(true)
                .pickupAddress("Selitskogo 21")
                .deliveryAddress("Matusevicha 39")
                .creationDate(LocalDate.now())
                .carrier(carrier)
                .build();
        session.persist(request);
        Driver driver = Driver.builder()
                .carrier(carrier)
                .name("Вася Пупкин")
                .licenseNumber("ABC123456")
                .phoneNumber("+1234567890")
                .build();
        session.persist(driver);
        Vehicle vehicle = Vehicle.builder()
                .carrier(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.9))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
                .build();
        session.persist(vehicle);
        Trip trip = Trip.builder()
                .request(request)
                .vehicle(vehicle)
                .driver(driver)
                .departureTime(LocalDate.now())
                .arrivalTime(LocalDate.now())
                .status(TripStatus.PENDING)
                .build();
        session.persist(trip);
        session.flush();
        session.clear();

        Trip actualResult = session.get(Trip.class, trip.getId());

        assertEquals(trip, actualResult);

    }

    private static Driver getDriver() {
        return Driver.builder()
                .carrier(getCarrier())
                .name("Вася Пупкин")
                .licenseNumber("ABC123456")
                .phoneNumber("+1234567890")
                .build();
    }

    private static Vehicle getVehicle() {
        return Vehicle.builder()
                .carrier(getCarrier())
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.9))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
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

}
