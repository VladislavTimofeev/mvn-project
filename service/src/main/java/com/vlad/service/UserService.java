package com.vlad.service;

import com.vlad.dto.filter.UserFilterDto;
import com.vlad.dto.user.UserCreateEditDto;
import com.vlad.dto.user.UserReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserReadDto> findAll(UserFilterDto userFilterDto, Pageable pageable);

    UserReadDto findById(Long id);

    UserReadDto save(UserCreateEditDto userCreateEditDto);

    UserReadDto update(Long id, UserCreateEditDto userCreateEditDto);

    void delete(Long id);
}
