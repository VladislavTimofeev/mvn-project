package com.vlad.service;

import com.vlad.dto.driver.DriverCreateDto;
import com.vlad.dto.driver.DriverEditDto;
import com.vlad.dto.driver.DriverReadDto;
import com.vlad.dto.filter.DriverFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DriverService {

    Page<DriverReadDto> findAll(DriverFilterDto driverFilterDto, Pageable pageable);

    List<DriverReadDto> findAll();

    Optional<DriverReadDto> findById(Long id);

    DriverReadDto save(DriverCreateDto driverCreateDto);

    Optional<DriverReadDto> update(Long id, DriverEditDto driverEditDto);

    boolean delete(Long id);
}
