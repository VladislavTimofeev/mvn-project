package com.vlad.service;

import com.vlad.dto.request.RequestCreateEditDto;
import com.vlad.dto.request.RequestReadDto;
import com.vlad.mapper.RequestCreateEditMapper;
import com.vlad.mapper.RequestReadMapper;
import com.vlad.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestService {

    private final RequestRepository requestRepository;
    private final RequestReadMapper requestReadMapper;
    private final RequestCreateEditMapper requestCreateEditMapper;

    public List<RequestReadDto> findAll() {
        return requestRepository.findAll().stream()
                .map(requestReadMapper::map)
                .toList();
    }

    public Optional<RequestReadDto> findById(Long id) {
        return requestRepository.findById(id)
                .map(requestReadMapper::map);
    }

    @Transactional
    public RequestReadDto save(RequestCreateEditDto requestCreateEditDto) {
        return Optional.of(requestCreateEditDto)
                .map(requestCreateEditMapper::map)
                .map(requestRepository::save)
                .map(requestReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<RequestReadDto> update(Long id, RequestCreateEditDto requestCreateEditDto) {
        return requestRepository.findById(id)
                .map(entity->requestCreateEditMapper.map(requestCreateEditDto,entity))
                .map(requestRepository::saveAndFlush)
                .map(requestReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return requestRepository.findById(id)
                .map(entity -> {
                    requestRepository.delete(entity);
                    requestRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
