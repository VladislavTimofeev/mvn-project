package com.vlad.config;

import com.vlad.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(ApplicationConfiguration.class)
public class AppConfig {

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateTestUtil.buildSessionFactory();
    }

}
