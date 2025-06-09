package com.vlad.service.impl;

import com.querydsl.core.types.Predicate;
import com.vlad.dto.filter.VehicleFilterDto;
import com.vlad.dto.vehicle.VehicleCreateDto;
import com.vlad.dto.vehicle.VehicleEditDto;
import com.vlad.dto.vehicle.VehicleReadDto;
import com.vlad.entity.QVehicle;
import com.vlad.mapper.VehicleMapper;
import com.vlad.repository.QPredicate;
import com.vlad.repository.VehicleRepository;
import com.vlad.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

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
                .map(vehicleMapper::toDto);
    }

    @Override
    public Optional<VehicleReadDto> findById(Long id) {
        return vehicleRepository.findById(id)
                .map(vehicleMapper::toDto);
    }

    @Override
    @Transactional
    public VehicleReadDto save(VehicleCreateDto vehicleCreateDto) {
        return Optional.of(vehicleCreateDto)
                .map(vehicleMapper::toEntity)
                .map(vehicleRepository::save)
                .map(vehicleMapper::toDto)
                .orElseThrow();
    }

    @Override
    @Transactional
    public Optional<VehicleReadDto> update(Long id, VehicleEditDto vehicleEditDto) {
        return vehicleRepository.findById(id)
                .map(existingVehicle -> {
                    vehicleMapper.updateEntityFromDto(vehicleEditDto, existingVehicle);
                    return vehicleRepository.saveAndFlush(existingVehicle);
                })
                .map(vehicleMapper::toDto);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return vehicleRepository.findById(id)
                .map(entity -> {
                    vehicleRepository.delete(entity);
                    vehicleRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
