package com.vlad.service;

import com.vlad.dto.filter.RequestFilterDto;
import com.vlad.dto.request.RequestCreateEditDto;
import com.vlad.dto.request.RequestReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestService {

    Page<RequestReadDto> findAll(RequestFilterDto requestFilterDto, Pageable pageable);

    RequestReadDto findById(Long id);

    RequestReadDto save(RequestCreateEditDto requestCreateEditDto);

    RequestReadDto update(Long id, RequestCreateEditDto requestCreateEditDto);

    void delete(Long id);
}
