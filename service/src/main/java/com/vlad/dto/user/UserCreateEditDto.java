package com.vlad.dto.user;

import com.vlad.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@FieldNameConstants
public class UserCreateEditDto {

    @Email
    String username;

    @NotBlank
    String password;

    @NotBlank
    String name;

    String contactInfo;
    String address;
    Role role;
}
