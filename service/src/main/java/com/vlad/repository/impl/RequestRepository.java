package com.vlad.repository.impl;

import com.vlad.entity.Request;
import com.vlad.repository.AbstractRepository;
import jakarta.persistence.EntityManager;

public class RequestRepository extends AbstractRepository<Long, Request> {

    public RequestRepository(EntityManager entityManager) {
        super(Request.class, entityManager);
    }
}
