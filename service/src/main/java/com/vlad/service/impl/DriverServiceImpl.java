package com.vlad.service.impl;

import com.querydsl.core.types.Predicate;
import com.vlad.dto.driver.DriverCreateDto;
import com.vlad.dto.driver.DriverEditDto;
import com.vlad.dto.driver.DriverReadDto;
import com.vlad.dto.filter.DriverFilterDto;
import com.vlad.entity.QDriver;
import com.vlad.mapper.DriverCreateMapper;
import com.vlad.mapper.DriverEditMapper;
import com.vlad.mapper.DriverReadMapper;
import com.vlad.repository.DriverRepository;
import com.vlad.repository.QPredicate;
import com.vlad.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverReadMapper driverReadMapper;
    private final DriverCreateMapper driverCreateMapper;
    private final DriverEditMapper driverEditMapper;

    @Override
    public Page<DriverReadDto> findAll(DriverFilterDto driverFilterDto, Pageable pageable) {
        Predicate predicate = QPredicate.builder()
                .add(driverFilterDto.getName(), QDriver.driver.name::containsIgnoreCase)
                .add(driverFilterDto.getCarrierId(), QDriver.driver.carrier.id::eq)
                .buildAnd();
        return driverRepository.findAll(predicate, pageable)
                .map(driverReadMapper::map);
    }

    @Override
    public Optional<DriverReadDto> findById(Long id) {
        return driverRepository.findById(id)
                .map(driverReadMapper::map);
    }

    @Override
    @Transactional
    public DriverReadDto save(DriverCreateDto driverCreateDto) {
        return Optional.of(driverCreateDto)
                .map(driverCreateMapper::map)
                .map(driverRepository::save)
                .map(driverReadMapper::map)
                .orElseThrow();
    }

    @Override
    @Transactional
    public Optional<DriverReadDto> update(Long id, DriverEditDto driverEditDto) {
        return driverRepository.findById(id)
                .map(entity -> driverEditMapper.map(driverEditDto, entity))
                .map(driverRepository::saveAndFlush)
                .map(driverReadMapper::map);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return driverRepository.findById(id)
                .map(entity -> {
                    driverRepository.delete(entity);
                    driverRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
