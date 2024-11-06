package com.vlad.repository;

import com.vlad.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long>, FilterRequestRepository {

}
