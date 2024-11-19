package com.vlad.http.rest;

import com.vlad.dto.request.RequestCreateEditDto;
import com.vlad.dto.request.RequestReadDto;
import com.vlad.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v2/requests")
@RequiredArgsConstructor
public class RequestRestController {

    private final RequestService requestService;

    @GetMapping
    public List<RequestReadDto> findAll() {
        return requestService.findAll();
    }

    @GetMapping("/{id}")
    public RequestReadDto findById(@PathVariable("id") Long id) {
        return requestService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestReadDto create(@RequestBody RequestCreateEditDto requestCreateEditDto) {
        return requestService.save(requestCreateEditDto);
    }

    @PutMapping("/{id}")
    public RequestReadDto update(@PathVariable("id") Long id, @RequestBody RequestCreateEditDto requestCreateEditDto) {
        return requestService.update(id, requestCreateEditDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        if (!requestService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
