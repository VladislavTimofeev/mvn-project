package com.vlad.repository.impl;

import com.vlad.entity.Request;
import com.vlad.repository.BaseAbstractRepository;
import jakarta.persistence.EntityManager;

public class RequestRepositoryImpl extends BaseAbstractRepository<Long, Request> {

    public RequestRepositoryImpl(EntityManager entityManager) {
        super(Request.class, entityManager);
    }
}
