package com.vlad.http.rest;

import com.vlad.dto.PageResponse;
import com.vlad.dto.driver.DriverCreateDto;
import com.vlad.dto.driver.DriverEditDto;
import com.vlad.dto.driver.DriverReadDto;
import com.vlad.dto.filter.DriverFilterDto;
import com.vlad.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/drivers")
@RequiredArgsConstructor
public class DriverRestController {

    private final DriverService driverService;

    @GetMapping
    public PageResponse<DriverReadDto> findAll(DriverFilterDto driverFilterDto, Pageable pageable) {
        return PageResponse.of(driverService.findAll(driverFilterDto, pageable));
    }

    @GetMapping("/list")
    public List<DriverReadDto> findAll() {
        return driverService.findAll();
    }

    @GetMapping("/{id}")
    public DriverReadDto findById(@PathVariable Long id) {
        return driverService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverReadDto save(@Valid @RequestBody DriverCreateDto driverCreateDto) {
        return driverService.save(driverCreateDto);
    }

    @PutMapping("/{id}")
    public DriverReadDto update(@PathVariable Long id,
                                @Valid @RequestBody DriverEditDto driverEditDto) {
        return driverService.update(id, driverEditDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        driverService.delete(id);
    }
}
