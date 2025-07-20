package com.vlad.service.impl;

import com.querydsl.core.types.Predicate;
import com.vlad.dto.filter.TripFilterDto;
import com.vlad.dto.trip.TripCreateDto;
import com.vlad.dto.trip.TripEditDto;
import com.vlad.dto.trip.TripReadDto;
import com.vlad.entity.QTrip;
import com.vlad.mapper.TripCreateMapper;
import com.vlad.mapper.TripEditMapper;
import com.vlad.mapper.TripReadMapper;
import com.vlad.repository.QPredicate;
import com.vlad.repository.TripRepository;
import com.vlad.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final TripReadMapper tripReadMapper;
    private final TripCreateMapper tripCreateMapper;
    private final TripEditMapper tripEditMapper;

    @Override
    public Page<TripReadDto> findAll(TripFilterDto tripFilterDto, Pageable pageable) {
        Predicate predicate = QPredicate.builder()
                .add(tripFilterDto.getStatus(), QTrip.trip.status::eq)
                .buildAnd();
        return tripRepository.findAll(predicate, pageable)
                .map(tripReadMapper::map);
    }

    @Override
    public Optional<TripReadDto> findById(Long id) {
        return tripRepository.findById(id)
                .map(tripReadMapper::map);
    }

    @Override
    @Transactional
    public TripReadDto save(TripCreateDto tripCreateDto) {
        return Optional.of(tripCreateDto)
                .map(tripCreateMapper::map)
                .map(tripRepository::save)
                .map(tripReadMapper::map)
                .orElseThrow();
    }

    @Override
    @Transactional
    public Optional<TripReadDto> update(Long id, TripEditDto tripEditDto) {
        return tripRepository.findById(id)
                .map(entity -> tripEditMapper.map(tripEditDto, entity))
                .map(tripRepository::saveAndFlush)
                .map(tripReadMapper::map);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return tripRepository.findById(id)
                .map(entity -> {
                    tripRepository.delete(entity);
                    tripRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
