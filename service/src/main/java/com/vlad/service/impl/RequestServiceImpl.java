package com.vlad.service.impl;

import com.querydsl.core.types.Predicate;
import com.vlad.dto.filter.RequestFilterDto;
import com.vlad.dto.request.RequestCreateEditDto;
import com.vlad.dto.request.RequestReadDto;
import com.vlad.entity.QRequest;
import com.vlad.entity.Request;
import com.vlad.exception.api.ApiException;
import com.vlad.exception.error.ErrorCode;
import com.vlad.mapper.RequestCreateEditMapper;
import com.vlad.mapper.RequestReadMapper;
import com.vlad.repository.QPredicate;
import com.vlad.repository.RequestRepository;
import com.vlad.service.RequestService;
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
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestReadMapper requestReadMapper;
    private final RequestCreateEditMapper requestCreateEditMapper;

    @Override
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

    @Override
    public RequestReadDto findById(Long id) {
        log.debug("Finding request by id: {}", id);
        return requestReadMapper.map(getRequestOrThrow(id));
    }

    @Override
    @Transactional
    public RequestReadDto save(RequestCreateEditDto requestCreateEditDto) {
        log.info("Creating new request from {} to {}",
                requestCreateEditDto.getPickupAddress(),
                requestCreateEditDto.getDeliveryAddress());

        Request request = requestCreateEditMapper.map(requestCreateEditDto);
        Request savedRequest = requestRepository.save(request);

        log.info("Request created successfully with id: {}", savedRequest.getId());

        return requestReadMapper.map(savedRequest);
    }

    @Override
    @Transactional
    public RequestReadDto update(Long id, RequestCreateEditDto requestCreateEditDto) {
        log.info("Updating request with id: {}", id);

        Request request = requestRepository.findByIdWithLock(id)
                .orElseThrow(() -> {
                    log.warn("Request not found with id: {}", id);
                    return new ApiException(ErrorCode.REQUEST_NOT_FOUND);
                });

        Request updatedRequest = requestCreateEditMapper.map(requestCreateEditDto, request);
        Request savedRequest = requestRepository.saveAndFlush(updatedRequest);

        log.info("Request updated successfully with id: {}", savedRequest.getId());

        return requestReadMapper.map(savedRequest);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting request with id: {}", id);

        Request request = getRequestOrThrow(id);

        requestRepository.delete(request);
        requestRepository.flush();

        log.info("Request deleted successfully with id: {}", id);
    }

    private Request getRequestOrThrow(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Request not found with id: {}", id);
                    return new ApiException(ErrorCode.REQUEST_NOT_FOUND);
                });
    }
}
