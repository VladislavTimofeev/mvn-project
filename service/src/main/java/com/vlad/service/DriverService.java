package com.vlad.service;

import com.vlad.dto.driver.DriverCreateDto;
import com.vlad.dto.driver.DriverEditDto;
import com.vlad.dto.driver.DriverReadDto;

import java.util.List;
import java.util.Optional;

public interface DriverService {
    List<DriverReadDto> findAll();

    Optional<DriverReadDto> findById(Long id);

    DriverReadDto save(DriverCreateDto driverCreateDto);

    Optional<DriverReadDto> update(Long id, DriverEditDto driverEditDto);

    boolean delete(Long id);
}
