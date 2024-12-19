package com.vlad.repository;

import com.vlad.entity.Request;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RequestRepository extends
        JpaRepository<Request, Long>,
        FilterRequestRepository,
        QuerydslPredicateExecutor<Request>,
        RevisionRepository<Request, Long, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Request r WHERE r.id=:id")
    Optional<Request> findByIdWithLock(@Param("id") Long id);
}
