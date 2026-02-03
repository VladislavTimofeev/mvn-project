package com.vlad.http.rest;

import com.vlad.dto.PageResponse;
import com.vlad.dto.filter.RequestFilterDto;
import com.vlad.dto.request.RequestCreateEditDto;
import com.vlad.dto.request.RequestReadDto;
import com.vlad.service.RequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/requests")
@RequiredArgsConstructor
public class RequestRestController {

    private final RequestService requestService;

    @GetMapping
    public PageResponse<RequestReadDto> findAll(RequestFilterDto filter, Pageable pageable) {
        return PageResponse.of(requestService.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public RequestReadDto findById(@PathVariable Long id) {
        return requestService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestReadDto save(@Valid @RequestBody RequestCreateEditDto request) {
        return requestService.save(request);
    }

    @PutMapping("/{id}")
    public RequestReadDto update(@PathVariable Long id,
                                 @Valid @RequestBody RequestCreateEditDto request) {
        return requestService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        requestService.delete(id);
    }
}
