package com.vlad.service;

import com.querydsl.core.types.Predicate;
import com.vlad.dto.filter.VehicleFilterDto;
import com.vlad.dto.user.UserCreateEditDto;
import com.vlad.dto.user.UserReadDto;
import com.vlad.dto.vehicle.VehicleCreateDto;
import com.vlad.dto.vehicle.VehicleEditDto;
import com.vlad.dto.vehicle.VehicleReadDto;
import com.vlad.entity.QVehicle;
import com.vlad.entity.Role;
import com.vlad.entity.User;
import com.vlad.entity.Vehicle;
import com.vlad.mapper.UserCreateEditMapper;
import com.vlad.mapper.VehicleCreateMapper;
import com.vlad.mapper.VehicleEditMapper;
import com.vlad.mapper.VehicleReadMapper;
import com.vlad.repository.QPredicate;
import com.vlad.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class VehicleServiceTest {

    @Mock
    private UserCreateEditMapper userCreateEditMapper;
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private VehicleReadMapper vehicleReadMapper;
    @Mock
    private VehicleCreateMapper vehicleCreateMapper;
    @Mock
    private VehicleEditMapper vehicleEditMapper;
    @InjectMocks
    private VehicleService vehicleService;

//    @Test
//    void findAllShouldReturnAllVehicles() {
//        VehicleFilterDto filterDto = new VehicleFilterDto(25, true, "ISUZU");
//        Pageable pageable = PageRequest.of(0, 10);
//        Vehicle vehicle1 = getVehicle(1L,"AAA123CX", 25, "ISUZU");
//        Vehicle vehicle2 = getVehicle(2L,"VVV456WE", 18, "FORD");
//        Vehicle vehicle3 = getVehicle(3L,"BBB123XZ", 20, "ISUZU");
//        List<Vehicle> vehicles = List.of(vehicle1, vehicle2, vehicle3);
//        VehicleReadDto vehicleReadDto1 = getVehicleReadDto(1L, "AAA123CX", BigDecimal.valueOf(1500), 25, true, "ISUZU");
//        VehicleReadDto vehicleReadDto2 = getVehicleReadDto(2L, "VVV456WE", BigDecimal.valueOf(1500), 18, true, "FORD");
//        VehicleReadDto vehicleReadDto3 = getVehicleReadDto(3L, "BBB123XZ", BigDecimal.valueOf(1500), 20, true, "ISUZU");
//        Predicate predicate = QPredicate.builder()
//                .add(filterDto.getPalletCapacity(), QVehicle.vehicle.palletCapacity::eq)
//                .add(filterDto.getRefrigerated(), QVehicle.vehicle.refrigerated::eq)
//                .add(filterDto.getModel(), QVehicle.vehicle.model::eq)
//                .buildAnd();
//        Page<Vehicle> vehiclePage = new PageImpl<>(vehicles, pageable, vehicles.size());
//        when(vehicleRepository.findAll(predicate, pageable)).thenReturn(vehiclePage);
//        when(vehicleReadMapper.map(vehicle1)).thenReturn(vehicleReadDto1);
//        when(vehicleReadMapper.map(vehicle2)).thenReturn(vehicleReadDto2);
//        when(vehicleReadMapper.map(vehicle3)).thenReturn(vehicleReadDto3);
//
//        Page<VehicleReadDto> actualResult = vehicleService.findAll(filterDto, pageable);
//
//        assertNotNull(actualResult);
//        assertEquals(3, actualResult.getTotalElements());
//        assertTrue(actualResult.getContent().containsAll(List.of(vehicleReadDto1, vehicleReadDto2, vehicleReadDto3)));
//        verify(vehicleRepository).findAll(predicate, pageable);
//        verify(vehicleReadMapper).map(vehicle1);
//        verify(vehicleReadMapper).map(vehicle2);
//        verify(vehicleReadMapper).map(vehicle3);
//    }

    @Test
    void findByIdShouldReturnVehicleWhenExists() {
        Vehicle vehicle = getVehicle(1L,"AAA123CX", 25, "ISUZU");
        VehicleReadDto vehicleReadDto1 = getVehicleReadDto(1L, "AAA123CX", BigDecimal.valueOf(1500), 25, true, "ISUZU");
        doReturn(Optional.of(vehicle)).when(vehicleRepository).findById(vehicle.getId());
        doReturn(vehicleReadDto1).when(vehicleReadMapper).map(vehicle);

        Optional<VehicleReadDto> actualResult = vehicleService.findById(vehicle.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult).contains(vehicleReadDto1);
        verify(vehicleRepository, times(1)).findById(vehicle.getId());
        verify(vehicleReadMapper, times(1)).map(vehicle);
    }

    @Test
    void findByIdShouldReturnEmptyWhenVehicleDoesNotExist() {
        Vehicle vehicle = getVehicle(1L,"AAA123CX", 25, "ISUZU");
        when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.empty());

        Optional<VehicleReadDto> actualResult = vehicleService.findById(vehicle.getId());

        assertThat(actualResult).isNotPresent();
        verify(vehicleRepository, times(1)).findById(vehicle.getId());
        verifyNoInteractions(vehicleReadMapper);
    }

    @Test
    void shouldSaveAndReturnVehicle() {
        VehicleCreateDto vehicleCreateDto = new VehicleCreateDto(1L, "AAA123CX", BigDecimal.valueOf(1500), 25, true, "ISUZU");
        Vehicle vehicle = getVehicle(1L,"AAA123CX", 25, "ISUZU");
        VehicleReadDto vehicleReadDto = getVehicleReadDto(1L, "AAA123CX", BigDecimal.valueOf(1500), 25, true, "ISUZU");
        when(vehicleCreateMapper.map(vehicleCreateDto)).thenReturn(vehicle);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleReadMapper.map(vehicle)).thenReturn(vehicleReadDto);

        VehicleReadDto actualResult = vehicleService.save(vehicleCreateDto);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getId()).isEqualTo(vehicleReadDto.getId());
        assertThat(actualResult.getLicensePlate()).isEqualTo(vehicleReadDto.getLicensePlate());
        assertThat(actualResult.getModel()).isEqualTo(vehicleReadDto.getModel());
        verify(vehicleCreateMapper, times(1)).map(vehicleCreateDto);
        verify(vehicleRepository, times(1)).save(vehicle);
        verify(vehicleReadMapper, times(1)).map(vehicle);
    }

    @Test
    void shouldUpdateExistingVehicle() {
        VehicleEditDto vehicleEditDto = new VehicleEditDto(1L, "BBB666GG", BigDecimal.valueOf(1500), 20, true, "MAZDA");
        Vehicle existingVehicle = getVehicle(1L,"AAA123CX", 25, "ISUZU");
        Vehicle updatedVehicle = getVehicle(1L,"BBB666GG", 20, "MAZDA");
        VehicleReadDto expectedVehicleReadDto = getVehicleReadDto(1L, "BBB666GG", BigDecimal.valueOf(1500), 20, true, "MAZDA");
        when(vehicleRepository.findById(existingVehicle.getId())).thenReturn(Optional.of(existingVehicle));
        when(vehicleEditMapper.map(vehicleEditDto, existingVehicle)).thenReturn(updatedVehicle);
        when(vehicleRepository.saveAndFlush(updatedVehicle)).thenReturn(updatedVehicle);
        when(vehicleReadMapper.map(updatedVehicle)).thenReturn(expectedVehicleReadDto);

        Optional<VehicleReadDto> actualResult = vehicleService.update(1L, vehicleEditDto);

        assertThat(actualResult).isPresent();
        assertThat(actualResult).contains(expectedVehicleReadDto);
        verify(vehicleRepository, times(1)).findById(existingVehicle.getId());
        verify(vehicleEditMapper, times(1)).map(vehicleEditDto, existingVehicle);
        verify(vehicleRepository, times(1)).saveAndFlush(updatedVehicle);
        verify(vehicleReadMapper, times(1)).map(updatedVehicle);
    }

    @Test
    void deleteShouldReturnTrueWhenVehicleExists() {
        Vehicle vehicle = getVehicle(1L,"AAA123CX", 25, "ISUZU");
        when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));

        boolean actualResult = vehicleService.delete(vehicle.getId());

        assertThat(actualResult).isTrue();
        verify(vehicleRepository, times(1)).findById(vehicle.getId());
        verify(vehicleRepository,times(1)).delete(vehicle);
        verify(vehicleRepository, times(1)).flush();
    }

    @Test
    void deleteShouldReturnFalseWhenVehicleNotFound() {
        Vehicle vehicle = getVehicle(1L,"AAA123CX", 25, "ISUZU");
        when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.empty());

        boolean actualResult = vehicleService.delete(vehicle.getId());

        assertThat(actualResult).isFalse();
        verify(vehicleRepository, times(1)).findById(vehicle.getId());
        verifyNoMoreInteractions(vehicleRepository);
    }

    private VehicleReadDto getVehicleReadDto(Long id, String licensePlate, BigDecimal capacity, Integer palletCapacity, Boolean refrigerated, String model) {
        UserReadDto carrier = new UserReadDto(1L, "transdelivery", "fastway", "2345600", "Brovki 14", Role.CARRIER);
        return new VehicleReadDto(id, carrier, licensePlate, capacity, palletCapacity, refrigerated, model);
    }

    private Vehicle getVehicle(Long id, String licensePlate, Integer palletCapacity, String model) {
        UserCreateEditDto carrierDto = getCarrier();
        User carrier = userCreateEditMapper.map(carrierDto);
        return Vehicle.builder()
                .id(id)
                .carrier(carrier)
                .licensePlate(licensePlate)
                .capacity(BigDecimal.valueOf(1500))
                .palletCapacity(palletCapacity)
                .refrigerated(true)
                .model(model)
                .build();
    }

    private UserCreateEditDto getCarrier() {
        return new UserCreateEditDto("trans123", "12344", "vladtrans", "13490909", "Selitskogo 21B", Role.CARRIER);
    }
}
