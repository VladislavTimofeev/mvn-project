package com.vlad.repository.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.vlad.dto.filter.UserFilterDto;
import com.vlad.entity.QUser;
import com.vlad.entity.User;
import com.vlad.repository.FilterUserRepository;
import com.vlad.repository.QPredicate;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private final EntityManager entityManager;

    @Override
    public List<User> findAllByFilter(UserFilterDto userFilterDto) {
        Predicate predicate = QPredicate.builder()
                .add(userFilterDto.getUsername(), QUser.user.username::eq)
                .add(userFilterDto.getName(), QUser.user.name::eq)
                .add(userFilterDto.getRole(), QUser.user.role::eq)
                .buildAnd();
        return new JPAQuery<User>(entityManager)
                .select(QUser.user)
                .from(QUser.user)
                .where(predicate)
                .fetch();
    }
}
