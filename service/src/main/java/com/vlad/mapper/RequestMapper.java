package com.vlad.mapper;

import com.vlad.dto.request.RequestCreateEditDto;
import com.vlad.dto.request.RequestReadDto;
import com.vlad.entity.Request;
import com.vlad.entity.User;
import com.vlad.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface RequestMapper {

    RequestReadDto toDto(Request request);

    @Mapping(target = "customer", source = "customerId", qualifiedByName = "mapCustomerIdToUser")
    @Mapping(target = "carrier", source = "carrierId", qualifiedByName = "mapCarrierIdToUser")
    Request toEntity(RequestCreateEditDto dto);

    @Mapping(target = "customer", source = "customerId", qualifiedByName = "mapCustomerIdToUser")
    @Mapping(target = "carrier", source = "carrierId", qualifiedByName = "mapCarrierIdToUser")
    void updateEntityFromDto(RequestCreateEditDto dto, @MappingTarget Request request, UserRepository userRepository);

    @Named("mapCustomerIdToUser")
    default User mapCustomerIdToUser(Long id, UserRepository userRepository) {
        return id == null ? null : userRepository.findById(id).orElse(null);
    }

    @Named("mapCarrierIdToUser")
    default User mapCarrierIdToUser(Long id, UserRepository userRepository) {
        return id == null ? null : userRepository.findById(id).orElse(null);
    }
}
