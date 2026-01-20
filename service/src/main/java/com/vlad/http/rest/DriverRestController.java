package com.vlad.http.rest;

import com.vlad.dto.PageResponse;
import com.vlad.dto.driver.DriverCreateDto;
import com.vlad.dto.driver.DriverEditDto;
import com.vlad.dto.driver.DriverReadDto;
import com.vlad.dto.filter.DriverFilterDto;
import com.vlad.exception.api.ApiException;
import com.vlad.exception.error.ErrorCode;
import com.vlad.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/drivers")
@RequiredArgsConstructor
public class DriverRestController {

    private final DriverService driverService;

    @GetMapping
    public PageResponse<DriverReadDto> findAll(DriverFilterDto driverFilterDto, Pageable pageable) {
        return PageResponse.of(driverService.findAll(driverFilterDto, pageable));
    }

    @GetMapping("/{id}")
    public DriverReadDto findById(@PathVariable Long id) {
        return driverService.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.DRIVER_NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverReadDto save(@Valid @RequestBody DriverCreateDto driverCreateDto) {
        return driverService.save(driverCreateDto);
    }

    @PutMapping("/{id}")
    public DriverReadDto update(@PathVariable Long id, @Valid @RequestBody DriverEditDto driverEditDto) {
        return driverService.update(id, driverEditDto)
                .orElseThrow(() -> new ApiException(ErrorCode.DRIVER_NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!driverService.delete(id)) {
            throw new ApiException(ErrorCode.DRIVER_NOT_FOUND);
        }
    }
}
