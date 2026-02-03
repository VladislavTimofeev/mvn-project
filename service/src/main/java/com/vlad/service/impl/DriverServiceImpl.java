package com.vlad.service.impl;

import com.querydsl.core.types.Predicate;
import com.vlad.dto.driver.DriverCreateDto;
import com.vlad.dto.driver.DriverEditDto;
import com.vlad.dto.driver.DriverReadDto;
import com.vlad.dto.filter.DriverFilterDto;
import com.vlad.entity.Driver;
import com.vlad.entity.QDriver;
import com.vlad.exception.api.ApiException;
import com.vlad.exception.error.ErrorCode;
import com.vlad.mapper.DriverCreateMapper;
import com.vlad.mapper.DriverEditMapper;
import com.vlad.mapper.DriverReadMapper;
import com.vlad.repository.DriverRepository;
import com.vlad.repository.QPredicate;
import com.vlad.service.DriverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
    public List<DriverReadDto> findAll() {
        return driverRepository.findAll().stream()
                .map(driverReadMapper::map)
                .toList();
    }

    @Override
    public DriverReadDto findById(Long id) {
        log.debug("Finding driver by id: {}", id);
        return driverReadMapper.map(getDriverOrThrow(id));
    }

    @Override
    @Transactional
    public DriverReadDto save(DriverCreateDto driverCreateDto) {
        log.info("Creating new driver: {}", driverCreateDto.getName());

        Driver driver = driverCreateMapper.map(driverCreateDto);
        Driver savedDriver = driverRepository.save(driver);

        log.info("Driver created successfully with id: {}", savedDriver.getId());

        return driverReadMapper.map(savedDriver);
    }

    @Override
    @Transactional
    public DriverReadDto update(Long id, DriverEditDto driverEditDto) {
        log.info("Updating driver with id: {}", id);

        Driver driver = getDriverOrThrow(id);

        Driver updatedDriver = driverEditMapper.map(driverEditDto, driver);
        Driver savedDriver = driverRepository.saveAndFlush(updatedDriver);

        log.info("Driver updated successfully with id: {}", savedDriver.getId());

        return driverReadMapper.map(savedDriver);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting driver with id: {}", id);

        Driver driver = getDriverOrThrow(id);

        driverRepository.delete(driver);
        driverRepository.flush();

        log.info("Driver deleted successfully with id: {}", id);
    }

    private Driver getDriverOrThrow(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Driver not found with id: {}", id);
                    return new ApiException(ErrorCode.DRIVER_NOT_FOUND);
                });
    }
}
