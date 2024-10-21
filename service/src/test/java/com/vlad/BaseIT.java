package com.vlad;

import com.vlad.util.HibernateTestUtil;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;


import java.lang.reflect.Proxy;

public abstract class BaseIT {

    private static SessionFactory sessionFactory;
    protected static EntityManager entityManager;

    @BeforeAll
    static void setupSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        entityManager = (EntityManager) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));
    }

    @BeforeEach
    void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    @AfterEach
    void closeTransaction() {
        entityManager.getTransaction().rollback();
    }

    @AfterAll
    static void shoutDownSessionFactory() {
        if (sessionFactory != null && sessionFactory.isOpen()) {
            sessionFactory.close();
        }
    }
}
