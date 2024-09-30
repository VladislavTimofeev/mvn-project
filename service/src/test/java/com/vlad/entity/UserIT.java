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

class UserIT {

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
    void deleteUser(){
        User user = User.builder()
                .username("MickyMouse")
                .password("1234567")
                .role(Role.GUEST)
                .name("Viktoria")
                .contactInfo("vika@example.com")
                .address("Mazurova 4")
                .build();

        session.persist(user);
        session.remove(user);

        User actualResult = session.get(User.class, user.getId());
        assertNull(actualResult);

    }

    @Test
    void updateUser(){
        User user = User.builder()
                .username("MickyMouse")
                .password("1234567")
                .role(Role.GUEST)
                .name("Viktoria")
                .contactInfo("vika@example.com")
                .address("Mazurova 4")
                .build();

        session.persist(user);
        user.setName("ViktoriaPobeditelnica");
        session.merge(user);

        User actualResult = session.get(User.class, user.getId());
        assertEquals("ViktoriaPobeditelnica", actualResult.getName());
    }

    @Test
    void getUser(){
        User user = User.builder()
                .username("MickyMouse")
                .password("1234567")
                .role(Role.GUEST)
                .name("Viktoria")
                .contactInfo("vika@example.com")
                .address("Mazurova 4")
                .build();

        session.persist(user);
        User actualResult = session.get(User.class, user.getId());

        assertEquals(user.getUsername(), actualResult.getUsername());
    }

    @Test
    void createUser() {
        User user = User.builder()
                .username("MickyMouse")
                .password("1234567")
                .role(Role.GUEST)
                .name("Viktoria")
                .contactInfo("vika@example.com")
                .address("Mazurova 4")
                .build();

        session.persist(user);

        assertNotNull(user.getId());
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
