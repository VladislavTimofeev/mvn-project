package com.vlad.repository.impl;

import com.vlad.entity.User;
import com.vlad.repository.AbstractRepository;
import jakarta.persistence.EntityManager;

public class UserRepository extends AbstractRepository<Long, User> {

    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }
}
