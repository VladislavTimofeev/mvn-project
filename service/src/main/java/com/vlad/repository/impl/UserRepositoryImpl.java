package com.vlad.repository.impl;

import com.vlad.entity.User;
import com.vlad.repository.BaseAbstractRepository;
import jakarta.persistence.EntityManager;

public class UserRepositoryImpl extends BaseAbstractRepository<Long, User> {

    public UserRepositoryImpl(EntityManager entityManager) {
        super(User.class, entityManager);
    }
}
