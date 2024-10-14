package com.vlad.entity;

import com.vlad.TestBase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class RequestIT extends TestBase {

    @Test
    void deleteRequest() {
        User customer = getCustomer();
        session.persist(customer);
        User carrier = getCarrier();
        session.persist(carrier);
        Request request = Request.builder()
                .customer(customer)
                .status(RequestStatus.PENDING)
                .cargoDetails("Fresh Fish")
                .weight(BigDecimal.valueOf(100.50).setScale(2, RoundingMode.HALF_UP))
                .palletCount(20)
                .refrigerated(true)
                .pickupAddress("Selitskogo 21")
                .deliveryAddress("Matusevicha 39")
                .creationDate(LocalDate.now())
                .carrier(carrier)
                .build();
        session.persist(request);
        session.remove(request);
        session.flush();
        session.clear();

        Request actualResult = session.get(Request.class, request.getId());

        assertNull(actualResult);
    }

    @Test
    void updateRequest() {
        User customer = getCustomer();
        session.persist(customer);
        User carrier = getCarrier();
        session.persist(carrier);
        Request request = Request.builder()
                .customer(customer)
                .status(RequestStatus.PENDING)
                .cargoDetails("Fresh Fish")
                .weight(BigDecimal.valueOf(100.50).setScale(2, RoundingMode.HALF_UP))
                .palletCount(20)
                .refrigerated(true)
                .pickupAddress("Selitskogo 21")
                .deliveryAddress("Matusevicha 39")
                .creationDate(LocalDate.now())
                .carrier(carrier)
                .build();
        session.persist(request);
        request.setStatus(RequestStatus.IN_PROGRESS);
        session.merge(request);
        session.flush();
        session.clear();

        Request actualResult = session.get(Request.class, request.getId());

        assertEquals(RequestStatus.IN_PROGRESS, actualResult.getStatus());

    }

    @Test
    void getRequest() {
        User customer = getCustomer();
        session.persist(customer);
        User carrier = getCarrier();
        session.persist(carrier);
        Request request = Request.builder()
                .customer(customer)
                .status(RequestStatus.PENDING)
                .cargoDetails("Fresh Fish")
                .weight(BigDecimal.valueOf(100.50).setScale(2, RoundingMode.HALF_UP))
                .palletCount(20)
                .refrigerated(true)
                .pickupAddress("Selitskogo 21")
                .deliveryAddress("Matusevicha 39")
                .creationDate(LocalDate.now())
                .carrier(carrier)
                .build();
        session.persist(request);
        session.flush();
        session.clear();

        Request actualResult = session.get(Request.class, request.getId());

        assertEquals(request, actualResult);

    }

    @Test
    void createRequest() {
        User customer = getCustomer();
        session.persist(customer);
        User carrier = getCarrier();
        session.persist(carrier);
        Request request = Request.builder()
                .customer(customer)
                .status(RequestStatus.PENDING)
                .cargoDetails("Fresh Fish")
                .weight(BigDecimal.valueOf(100.55).setScale(2, RoundingMode.HALF_UP))
                .palletCount(20)
                .refrigerated(true)
                .pickupAddress("Selitskogo 21")
                .deliveryAddress("Matusevicha 39")
                .creationDate(LocalDate.now())
                .carrier(carrier)
                .build();
        session.persist(request);
        session.flush();
        session.clear();

        Request actualResult = session.get(Request.class, request.getId());

        assertEquals(request, actualResult);
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
