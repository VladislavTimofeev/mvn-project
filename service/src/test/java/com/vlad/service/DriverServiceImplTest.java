package com.vlad.service;

import com.vlad.dto.driver.DriverCreateDto;
import com.vlad.dto.driver.DriverEditDto;
import com.vlad.dto.driver.DriverReadDto;
import com.vlad.dto.user.UserCreateEditDto;
import com.vlad.dto.user.UserReadDto;
import com.vlad.entity.Driver;
import com.vlad.entity.Role;
import com.vlad.entity.User;
import com.vlad.mapper.DriverMapper;
import com.vlad.mapper.UserMapper;
import com.vlad.repository.DriverRepository;
import com.vlad.repository.UserRepository;
import com.vlad.service.impl.DriverServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class DriverServiceImplTest {

    @Mock
    private DriverRepository driverRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DriverMapper driverMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private DriverServiceImpl driverServiceImpl;

    @Test
    void findAllShouldReturnListOfDrivers() {
        Driver driver1 = getDriver(1L, "Alex");
        Driver driver2 = getDriver(2L, "Bob");
        Driver driver3 = getDriver(3L, "Charlie");
        List<Driver> drivers = List.of(driver1, driver2, driver3);
        DriverReadDto driverReadDto1 = getDriverReadDto(1L, "Alex");
        DriverReadDto driverReadDto2 = getDriverReadDto(2L, "Bob");
        DriverReadDto driverReadDto3 = getDriverReadDto(3L, "Charlie");
        doReturn(drivers).when(driverRepository).findAll();
        doReturn(driverReadDto1).when(driverMapper).toDto(driver1);
        doReturn(driverReadDto2).when(driverMapper).toDto(driver2);
        doReturn(driverReadDto3).when(driverMapper).toDto(driver3);

        List<DriverReadDto> actualResult = driverServiceImpl.findAll();

        assertThat(actualResult).hasSize(3);
        verify(driverRepository, times(1)).findAll();
        verify(driverMapper, times(1)).toDto(driver1);
        verify(driverMapper, times(1)).toDto(driver2);
        verify(driverMapper, times(1)).toDto(driver3);
    }

    @Test
    void findByIdShouldReturnDriverWhenExists() {
        Driver driver = getDriver(1L, "Alexander");
        DriverReadDto driverReadDto = getDriverReadDto(1L, "Alexander");
        doReturn(Optional.of(driver)).when(driverRepository).findById(driver.getId());
        doReturn(driverReadDto).when(driverMapper).toDto(driver);

        Optional<DriverReadDto> actualResult = driverServiceImpl.findById(driver.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(driverReadDto);
        verify(driverRepository, times(1)).findById(driver.getId());
        verify(driverMapper, times(1)).toDto(driver);
    }

    @Test
    void findByIdShouldReturnEmptyWhenDriverNotExists() {
        Driver driver = getDriver(1L, "Alexander");
        doReturn(Optional.empty()).when(driverRepository).findById(driver.getId());

        Optional<DriverReadDto> actualResult = driverServiceImpl.findById(driver.getId());

        assertThat(actualResult).isNotPresent();
        verify(driverRepository, times(1)).findById(driver.getId());
        verifyNoInteractions(driverMapper);
    }

    @Test
    void shouldSaveAndReturnDriver() {
        DriverCreateDto driverCreateDto = new DriverCreateDto(1L, "Pavel", "AGKN453FS", "2887359");
        Driver driver = getDriver(1L, "Pavel");
        DriverReadDto driverReadDto = getDriverReadDto(1L, "Pavel");
        when(driverMapper.toEntity(driverCreateDto)).thenReturn(driver);
        when(driverRepository.save(driver)).thenReturn(driver);
        when(driverMapper.toDto(driver)).thenReturn(driverReadDto);

        DriverReadDto actualResult = driverServiceImpl.save(driverCreateDto);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getId()).isEqualTo(driverReadDto.getId());
        assertThat(actualResult.getName()).isEqualTo(driverReadDto.getName());
        verify(driverMapper, times(1)).toEntity(driverCreateDto);
        verify(driverRepository, times(1)).save(driver);
        verify(driverMapper, times(1)).toDto(driver);
    }

    @Test
    void shouldUpdateExistingDriver() {
        DriverEditDto driverEditDto = new DriverEditDto(1L, "Polina", "AAAAAAAAA", "0000000");
        UserCreateEditDto carrierDto = new UserCreateEditDto("vladtrans", "234546", "fastWayDelivery", "3450098", "Krupskaia 21/3", Role.CARRIER);
        User carrier = userMapper.toEntity(carrierDto);
        Driver existingDriver = new Driver(1L, carrier, "Maria", "AFG5474FH", "2456733");
        DriverReadDto expectedDriverReadDto = getDriverReadDto(1L, "Polina");
        when(driverRepository.findById(existingDriver.getId())).thenReturn(Optional.of(existingDriver));
        doNothing().when(driverMapper).updateEntityFromDto(driverEditDto, existingDriver, userRepository);
        when(driverRepository.saveAndFlush(existingDriver)).thenReturn(existingDriver);
        when(driverMapper.toDto(existingDriver)).thenReturn(expectedDriverReadDto);

        Optional<DriverReadDto> actualResult = driverServiceImpl.update(1L, driverEditDto);

        assertThat(actualResult).isPresent();
        assertThat(actualResult).contains(expectedDriverReadDto);
        verify(driverRepository, times(1)).findById(existingDriver.getId());
        verify(driverMapper, times(1)).updateEntityFromDto(driverEditDto, existingDriver, userRepository);
        verify(driverRepository, times(1)).saveAndFlush(existingDriver);
        verify(driverMapper, times(1)).toDto(existingDriver);
    }

    @Test
    void deleteShouldReturnTrueWhenDriverExists() {
        Driver driver = getDriver(1L, "Alexander");
        when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));

        boolean actualResult = driverServiceImpl.delete(driver.getId());

        assertThat(actualResult).isTrue();
        verify(driverRepository, times(1)).findById(driver.getId());
        verify(driverRepository, times(1)).delete(driver);
        verify(driverRepository, times(1)).flush();
    }

    @Test
    void deleteShouldReturnFalseWhenDriverNotFound() {
        Driver driver = getDriver(1L, "Alexander");
        when(driverRepository.findById(driver.getId())).thenReturn(Optional.empty());

        boolean actualResult = driverServiceImpl.delete(driver.getId());

        assertThat(actualResult).isFalse();
        verify(driverRepository, times(1)).findById(driver.getId());
        verifyNoMoreInteractions(driverRepository);
    }

    private DriverReadDto getDriverReadDto(Long id, String name) {
        UserReadDto carrier = new UserReadDto(1L, "transdelivery", "fastway", "2345600", "Brovki 14", Role.CARRIER);
        return new DriverReadDto(id, carrier, name, "AGKN453FS", "2887359");
    }

    private Driver getDriver(Long id, String name) {
        UserCreateEditDto carrierDto = getCarrier();
        User carrier = userMapper.toEntity(carrierDto);
        return Driver.builder()
                .id(id)
                .carrier(carrier)
                .name(name)
                .licenseNumber("AGKN453FS")
                .phoneNumber("2887359")
                .build();
    }

    private UserCreateEditDto getCarrier() {
        return new UserCreateEditDto("trans123", "12344", "vladtrans", "13490909", "Selitskogo 21B", Role.CARRIER);
    }
}
