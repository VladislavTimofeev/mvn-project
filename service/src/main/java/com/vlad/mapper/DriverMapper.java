package com.vlad.mapper;

import com.vlad.dto.driver.DriverCreateDto;
import com.vlad.dto.driver.DriverEditDto;
import com.vlad.dto.driver.DriverReadDto;
import com.vlad.entity.Driver;
import com.vlad.entity.User;
import com.vlad.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DriverMapper {

    DriverReadDto toDto(Driver driver);

    @Mapping(target = "carrier", source = "carrierId", qualifiedByName = "mapCarrierIdToUser")
    Driver toEntity(DriverCreateDto dto);

    @Mapping(target = "carrier", source = "carrierId", qualifiedByName = "mapCarrierIdToUser")
    void updateEntityFromDto(DriverEditDto dto, @MappingTarget Driver driver, UserRepository userRepository);

    @Named("mapCarrierIdToUser")
    default User mapCarrierIdToUser(Long id, UserRepository userRepository) {
        return id == null ? null : userRepository.findById(id).orElse(null);
    }
}
