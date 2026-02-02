package com.vlad.http.rest;

import com.vlad.dto.PageResponse;
import com.vlad.dto.filter.TripFilterDto;
import com.vlad.dto.trip.TripCreateDto;
import com.vlad.dto.trip.TripEditDto;
import com.vlad.dto.trip.TripReadDto;
import com.vlad.exception.api.ApiException;
import com.vlad.exception.error.ErrorCode;
import com.vlad.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/trips")
@RequiredArgsConstructor
public class TripRestController {

    private final TripService tripService;

    @GetMapping
    public PageResponse<TripReadDto> findAll(TripFilterDto tripFilterDto, Pageable pageable) {
        return PageResponse.of(tripService.findAll(tripFilterDto,pageable));
    }

    @GetMapping("/{id}")
    public TripReadDto findById(@PathVariable Long id) {
        return tripService.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.TRIP_NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TripReadDto save(@Valid @RequestBody TripCreateDto tripCreateDto) {
        return tripService.save(tripCreateDto);
    }

    @PutMapping("/{id}")
    public TripReadDto update(@PathVariable Long id, @Valid @RequestBody TripEditDto tripEditDto) {
        return tripService.update(id, tripEditDto)
                .orElseThrow(() -> new ApiException(ErrorCode.TRIP_NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!tripService.delete(id)) {
            throw new ApiException(ErrorCode.TRIP_NOT_FOUND);
        }
    }
}
