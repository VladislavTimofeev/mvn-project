package com.vlad.mapper;

import com.vlad.dto.user.UserReadDto;
import com.vlad.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {

    @Override
    public UserReadDto map(User object) {
        return new UserReadDto(
                object.getId(),
                object.getUsername(),
                object.getName(),
                object.getContactInfo(),
                object.getAddress(),
                object.getRole()
        );
    }
}
