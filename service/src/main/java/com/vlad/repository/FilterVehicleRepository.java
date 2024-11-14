package com.vlad.repository;

import com.vlad.dto.filter.VehicleFilterDto;
import com.vlad.entity.Vehicle;

import java.util.List;

public interface FilterVehicleRepository {

    List<Vehicle> findAllByFilter(VehicleFilterDto vehicleFilterDto);
}
