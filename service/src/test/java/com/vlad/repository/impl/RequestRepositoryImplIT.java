package com.vlad.repository.impl;

import com.vlad.BaseIT;
import com.vlad.entity.Request;
import com.vlad.entity.RequestStatus;
import com.vlad.entity.Role;
import com.vlad.entity.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestRepositoryImplIT extends BaseIT {

    private RequestRepositoryImpl requestRepository;
    private UserRepositoryImpl userRepository;

    @Test
    void deleteRequest() {
        userRepository = new UserRepositoryImpl(entityManager);
        requestRepository = new RequestRepositoryImpl(entityManager);
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
        requestRepository.delete(request.getId());
        entityManager.clear();

        Optional<Request> actualResult = requestRepository.findById(request.getId());

        assertFalse(actualResult.isPresent());
    }

    @Test
    void updateRequest() {
        userRepository = new UserRepositoryImpl(entityManager);
        requestRepository = new RequestRepositoryImpl(entityManager);
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
        request.setStatus(RequestStatus.IN_PROGRESS);
        requestRepository.update(request);
        entityManager.flush();
        entityManager.clear();

        Optional<Request> actualResult = requestRepository.findById(request.getId());

        assertTrue(actualResult.isPresent());
        assertEquals(RequestStatus.IN_PROGRESS, actualResult.get().getStatus());
    }

    @Test
    void getAllRequests() {
        userRepository = new UserRepositoryImpl(entityManager);
        requestRepository = new RequestRepositoryImpl(entityManager);
        User customer1 = getCustomer();
        userRepository.save(customer1);
        User carrier1 = getCarrier();
        userRepository.save(carrier1);
        Request request = new Request();
        request.setCustomer(customer1);
        request.setStatus(RequestStatus.PENDING);
        request.setCargoDetails("Cargo Details");
        request.setWeight(BigDecimal.valueOf(2500.90).setScale(2, RoundingMode.HALF_UP));
        request.setPalletCount(25);
        request.setRefrigerated(false);
        request.setPickupAddress("Kolasa 45");
        request.setDeliveryAddress("Selitskogo 21");
        request.setCreationDate(LocalDate.now());
        request.setCarrier(carrier1);
        requestRepository.save(request);
        User customer2 = getCustomer1();
        userRepository.save(customer2);
        User carrier2 = getCarrier1();
        userRepository.save(carrier2);
        Request request2 = new Request();
        request2.setCustomer(customer2);
        request2.setStatus(RequestStatus.IN_PROGRESS);
        request2.setCargoDetails("Some Cargo Details");
        request2.setWeight(BigDecimal.valueOf(1800.40).setScale(2, RoundingMode.HALF_UP));
        request2.setPalletCount(20);
        request2.setRefrigerated(true);
        request2.setPickupAddress("Pushkina 34");
        request2.setDeliveryAddress("Kasperskogo 33");
        request2.setCreationDate(LocalDate.now());
        request2.setCarrier(carrier2);
        requestRepository.save(request2);
        entityManager.flush();
        entityManager.clear();

        List<Request> actualResult = requestRepository.findAll();

        assertEquals(2, actualResult.size());
        assertThat(actualResult).containsExactlyInAnyOrder(request, request2);
    }

    @Test
    void saveRequest() {
        userRepository = new UserRepositoryImpl(entityManager);
        requestRepository = new RequestRepositoryImpl(entityManager);
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
        entityManager.flush();
        entityManager.clear();

        Optional<Request> actualResult = requestRepository.findById(request.getId());

        assertTrue(actualResult.isPresent());
        assertEquals(request, actualResult.get());
    }

    private static User getCarrier1() {
        User user = new User();
        user.setUsername("Pumba");
        user.setPassword("534534543");
        user.setRole(Role.CARRIER);
        user.setName("Papo4ka");
        user.setContactInfo("papa@gmail.com");
        user.setAddress("Beruta 5");
        return user;
    }

    private static User getCustomer1() {
        User user = new User();
        user.setUsername("King");
        user.setPassword("Kingforever");
        user.setRole(Role.CUSTOMER);
        user.setName("King");
        user.setContactInfo("king@gmail.com");
        user.setAddress("Pritickogo 23");
        return user;
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
