package com.vlad.http.rest;

import com.vlad.dto.PageResponse;
import com.vlad.dto.filter.VehicleFilterDto;
import com.vlad.dto.vehicle.VehicleCreateDto;
import com.vlad.dto.vehicle.VehicleEditDto;
import com.vlad.dto.vehicle.VehicleReadDto;
import com.vlad.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v2/vehicles")
@RequiredArgsConstructor
public class VehicleRestController {

    private final VehicleService vehicleService;

    @GetMapping
    public PageResponse<VehicleReadDto> findAll(VehicleFilterDto filter, Pageable pageable) {
        Page<VehicleReadDto> page = vehicleService.findAll(filter, pageable);
        return PageResponse.of(page);
    }

    @GetMapping("/{id}")
    public VehicleReadDto findById(@PathVariable Long id) {
        return vehicleService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleReadDto save(@Valid @RequestBody VehicleCreateDto vehicleCreateDto) {
        return vehicleService.save(vehicleCreateDto);
    }

    @PutMapping("/{id}")
    public VehicleReadDto update(@PathVariable Long id, @Valid @RequestBody VehicleEditDto vehicleEditDto) {
        return vehicleService.update(id, vehicleEditDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!vehicleService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
