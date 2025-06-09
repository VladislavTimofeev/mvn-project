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

@Mapper(componentModel = "spring")
public abstract class RequestMapper {

    private final UserRepository userRepository;

    protected RequestMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public abstract RequestReadDto toDto(Request request);

    @Mapping(target = "customer", source = "customerId", qualifiedByName = "mapCustomerIdToUser")
    @Mapping(target = "carrier", source = "carrierId", qualifiedByName = "mapCarrierIdToUser")
    public abstract Request toEntity(RequestCreateEditDto dto);

    @Mapping(target = "customer", source = "customerId", qualifiedByName = "mapCustomerIdToUser")
    @Mapping(target = "carrier", source = "carrierId", qualifiedByName = "mapCarrierIdToUser")
    public abstract void updateEntityFromDto(RequestCreateEditDto dto, @MappingTarget Request request);

    @Named("mapCustomerIdToUser")
    protected User mapCustomerIdToUser(Long id) {
        return id == null ? null : userRepository.findById(id).orElse(null);
    }

    @Named("mapCarrierIdToUser")
    protected User mapCarrierIdToUser(Long id) {
        return id == null ? null : userRepository.findById(id).orElse(null);
    }
}
