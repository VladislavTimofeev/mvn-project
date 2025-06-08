package com.vlad.service.impl;

import com.querydsl.core.types.Predicate;
import com.vlad.dto.user.UserCreateEditDto;
import com.vlad.dto.filter.UserFilterDto;
import com.vlad.dto.user.UserReadDto;
import com.vlad.entity.QUser;
import com.vlad.mapper.UserMapper;
import com.vlad.repository.QPredicate;
import com.vlad.repository.UserRepository;
import com.vlad.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Page<UserReadDto> findAll(UserFilterDto userFilterDto, Pageable pageable) {
        Predicate predicate = QPredicate.builder()
                .add(userFilterDto.getUsername(), QUser.user.username::eq)
                .add(userFilterDto.getName(), QUser.user.name::containsIgnoreCase)
                .add(userFilterDto.getRole(), QUser.user.role::eq)
                .buildAnd();
        return userRepository.findAll(predicate, pageable)
                .map(userMapper::toDto);
    }

    @Override
    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional
    public UserReadDto save(UserCreateEditDto userCreateEditDto) {
        return Optional.of(userCreateEditDto)
                .map(user -> userMapper.toEntity(userCreateEditDto))
                .map(userRepository::save)
                .map(userMapper::toDto)
                .orElseThrow();
    }

    @Override
    @Transactional
    public Optional<UserReadDto> update(Long id, UserCreateEditDto userCreateEditDto) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    userMapper.updateEntityFromDto(userCreateEditDto, existingUser);
                    return userRepository.saveAndFlush(existingUser);
                })
                .map(userMapper::toDto);
    }

    @Override
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }
}
