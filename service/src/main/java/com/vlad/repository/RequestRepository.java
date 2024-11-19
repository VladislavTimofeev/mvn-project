package com.vlad.repository;

import com.vlad.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface RequestRepository extends
        JpaRepository<Request, Long>,
        FilterRequestRepository,
        RevisionRepository<Request, Long, Long> {
}
