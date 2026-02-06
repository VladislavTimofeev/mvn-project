package com.vlad.service;

import com.vlad.dto.driver.DriverCreateDto;
import com.vlad.dto.driver.DriverEditDto;
import com.vlad.dto.driver.DriverReadDto;
import com.vlad.dto.filter.DriverFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DriverService {

    Page<DriverReadDto> findAll(DriverFilterDto driverFilterDto, Pageable pageable);

    List<DriverReadDto> findAll();

    DriverReadDto findById(Long id);

    DriverReadDto save(DriverCreateDto driverCreateDto);

    DriverReadDto update(Long id, DriverEditDto driverEditDto);

    void delete(Long id);
}
