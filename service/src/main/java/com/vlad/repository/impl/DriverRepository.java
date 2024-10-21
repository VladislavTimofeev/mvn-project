package com.vlad.repository.impl;

import com.vlad.entity.Driver;
import com.vlad.repository.AbstractRepository;
import jakarta.persistence.EntityManager;

public class DriverRepository extends AbstractRepository<Long, Driver> {

    public DriverRepository(EntityManager entityManager) {
        super(Driver.class, entityManager);
    }
}
