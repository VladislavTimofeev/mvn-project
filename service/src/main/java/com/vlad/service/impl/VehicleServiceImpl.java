package com.vlad.service.impl;

import com.querydsl.core.types.Predicate;
import com.vlad.dto.filter.VehicleFilterDto;
import com.vlad.dto.vehicle.VehicleCreateDto;
import com.vlad.dto.vehicle.VehicleEditDto;
import com.vlad.dto.vehicle.VehicleReadDto;
import com.vlad.entity.QVehicle;
import com.vlad.entity.Vehicle;
import com.vlad.exception.api.ApiException;
import com.vlad.exception.error.ErrorCode;
import com.vlad.mapper.VehicleCreateMapper;
import com.vlad.mapper.VehicleEditMapper;
import com.vlad.mapper.VehicleReadMapper;
import com.vlad.repository.QPredicate;
import com.vlad.repository.VehicleRepository;
import com.vlad.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleReadMapper vehicleReadMapper;
    private final VehicleCreateMapper vehicleCreateMapper;
    private final VehicleEditMapper vehicleEditMapper;

    @Override
    public Page<VehicleReadDto> findAll(VehicleFilterDto vehicleFilterDto, Pageable pageable) {
        Predicate predicate = QPredicate.builder()
                .add(vehicleFilterDto.getLicensePlate(), QVehicle.vehicle.licensePlate::eq)
                .add(vehicleFilterDto.getCapacity(), QVehicle.vehicle.capacity::eq)
                .add(vehicleFilterDto.getPalletCapacity(), QVehicle.vehicle.palletCapacity::eq)
                .add(vehicleFilterDto.getRefrigerated(), QVehicle.vehicle.refrigerated::eq)
                .add(vehicleFilterDto.getModel(), QVehicle.vehicle.model::eq)
                .buildAnd();
        return vehicleRepository.findAll(predicate, pageable)
                .map(vehicleReadMapper::map);
    }

    @Override
    public VehicleReadDto findById(Long id) {
        log.debug("Finding vehicle by id: {}", id);
        return vehicleReadMapper.map(getVehicleOrThrow(id));
    }

    @Override
    @Transactional
    public VehicleReadDto save(VehicleCreateDto vehicleCreateDto) {
        log.info("Creating new vehicle: {}", vehicleCreateDto.getModel());

        Vehicle vehicle = vehicleCreateMapper.map(vehicleCreateDto);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        log.info("Vehicle created successfully with id: {}", savedVehicle.getId());

        return vehicleReadMapper.map(savedVehicle);
    }

    @Override
    @Transactional
    public VehicleReadDto update(Long id, VehicleEditDto vehicleEditDto) {
        log.info("Updating vehicle with id: {}", id);

        Vehicle vehicle = getVehicleOrThrow(id);

        if (!vehicle.getLicensePlate().equals(vehicleEditDto.getLicensePlate())
                && vehicleRepository.existsByLicensePlate(vehicleEditDto.getLicensePlate())) {
            log.warn("License plate already taken: {}", vehicleEditDto.getLicensePlate());
            throw new ApiException(ErrorCode.VEHICLE_ALREADY_EXISTS);
        }

        Vehicle updatedVehicle = vehicleEditMapper.map(vehicleEditDto, vehicle);
        Vehicle savedVehicle = vehicleRepository.saveAndFlush(updatedVehicle);

        log.info("Vehicle updated successfully with id: {}", savedVehicle.getId());

        return vehicleReadMapper.map(savedVehicle);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting vehicle with id: {}", id);

        Vehicle vehicle = getVehicleOrThrow(id);

        vehicleRepository.delete(vehicle);
        vehicleRepository.flush();

        log.info("Vehicle deleted successfully with id: {}", id);
    }

    private Vehicle getVehicleOrThrow(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Vehicle not found with id: {}", id);
                    return new ApiException(ErrorCode.VEHICLE_NOT_FOUND);
                });
    }
}
