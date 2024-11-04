package com.vlad.repository;

import com.vlad.entity.Driver;
import com.vlad.entity.Request;
import com.vlad.entity.RequestStatus;
import com.vlad.entity.Role;
import com.vlad.entity.Trip;
import com.vlad.entity.TripStatus;
import com.vlad.entity.User;
import com.vlad.entity.Vehicle;
import com.vlad.repository.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class TripRepositoryIT extends IntegrationTestBase {

    private final TripRepository tripRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    @Test
    void deleteTrip() {
        User customer = getCustomer();
        userRepository.save(customer);
        User carrier = getCarrier();
        userRepository.save(carrier);
        Request request = getRequest(customer, carrier);
        requestRepository.save(request);
        Driver driver = getDriver(carrier);
        driverRepository.save(driver);
        Vehicle vehicle = getVehicle(carrier);
        vehicleRepository.save(vehicle);
        Trip trip = getTrip(request, vehicle, driver);
        tripRepository.save(trip);

        tripRepository.delete(trip);

        Optional<Trip> actualResult = tripRepository.findById(trip.getId());
        assertFalse(actualResult.isPresent());
    }

    @Test
    void updateTrip() {
        User customer = getCustomer();
        userRepository.save(customer);
        User carrier = getCarrier();
        userRepository.save(carrier);
        Request request = getRequest(customer, carrier);
        requestRepository.save(request);
        Driver driver = getDriver(carrier);
        driverRepository.save(driver);
        Vehicle vehicle = getVehicle(carrier);
        vehicleRepository.save(vehicle);
        Trip trip = getTrip(request, vehicle, driver);
        tripRepository.save(trip);
        trip.setStatus(TripStatus.COMPLETED);

        tripRepository.save(trip);

        Optional<Trip> actualResult = tripRepository.findById(trip.getId());
        assertTrue(actualResult.isPresent());
        assertEquals(TripStatus.COMPLETED, actualResult.get().getStatus());
    }

    @Test
    void saveTrip() {
        User customer = getCustomer();
        userRepository.save(customer);
        User carrier = getCarrier();
        userRepository.save(carrier);
        Request request = getRequest(customer, carrier);
        requestRepository.save(request);
        Driver driver = getDriver(carrier);
        driverRepository.save(driver);
        Vehicle vehicle = getVehicle(carrier);
        vehicleRepository.save(vehicle);
        Trip trip = getTrip(request, vehicle, driver);

        tripRepository.save(trip);

        Optional<Trip> actualResult = tripRepository.findById(trip.getId());
        assertTrue(actualResult.isPresent());
        assertEquals(trip, actualResult.get());
    }

    private static Vehicle getVehicle(User carrier) {
        Vehicle vehicle = new Vehicle();
        vehicle.setCarrier(carrier);
        vehicle.setLicensePlate("AA1234-5");
        vehicle.setCapacity(BigDecimal.valueOf(25.90).setScale(2, RoundingMode.HALF_UP));
        vehicle.setPalletCapacity(25);
        vehicle.setRefrigerated(true);
        vehicle.setModel("ISUZU");
        return vehicle;
    }

    private static Trip getTrip(Request request, Vehicle vehicle, Driver driver) {
        Trip trip = new Trip();
        trip.setRequest(request);
        trip.setVehicle(vehicle);
        trip.setDriver(driver);
        trip.setDepartureTime(LocalDate.now());
        trip.setArrivalTime(LocalDate.now());
        trip.setStatus(TripStatus.PENDING);
        return trip;
    }

    private static Driver getDriver(User carrier) {
        Driver driver = new Driver();
        driver.setCarrier(carrier);
        driver.setName("Volodimir");
        driver.setLicenseNumber("AB1234-7");
        driver.setPhoneNumber("2456700");
        return driver;
    }

    private static Request getRequest(User customer, User carrier) {
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
        return request;
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
