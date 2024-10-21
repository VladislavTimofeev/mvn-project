package com.vlad.repository.impl;

import com.vlad.entity.Trip;
import com.vlad.repository.AbstractRepository;
import jakarta.persistence.EntityManager;

public class TripRepository extends AbstractRepository<Long, Trip> {

    public TripRepository(EntityManager entityManager) {
        super(Trip.class, entityManager);
    }
}
