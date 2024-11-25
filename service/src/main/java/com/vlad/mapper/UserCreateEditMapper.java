package com.vlad.mapper;

import com.vlad.dto.user.UserCreateEditDto;
import com.vlad.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User map(UserCreateEditDto fromObject, User toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public User map(UserCreateEditDto object) {
        User user = new User();
        copy(object, user);
        return user;
    }

    private void copy(UserCreateEditDto object, User user) {
        user.setUsername(object.getUsername());
        if (object.getPassword() != null && !object.getPassword().isEmpty()) {
            Optional.of(object.getPassword())
                    .filter(StringUtils::hasText)
                    .map(passwordEncoder::encode)
                    .ifPresent(user::setPassword);
        }
        user.setName(object.getName());
        user.setContactInfo(object.getContactInfo());
        user.setAddress(object.getAddress());
        user.setRole(object.getRole());
    }
}
