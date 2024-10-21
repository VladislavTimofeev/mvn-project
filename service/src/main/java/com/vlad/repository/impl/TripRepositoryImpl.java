package com.vlad.repository.impl;

import com.vlad.entity.Trip;
import com.vlad.repository.BaseAbstractRepository;
import jakarta.persistence.EntityManager;

public class TripRepositoryImpl extends BaseAbstractRepository<Long, Trip> {

    public TripRepositoryImpl(EntityManager entityManager) {
        super(Trip.class, entityManager);
    }
}
