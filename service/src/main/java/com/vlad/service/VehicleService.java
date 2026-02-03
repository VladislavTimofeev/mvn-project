package com.vlad.service;

import com.vlad.dto.filter.VehicleFilterDto;
import com.vlad.dto.vehicle.VehicleCreateDto;
import com.vlad.dto.vehicle.VehicleEditDto;
import com.vlad.dto.vehicle.VehicleReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VehicleService {

    Page<VehicleReadDto> findAll(VehicleFilterDto vehicleFilterDto, Pageable pageable);

    VehicleReadDto findById(Long id);

    VehicleReadDto save(VehicleCreateDto vehicleCreateDto);

    VehicleReadDto update(Long id, VehicleEditDto vehicleEditDto);

    void delete(Long id);
}
