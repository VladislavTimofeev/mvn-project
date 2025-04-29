package com.vlad.service;

import com.vlad.dto.filter.VehicleFilterDto;
import com.vlad.dto.vehicle.VehicleCreateDto;
import com.vlad.dto.vehicle.VehicleEditDto;
import com.vlad.dto.vehicle.VehicleReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface VehicleService {

    Page<VehicleReadDto> findAll(VehicleFilterDto vehicleFilterDto, Pageable pageable);
    Optional<VehicleReadDto> findById(Long id);
    VehicleReadDto save(VehicleCreateDto vehicleCreateDto);
    Optional<VehicleReadDto> update(Long id, VehicleEditDto vehicleEditDto);
    boolean delete(Long id);
}
