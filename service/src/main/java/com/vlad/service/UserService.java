package com.vlad.service;

import com.querydsl.core.types.Predicate;
import com.vlad.dto.user.UserCreateEditDto;
import com.vlad.dto.filter.UserFilterDto;
import com.vlad.dto.user.UserReadDto;
import com.vlad.entity.QUser;
import com.vlad.mapper.UserCreateEditMapper;
import com.vlad.mapper.UserReadMapper;
import com.vlad.repository.QPredicate;
import com.vlad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;

    public Page<UserReadDto> findAll(UserFilterDto userFilterDto, Pageable pageable) {
        Predicate predicate = QPredicate.builder()
                .add(userFilterDto.getUsername(), QUser.user.username::eq)
                .add(userFilterDto.getName(), QUser.user.name::eq)
                .add(userFilterDto.getRole(), QUser.user.role::eq)
                .buildOr();
        return userRepository.findAll(predicate, pageable)
                .map(userReadMapper::map);
    }

    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream()
                .map(userReadMapper::map)
                .toList();
    }

    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userReadMapper::map);
    }

    @Transactional
    public UserReadDto save(UserCreateEditDto userCreateEditDto) {
        return Optional.of(userCreateEditDto)
                .map(userCreateEditMapper::map)
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserCreateEditDto userCreateEditDto) {
        return userRepository.findById(id)
                .map(entity -> userCreateEditMapper.map(userCreateEditDto, entity))
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return userRepository.findById(id)
                .map(entity -> {
                    userRepository.delete(entity);
                    userRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
