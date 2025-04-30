package com.vlad.mapper;

import com.vlad.dto.driver.DriverCreateDto;
import com.vlad.dto.driver.DriverEditDto;
import com.vlad.dto.driver.DriverReadDto;
import com.vlad.entity.Driver;
import com.vlad.entity.User;
import com.vlad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
@RequiredArgsConstructor
public abstract class DriverMapper {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public abstract DriverReadDto toDto(Driver driver);

    @Mapping(target = "carrier", source = "carrierId", qualifiedByName = "mapCarrierIdToUser")
    public abstract Driver toEntity(DriverCreateDto dto);

    @Mapping(target = "carrier", source = "carrierId", qualifiedByName = "mapCarrierIdToUser")
    public abstract void updateEntityFromDto(DriverEditDto dto, @MappingTarget Driver driver);

    @Named("mapCarrierIdToUser")
    protected User mapCarrierIdToUser(Long id) {
        return id == null ? null : userRepository.findById(id).orElse(null);
    }
}
