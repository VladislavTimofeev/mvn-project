package com.vlad.service;

import com.querydsl.core.types.Predicate;
import com.vlad.dto.filter.RequestFilterDto;
import com.vlad.dto.request.RequestCreateEditDto;
import com.vlad.dto.request.RequestReadDto;
import com.vlad.entity.QRequest;
import com.vlad.mapper.RequestCreateEditMapper;
import com.vlad.mapper.RequestReadMapper;
import com.vlad.repository.QPredicate;
import com.vlad.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestService {

    private final RequestRepository requestRepository;
    private final RequestReadMapper requestReadMapper;
    private final RequestCreateEditMapper requestCreateEditMapper;

    public Page<RequestReadDto> findAll(RequestFilterDto requestFilterDto, Pageable pageable) {
        Predicate predicate = QPredicate.builder()
                .add(requestFilterDto.getStatus(), QRequest.request.status::eq)
                .add(requestFilterDto.getPickupAddress(), QRequest.request.pickupAddress::eq)
                .add(requestFilterDto.getDeliveryAddress(), QRequest.request.deliveryAddress::eq)
                .add(requestFilterDto.getCreationDate(), QRequest.request.creationDate::eq)
                .buildAnd();
        return requestRepository.findAll(predicate, pageable)
                .map(requestReadMapper::map);
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
                .map(entity -> requestCreateEditMapper.map(requestCreateEditDto, entity))
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
