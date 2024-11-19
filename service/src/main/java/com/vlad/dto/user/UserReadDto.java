package com.vlad.dto.user;

import com.vlad.entity.Role;
import lombok.Value;

@Value
public class UserReadDto {
    Long id;
    String username;
    String name;
    String contactInfo;
    String address;
    Role role;
}
