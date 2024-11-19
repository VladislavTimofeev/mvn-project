package com.vlad.repository;

import com.vlad.dto.filter.UserFilterDto;
import com.vlad.entity.User;

import java.util.List;

public interface FilterUserRepository {

    List<User> findAllByFilter(UserFilterDto userFilterDto);
}
