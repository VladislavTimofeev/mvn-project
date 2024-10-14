package com.vlad.repository.impl;

import com.vlad.entity.Vehicle;
import com.vlad.repository.BaseAbstractRepository;
import jakarta.persistence.EntityManager;

public class VehicleRepositoryImpl extends BaseAbstractRepository<Long, Vehicle> {

    public VehicleRepositoryImpl(EntityManager entityManager) {
        super(Vehicle.class, entityManager);
    }
}
