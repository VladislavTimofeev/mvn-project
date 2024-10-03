package com.vlad.entity;

import com.vlad.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


class DriverIT {

    private static SessionFactory sessionFactory;
    private Session session;

    @BeforeAll
    static void createSessionFactory() {
        sessionFactory = HibernateUtil.buildSessionFactory();
    }

    @BeforeEach
    void openSession(){
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @Test
    void deleteDriver(){
        User carrier = getUser();
        session.persist(carrier);

        Driver driver = Driver.builder()
                .carrierId(carrier)
                .name("Вася Пупкин")
                .licenseNumber("ABC123456")
                .phoneNumber("+1234567890")
                .build();

        session.persist(driver);
        session.remove(driver);

        Driver actualResult = session.get(Driver.class, driver.getId());
        assertNull(actualResult);
    }

    @Test
    void updateDriver(){
        User carrier = getUser();
        session.persist(carrier);

        Driver driver = Driver.builder()
                .carrierId(carrier)
                .name("Вася Пупкин")
                .licenseNumber("ABC123456")
                .phoneNumber("+1234567890")
                .build();

        session.persist(driver);
        driver.setName("Гоша Пупкин");
        session.merge(driver);

        Driver actualResult = session.get(Driver.class, driver.getId());
        assertEquals("Гоша Пупкин", actualResult.getName());
    }

    @Test
    void getDriver(){
        User carrier = getUser();
        session.persist(carrier);

        Driver driver = Driver.builder()
                .carrierId(carrier)
                .name("Вася Пупкин")
                .licenseNumber("ABC123456")
                .phoneNumber("+1234567890")
                .build();

        session.persist(driver);

        Driver actualResult = session.get(Driver.class, driver.getId());

        assertEquals(driver.getName(), actualResult.getName());
    }

    @Test
    void createDriver() {
        User carrier = getUser();
        session.persist(carrier);

        Driver driver = Driver.builder()
                .carrierId(carrier)
                .name("Вася Пупкин")
                .licenseNumber("ABC123456")
                .phoneNumber("+1234567890")
                .build();

        session.persist(driver);

        assertNotNull(driver.getId());
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

    @AfterEach
    void closeSession(){
        session.getTransaction().rollback();
        session.close();
    }

    @AfterAll
    static void closeSessionFactory(){
        sessionFactory.close();
    }

}
