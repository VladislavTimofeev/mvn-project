package com.vlad.service.impl;

import com.querydsl.core.types.Predicate;
import com.vlad.dto.filter.RequestFilterDto;
import com.vlad.dto.request.RequestCreateEditDto;
import com.vlad.dto.request.RequestReadDto;
import com.vlad.entity.QRequest;
import com.vlad.mapper.RequestMapper;
import com.vlad.repository.QPredicate;
import com.vlad.repository.RequestRepository;
import com.vlad.repository.UserRepository;
import com.vlad.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    @Override
    public Page<RequestReadDto> findAll(RequestFilterDto requestFilterDto, Pageable pageable) {
        Predicate predicate = QPredicate.builder()
                .add(requestFilterDto.getStatus(), QRequest.request.status::eq)
                .add(requestFilterDto.getPickupAddress(), QRequest.request.pickupAddress::eq)
                .add(requestFilterDto.getDeliveryAddress(), QRequest.request.deliveryAddress::eq)
                .add(requestFilterDto.getCreationDate(), QRequest.request.creationDate::eq)
                .buildAnd();
        return requestRepository.findAll(predicate, pageable)
                .map(requestMapper::toDto);
    }

    @Override
    public Optional<RequestReadDto> findById(Long id) {
        return requestRepository.findById(id)
                .map(requestMapper::toDto);
    }

    @Override
    @Transactional
    public RequestReadDto save(RequestCreateEditDto requestCreateEditDto) {
        return Optional.of(requestCreateEditDto)
                .map(requestMapper::toEntity)
                .map(requestRepository::save)
                .map(requestMapper::toDto)
                .orElseThrow();
    }

    @Override
    @Transactional
    public Optional<RequestReadDto> update(Long id, RequestCreateEditDto requestCreateEditDto) {
        return requestRepository.findByIdWithLock(id)
                .map(existingRequest -> {
                    requestMapper.updateEntityFromDto(requestCreateEditDto, existingRequest, userRepository);
                    return requestRepository.saveAndFlush(existingRequest);
                })
                .map(requestMapper::toDto);
    }

    @Override
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
