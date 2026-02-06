package com.vlad.service.impl;

import com.querydsl.core.types.Predicate;
import com.vlad.dto.user.UserCreateEditDto;
import com.vlad.dto.filter.UserFilterDto;
import com.vlad.dto.user.UserReadDto;
import com.vlad.entity.QUser;
import com.vlad.entity.User;
import com.vlad.exception.api.ApiException;
import com.vlad.exception.error.ErrorCode;
import com.vlad.mapper.UserCreateEditMapper;
import com.vlad.mapper.UserReadMapper;
import com.vlad.repository.QPredicate;
import com.vlad.repository.UserRepository;
import com.vlad.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;

    @Override
    public Page<UserReadDto> findAll(UserFilterDto userFilterDto, Pageable pageable) {
        Predicate predicate = QPredicate.builder()
                .add(userFilterDto.getUsername(), QUser.user.username::eq)
                .add(userFilterDto.getName(), QUser.user.name::containsIgnoreCase)
                .add(userFilterDto.getRole(), QUser.user.role::eq)
                .buildAnd();
        return userRepository.findAll(predicate, pageable)
                .map(userReadMapper::map);
    }

    @Override
    public UserReadDto findById(Long id) {
        log.debug("Finding user by id: {}", id);
        return userReadMapper.map(getUserOrThrow(id));
    }

    @Override
    @Transactional
    public UserReadDto save(UserCreateEditDto userCreateEditDto) {
        log.info("Creating new user: {}", userCreateEditDto.getUsername());

        if (userRepository.existsByUsername(userCreateEditDto.getUsername())) {
            log.warn("User already exists with username: {}", userCreateEditDto.getUsername());
            throw new ApiException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = userCreateEditMapper.map(userCreateEditDto);
        User savedUser = userRepository.save(user);

        log.info("User created successfully with id: {}", savedUser.getId());

        return userReadMapper.map(savedUser);
    }

    @Override
    @Transactional
    public UserReadDto update(Long id, UserCreateEditDto userCreateEditDto) {
        log.info("Updating user with id: {}", id);

        User user = getUserOrThrow(id);

        if (!user.getUsername().equals(userCreateEditDto.getUsername())
                && userRepository.existsByUsername(userCreateEditDto.getUsername())) {
            log.warn("Username already taken: {}", userCreateEditDto.getUsername());
            throw new ApiException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User updatedUser = userCreateEditMapper.map(userCreateEditDto, user);
        User savedUser = userRepository.saveAndFlush(updatedUser);
        log.info("User updated successfully with id: {}", savedUser.getId());

        return userReadMapper.map(savedUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting user with id: {}", id);

        User user = getUserOrThrow(id);

        userRepository.delete(user);
        userRepository.flush();

        log.info("User deleted successfully with id: {}", id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                ))
                .orElseThrow(() -> {
                    log.warn("User not found with username: {}", username);
                    return new UsernameNotFoundException("Failed to retrieve user: " + username);
                });
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with id: {}", id);
                    return new ApiException(ErrorCode.USER_NOT_FOUND);
                });
    }
}
