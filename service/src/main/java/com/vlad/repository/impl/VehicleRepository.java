package com.vlad.repository.impl;

import com.vlad.entity.Vehicle;
import com.vlad.repository.AbstractRepository;
import jakarta.persistence.EntityManager;

public class VehicleRepository extends AbstractRepository<Long, Vehicle> {

    public VehicleRepository(EntityManager entityManager) {
        super(Vehicle.class, entityManager);
    }
}
