package com.vlad.service.impl;

import com.querydsl.core.types.Predicate;
import com.vlad.dto.filter.TripFilterDto;
import com.vlad.dto.trip.TripCreateDto;
import com.vlad.dto.trip.TripEditDto;
import com.vlad.dto.trip.TripReadDto;
import com.vlad.entity.QTrip;
import com.vlad.entity.Trip;
import com.vlad.exception.api.ApiException;
import com.vlad.exception.error.ErrorCode;
import com.vlad.mapper.TripCreateMapper;
import com.vlad.mapper.TripEditMapper;
import com.vlad.mapper.TripReadMapper;
import com.vlad.repository.QPredicate;
import com.vlad.repository.TripRepository;
import com.vlad.service.TripService;
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
    public TripReadDto findById(Long id) {
        log.debug("Finding trip by id {}", id);
        return tripReadMapper.map(getTripOrThrow(id));
    }

    @Override
    @Transactional
    public TripReadDto save(TripCreateDto tripCreateDto) {
        log.info("Saving trip with request id {}", tripCreateDto.getRequestId());

        Trip trip = tripCreateMapper.map(tripCreateDto);
        Trip savedTrip = tripRepository.save(trip);

        log.info("Saved trip with id {}", savedTrip.getId());

        return tripReadMapper.map(savedTrip);
    }

    @Override
    @Transactional
    public TripReadDto update(Long id, TripEditDto tripEditDto) {
        log.info("Updating trip with id {}", id);

        Trip trip = getTripOrThrow(id);

        Trip updatedTrip = tripEditMapper.map(tripEditDto, trip);
        Trip savedTrip = tripRepository.saveAndFlush(updatedTrip);

        log.info("Updated trip with id {}", savedTrip.getId());

        return tripReadMapper.map(savedTrip);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting trip with id {}", id);

        Trip trip = getTripOrThrow(id);

        tripRepository.delete(trip);
        tripRepository.flush();

        log.info("Deleted trip with id {}", id);
    }

    private Trip getTripOrThrow(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Trip with id {} not found", id);
                    return new ApiException(ErrorCode.TRIP_NOT_FOUND);
                });
    }
}
