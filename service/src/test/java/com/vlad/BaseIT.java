package com.vlad;

import com.vlad.config.AppConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public abstract class BaseIT {

    protected static EntityManager entityManager;
    protected static AnnotationConfigApplicationContext context;

    @BeforeAll
    static void setupApplicationContext() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    @BeforeEach
    void beginTransaction() {
        entityManager = context.getBean(EntityManager.class);
        entityManager.getTransaction().begin();
    }

    @AfterEach
    void closeTransaction() {
        entityManager.getTransaction().rollback();
    }

    @AfterAll
    static void closeApplicationContext() {
        context.close();
    }
}
