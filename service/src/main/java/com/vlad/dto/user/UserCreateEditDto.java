package com.vlad.dto.user;

import com.vlad.entity.Role;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@FieldNameConstants
public class UserCreateEditDto {
    String username;
    String password;
    String name;
    String contactInfo;
    String address;
    Role role;
}
