package com.vlad.service;

import com.vlad.dto.filter.TripFilterDto;
import com.vlad.dto.trip.TripCreateDto;
import com.vlad.dto.trip.TripEditDto;
import com.vlad.dto.trip.TripReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TripService {

    Page<TripReadDto> findAll(TripFilterDto tripFilterDto, Pageable pageable);

    TripReadDto findById(Long id);

    TripReadDto save(TripCreateDto tripCreateDto);

    TripReadDto update(Long id, TripEditDto tripEditDto);

    void delete(Long id);
}
