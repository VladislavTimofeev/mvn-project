package com.vlad.service;

import com.vlad.dto.user.UserCreateEditDto;
import com.vlad.dto.user.UserReadDto;
import com.vlad.entity.Role;
import com.vlad.entity.User;
import com.vlad.mapper.UserMapper;
import com.vlad.repository.UserRepository;
import com.vlad.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void findByIdShouldReturnUserWhenExists() {
        User user = User.builder()
                .id(1L)
                .username("vlad@gmail.com")
                .password("123gg")
                .name("Vladik")
                .contactInfo("22334455")
                .address("Pushkina 31-2")
                .role(Role.ADMIN)
                .build();
        UserReadDto userReadDto = new UserReadDto(1L, "vlad@gmail.com", "Vladik", "22334455", "Pushkina 31-2", Role.ADMIN);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userReadDto);

        Optional<UserReadDto> actualResult = userServiceImpl.findById(user.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult).contains(userReadDto);
        verify(userRepository, times(1)).findById(user.getId());
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void findByIdShouldReturnEmptyWhenUserDoesNotExists() {
        User user = User.builder()
                .id(1L)
                .username("vlad@gmail.com")
                .password("123gg")
                .name("Vladik")
                .contactInfo("22334455")
                .address("Pushkina 31-2")
                .role(Role.ADMIN)
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Optional<UserReadDto> actualResult = userServiceImpl.findById(user.getId());

        assertThat(actualResult).isNotPresent();
        verify(userRepository, times(1)).findById(user.getId());
        verifyNoMoreInteractions(userMapper);
    }

    @Test
    void shouldSaveAndReturnUser() {
        UserCreateEditDto userCreateEditDto = new UserCreateEditDto("vlad@gmail.com", "123gg", "Vladik", "22334455", "Pushkina 31-2", Role.ADMIN);
        User user = User.builder()
                .id(1L)
                .username("vlad@gmail.com")
                .password("123gg")
                .name("Vladik")
                .contactInfo("22334455")
                .address("Pushkina 31-2")
                .role(Role.ADMIN)
                .build();
        UserReadDto userReadDto = new UserReadDto(1L, "vlad@gmail.com", "Vladik", "22334455", "Pushkina 31-2", Role.ADMIN);
        when(userMapper.toEntity(userCreateEditDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userReadDto);

        UserReadDto actualResult = userServiceImpl.save(userCreateEditDto);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getId()).isEqualTo(user.getId());
        assertThat(actualResult.getUsername()).isEqualTo(user.getUsername());
        assertThat(actualResult.getRole()).isEqualTo(user.getRole());
        verify(userMapper, times(1)).toEntity(userCreateEditDto);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void shouldUpdateExistingUser() {
        UserCreateEditDto userCreateEditDto = new UserCreateEditDto("vlad@gmail.com", "123gg", "Vladik", "22334455", "Pushkina 31-2", Role.ADMIN);
        User existingUser = User.builder()
                .id(1L)
                .username("vlad@gmail.com")
                .password("123gg")
                .name("Vladik")
                .contactInfo("22334455")
                .address("Pushkina 31-2")
                .role(Role.ADMIN)
                .build();
        UserReadDto expectedUserReadDto = new UserReadDto(1L, "masha@gmail.com", "Maria", "77889900", "Pushkina 66-1", Role.CUSTOMER);
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        when(userRepository.saveAndFlush(existingUser)).thenReturn(existingUser);
        when(userMapper.toDto(existingUser)).thenReturn(expectedUserReadDto);

        Optional<UserReadDto> actualResult = userServiceImpl.update(1L, userCreateEditDto);

        assertThat(actualResult).isPresent();
        assertThat(actualResult).contains(expectedUserReadDto);
        verify(userRepository, times(1)).findById(existingUser.getId());
        verify(userRepository, times(1)).saveAndFlush(existingUser);
        verify(userMapper, times(1)).updateEntityFromDto(userCreateEditDto, existingUser);
        verify(userMapper).toDto(existingUser);
    }

    @Test
    void deleteShouldReturnTrueWhenUserExists() {
        User user = User.builder()
                .id(1L)
                .username("vlad@gmail.com")
                .password("123gg")
                .name("Vladik")
                .contactInfo("22334455")
                .address("Lobonka 22-22")
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        boolean actualResult = userServiceImpl.delete(user.getId());

        assertThat(actualResult).isTrue();
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).delete(user);
        verify(userRepository, times(1)).flush();
    }

    @Test
    void deleteShouldReturnFalseWhenUserNotFound() {
        User user = User.builder()
                .id(1L)
                .username("vlad@gmail.com")
                .password("123gg")
                .name("Vladik")
                .contactInfo("22334455")
                .address("Lobonka 22-22")
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        boolean actualResult = userServiceImpl.delete(user.getId());

        assertThat(actualResult).isFalse();
        verify(userRepository, times(1)).findById(user.getId());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void loadUserByUsernameUserFound() {
        String username = "testUser";
        String password = "testPassword";
        Role role = Role.ADMIN;
        User mockUser = User.builder()
                .id(1L)
                .username(username)
                .password(password)
                .role(role)
                .name("Ivan")
                .contactInfo("test@example.com")
                .address("123 Test Street")
                .build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = userServiceImpl.loadUserByUsername(username);

        assertNotNull(userDetails, "UserDetails не должен быть null");
        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(role.name())));
    }
}
