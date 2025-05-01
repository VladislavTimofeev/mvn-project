package com.vlad.mapper;

import com.vlad.dto.user.UserCreateEditDto;
import com.vlad.dto.user.UserReadDto;
import com.vlad.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserReadDto toDto(User user);

    @Mapping(target = "password", ignore = true)
    User toEntity(UserCreateEditDto dto);

    @Mapping(target = "password", ignore = true)
    void updateEntityFromDto(UserCreateEditDto dto, @MappingTarget User user);
}
