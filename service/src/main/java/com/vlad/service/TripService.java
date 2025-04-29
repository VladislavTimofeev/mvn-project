package com.vlad.service;

import com.vlad.dto.filter.TripFilterDto;
import com.vlad.dto.trip.TripCreateDto;
import com.vlad.dto.trip.TripEditDto;
import com.vlad.dto.trip.TripReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TripService {

    Page<TripReadDto> findAll(TripFilterDto tripFilterDto, Pageable pageable);
    Optional<TripReadDto> findById(Long id);
    TripReadDto save(TripCreateDto tripCreateDto);
    Optional<TripReadDto> update(Long id, TripEditDto tripEditDto);
    boolean delete(Long id);
}
