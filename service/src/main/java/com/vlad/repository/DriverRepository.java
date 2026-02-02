package com.vlad.repository;

import com.vlad.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DriverRepository extends
        JpaRepository<Driver, Long>,
        QuerydslPredicateExecutor<Driver> {
}
