package com.vlad.repository.impl;

import com.vlad.entity.Driver;
import com.vlad.repository.BaseAbstractRepository;
import jakarta.persistence.EntityManager;

public class DriverRepositoryImpl extends BaseAbstractRepository<Long, Driver> {

    public DriverRepositoryImpl(EntityManager entityManager) {
        super(Driver.class, entityManager);
    }
}
