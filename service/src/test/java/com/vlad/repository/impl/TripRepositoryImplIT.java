package com.vlad.repository.impl;

import com.vlad.BaseIT;
import com.vlad.entity.Driver;
import com.vlad.entity.Request;
import com.vlad.entity.RequestStatus;
import com.vlad.entity.Role;
import com.vlad.entity.Trip;
import com.vlad.entity.TripStatus;
import com.vlad.entity.User;
import com.vlad.entity.Vehicle;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TripRepositoryImplIT extends BaseIT {

    private TripRepositoryImpl tripRepository;
    private RequestRepositoryImpl requestRepository;
    private UserRepositoryImpl userRepository;
    private VehicleRepositoryImpl vehicleRepository;
    private DriverRepositoryImpl driverRepository;

    @Test
    void deleteTrip(){
        userRepository = new UserRepositoryImpl(entityManager);
        requestRepository = new RequestRepositoryImpl(entityManager);
        vehicleRepository = new VehicleRepositoryImpl(entityManager);
        driverRepository = new DriverRepositoryImpl(entityManager);
        tripRepository = new TripRepositoryImpl(entityManager);

        User customer = getCustomer();
        userRepository.save(customer);
        User carrier = getCarrier();
        userRepository.save(carrier);

        Request request = new Request();
        request.setCustomer(customer);
        request.setStatus(RequestStatus.PENDING);
        request.setCargoDetails("Cargo Details");
        request.setWeight(BigDecimal.valueOf(2500.90).setScale(2, RoundingMode.HALF_UP));
        request.setPalletCount(25);
        request.setRefrigerated(false);
        request.setPickupAddress("Kolasa 45");
        request.setDeliveryAddress("Selitskogo 21");
        request.setCreationDate(LocalDate.now());
        request.setCarrier(carrier);
        requestRepository.save(request);

        Driver driver = new Driver();
        driver.setCarrier(carrier);
        driver.setName("Volodimir");
        driver.setLicenseNumber("AB1234-7");
        driver.setPhoneNumber("2456700");
        driverRepository.save(driver);

        Vehicle vehicle = new Vehicle();
        vehicle.setCarrier(carrier);
        vehicle.setLicensePlate("AA1234-5");
        vehicle.setCapacity(BigDecimal.valueOf(25.90).setScale(2, RoundingMode.HALF_UP));
        vehicle.setPalletCapacity(25);
        vehicle.setRefrigerated(true);
        vehicle.setModel("ISUZU");
        vehicleRepository.save(vehicle);

        Trip trip = new Trip();
        trip.setRequest(request);
        trip.setVehicle(vehicle);
        trip.setDriver(driver);
        trip.setDepartureTime(LocalDate.now());
        trip.setArrivalTime(LocalDate.now());
        trip.setStatus(TripStatus.PENDING);
        tripRepository.save(trip);
        tripRepository.delete(trip.getId());
        entityManager.clear();

        Optional<Trip> actualResult = tripRepository.findById(trip.getId());

        assertFalse(actualResult.isPresent());
    }

    @Test
    void updateTrip(){
        userRepository = new UserRepositoryImpl(entityManager);
        requestRepository = new RequestRepositoryImpl(entityManager);
        vehicleRepository = new VehicleRepositoryImpl(entityManager);
        driverRepository = new DriverRepositoryImpl(entityManager);
        tripRepository = new TripRepositoryImpl(entityManager);

        User customer = getCustomer();
        userRepository.save(customer);
        User carrier = getCarrier();
        userRepository.save(carrier);

        Request request = new Request();
        request.setCustomer(customer);
        request.setStatus(RequestStatus.PENDING);
        request.setCargoDetails("Cargo Details");
        request.setWeight(BigDecimal.valueOf(2500.90).setScale(2, RoundingMode.HALF_UP));
        request.setPalletCount(25);
        request.setRefrigerated(false);
        request.setPickupAddress("Kolasa 45");
        request.setDeliveryAddress("Selitskogo 21");
        request.setCreationDate(LocalDate.now());
        request.setCarrier(carrier);
        requestRepository.save(request);

        Driver driver = new Driver();
        driver.setCarrier(carrier);
        driver.setName("Volodimir");
        driver.setLicenseNumber("AB1234-7");
        driver.setPhoneNumber("2456700");
        driverRepository.save(driver);

        Vehicle vehicle = new Vehicle();
        vehicle.setCarrier(carrier);
        vehicle.setLicensePlate("AA1234-5");
        vehicle.setCapacity(BigDecimal.valueOf(25.90).setScale(2, RoundingMode.HALF_UP));
        vehicle.setPalletCapacity(25);
        vehicle.setRefrigerated(true);
        vehicle.setModel("ISUZU");
        vehicleRepository.save(vehicle);

        Trip trip = new Trip();
        trip.setRequest(request);
        trip.setVehicle(vehicle);
        trip.setDriver(driver);
        trip.setDepartureTime(LocalDate.now());
        trip.setArrivalTime(LocalDate.now());
        trip.setStatus(TripStatus.PENDING);
        tripRepository.save(trip);
        trip.setStatus(TripStatus.COMPLETED);
        tripRepository.update(trip);
        entityManager.flush();
        entityManager.clear();

        Optional<Trip> actualResult = tripRepository.findById(trip.getId());

        assertTrue(actualResult.isPresent());
        assertEquals(TripStatus.COMPLETED, actualResult.get().getStatus());
    }

    @Test
    void saveTrip() {
        userRepository = new UserRepositoryImpl(entityManager);
        requestRepository = new RequestRepositoryImpl(entityManager);
        vehicleRepository = new VehicleRepositoryImpl(entityManager);
        driverRepository = new DriverRepositoryImpl(entityManager);
        tripRepository = new TripRepositoryImpl(entityManager);

        User customer = getCustomer();
        userRepository.save(customer);
        User carrier = getCarrier();
        userRepository.save(carrier);

        Request request = new Request();
        request.setCustomer(customer);
        request.setStatus(RequestStatus.PENDING);
        request.setCargoDetails("Cargo Details");
        request.setWeight(BigDecimal.valueOf(2500.90).setScale(2, RoundingMode.HALF_UP));
        request.setPalletCount(25);
        request.setRefrigerated(false);
        request.setPickupAddress("Kolasa 45");
        request.setDeliveryAddress("Selitskogo 21");
        request.setCreationDate(LocalDate.now());
        request.setCarrier(carrier);
        requestRepository.save(request);

        Driver driver = new Driver();
        driver.setCarrier(carrier);
        driver.setName("Volodimir");
        driver.setLicenseNumber("AB1234-7");
        driver.setPhoneNumber("2456700");
        driverRepository.save(driver);

        Vehicle vehicle = new Vehicle();
        vehicle.setCarrier(carrier);
        vehicle.setLicensePlate("AA1234-5");
        vehicle.setCapacity(BigDecimal.valueOf(25.90).setScale(2, RoundingMode.HALF_UP));
        vehicle.setPalletCapacity(25);
        vehicle.setRefrigerated(true);
        vehicle.setModel("ISUZU");
        vehicleRepository.save(vehicle);

        Trip trip = new Trip();
        trip.setRequest(request);
        trip.setVehicle(vehicle);
        trip.setDriver(driver);
        trip.setDepartureTime(LocalDate.now());
        trip.setArrivalTime(LocalDate.now());
        trip.setStatus(TripStatus.PENDING);
        tripRepository.save(trip);
        entityManager.flush();
        entityManager.clear();

        Optional<Trip> actualResult = tripRepository.findById(trip.getId());

        assertTrue(actualResult.isPresent());
        assertEquals(trip, actualResult.get());
    }

    private static User getCarrier() {
        User user = new User();
        user.setUsername("Timon");
        user.setPassword("999999");
        user.setRole(Role.CARRIER);
        user.setName("Twinki");
        user.setContactInfo("tv@gmail.com");
        user.setAddress("Selickogo 21");
        return user;
    }

    private static User getCustomer() {
        User user = new User();
        user.setUsername("MickyMouse");
        user.setPassword("123MickyMouse123");
        user.setRole(Role.CUSTOMER);
        user.setName("Martin");
        user.setContactInfo("martin@gmail.com");
        user.setAddress("Mazurova 4-56");
        return user;
    }

}
