package com.vlad.repository;

import com.vlad.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface VehicleRepository extends
        JpaRepository<Vehicle, Long>,
        FilterVehicleRepository,
        QuerydslPredicateExecutor<Vehicle> {
    boolean existsByLicensePlate(String licensePlate);
}
