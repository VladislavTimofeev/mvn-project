package com.vlad.dto.user;

import com.vlad.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class UserCreateEditDto {

    @Email
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    private String contactInfo;
    private String address;
    private Role role;
}
