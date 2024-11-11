package com.vlad.mapper;

import com.vlad.dto.user.UserCreateEditDto;
import com.vlad.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User> {

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

    public void copy(UserCreateEditDto object, User user) {
        user.setUsername(object.getUsername());
        user.setPassword(object.getPassword());
        user.setName(object.getName());
        user.setContactInfo(object.getContactInfo());
        user.setAddress(object.getAddress());
        user.setRole(object.getRole());
    }

}
