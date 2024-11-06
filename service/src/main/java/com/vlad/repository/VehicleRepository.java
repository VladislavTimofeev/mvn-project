package com.vlad.repository;

import com.vlad.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long>, FilterVehicleRepository {

}
