package com.vlad;

import com.vlad.entity.Role;
import com.vlad.entity.User;
import com.vlad.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

public class HibernateEntityTest {

    @Test
    void checkH2(){
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            User user = User.builder()
                    .username("MickyMouse")
                    .password("1234567")
                    .role(Role.GUEST)
                    .name("Viktoria")
                    .build();
            session.save(user);

            session.getTransaction().commit();
        }
    }
}
