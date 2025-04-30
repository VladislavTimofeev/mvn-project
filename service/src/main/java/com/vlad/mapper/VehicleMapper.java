package com.vlad.mapper;

import com.vlad.dto.vehicle.VehicleCreateDto;
import com.vlad.dto.vehicle.VehicleEditDto;
import com.vlad.dto.vehicle.VehicleReadDto;
import com.vlad.entity.User;
import com.vlad.entity.Vehicle;
import com.vlad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
@RequiredArgsConstructor
public abstract class VehicleMapper {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public abstract VehicleReadDto toDto(Vehicle vehicle);

    @Mapping(target = "carrier", source = "carrierId", qualifiedByName = "mapCarrierIdToUser")
    public abstract Vehicle toEntity(VehicleCreateDto dto);

    @Mapping(target = "carrier", source = "carrierId", qualifiedByName = "mapCarrierIdToUser")
    public abstract void updateEntityFromDto(VehicleEditDto dto, @MappingTarget Vehicle vehicle);

    @Named("mapCarrierIdToUser")
    protected User mapCarrierIdToUser(Long id) {
        return id == null ? null : userRepository.findById(id).orElse(null);
    }
}
