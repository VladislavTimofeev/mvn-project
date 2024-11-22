package com.vlad.repository;

import com.vlad.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TripRepository extends
        JpaRepository<Trip, Long>,
        QuerydslPredicateExecutor<Trip> {
}
