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


class RequestIT {

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
    void deleteRequest() {
        User customer = getCustomer();
        User carrier = getCarrier();
        Request request = Request.builder()
                .customerId(customer)
                .status(RequestStatus.PENDING)
                .cargoDetails("Fresh Fish")
                .weight(BigDecimal.valueOf(100.59))
                .palletCount(20)
                .refrigerated(true)
                .pickupAddress("Selitskogo 21")
                .deliveryAddress("Matusevicha 39")
                .creationDate(LocalDate.now())
                .carrierId(carrier)
                .build();
        session.persist(request);
        session.remove(request);

        Request actualResult = session.get(Request.class, request.getId());
        assertNull(actualResult);
    }

    @Test
    void updateRequest() {
        User customer = getCustomer();
        User carrier = getCarrier();
        Request request = Request.builder()
                .customerId(customer)
                .status(RequestStatus.PENDING)
                .cargoDetails("Fresh Fish")
                .weight(BigDecimal.valueOf(100.59))
                .palletCount(20)
                .refrigerated(true)
                .pickupAddress("Selitskogo 21")
                .deliveryAddress("Matusevicha 39")
                .creationDate(LocalDate.now())
                .carrierId(carrier)
                .build();
        session.persist(request);
        request.setStatus(RequestStatus.IN_PROGRESS);
        session.merge(request);

        Request actualResult = session.get(Request.class, request.getId());
        assertEquals(RequestStatus.IN_PROGRESS, actualResult.getStatus());

    }

    @Test
    void getRequest(){
        User customer = getCustomer();
        User carrier = getCarrier();
        Request request = Request.builder()
                .customerId(customer)
                .status(RequestStatus.PENDING)
                .cargoDetails("Fresh Fish")
                .weight(BigDecimal.valueOf(100.59))
                .palletCount(20)
                .refrigerated(true)
                .pickupAddress("Selitskogo 21")
                .deliveryAddress("Matusevicha 39")
                .creationDate(LocalDate.now())
                .carrierId(carrier)
                .build();
        session.persist(request);

        Request actualResult = session.get(Request.class, request.getId());

        assertEquals(request.getId(), actualResult.getId());

    }

    @Test
    void createRequest() {
        User customer = getCustomer();
        User carrier = getCarrier();
        Request request = Request.builder()
                .customerId(customer)
                .status(RequestStatus.PENDING)
                .cargoDetails("Fresh Fish")
                .weight(BigDecimal.valueOf(100.59))
                .palletCount(20)
                .refrigerated(true)
                .pickupAddress("Selitskogo 21")
                .deliveryAddress("Matusevicha 39")
                .creationDate(LocalDate.now())
                .carrierId(carrier)
                .build();
        session.persist(request);

        assertNotNull(request.getId());
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
