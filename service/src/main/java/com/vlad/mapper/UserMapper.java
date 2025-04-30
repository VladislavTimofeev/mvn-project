package com.vlad.mapper;

import com.vlad.dto.user.UserCreateEditDto;
import com.vlad.dto.user.UserReadDto;
import com.vlad.entity.Role;
import com.vlad.entity.User;
import lombok.RequiredArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
@RequiredArgsConstructor
public abstract class UserMapper {

    protected PasswordEncoder passwordEncoder;

    public abstract UserReadDto toDto(User user);

    @Mapping(target = "password", ignore = true)
    public abstract User toEntity(UserCreateEditDto dto);

    @Mapping(target = "password", ignore = true)
    public abstract void updateEntityFromDto(UserCreateEditDto dto, @MappingTarget User user);

    @AfterMapping
    protected void encodePassword(UserCreateEditDto dto, @MappingTarget User user) {
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
    }

    @AfterMapping
    protected void setDefaultRole(UserCreateEditDto dto, @MappingTarget User user) {
        if (user.getRole() == null) {
            user.setRole(Role.GUEST);
        } else if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
    }
}
