package com.vlad.service;

import com.vlad.dto.user.UserCreateEditDto;
import com.vlad.dto.user.UserReadDto;
import com.vlad.entity.Role;
import com.vlad.entity.User;
import com.vlad.mapper.UserCreateEditMapper;
import com.vlad.mapper.UserReadMapper;
import com.vlad.repository.UserRepository;
import com.vlad.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
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
    private UserCreateEditMapper userCreateEditMapper;
    @Mock
    private UserReadMapper userReadMapper;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void findAll() {
        User user1 = User.builder()
                .id(1L)
                .username("vlad@gmail.com")
                .password("123gg")
                .name("Vladik")
                .contactInfo("22334455")
                .address("Pushkina 31-2")
                .role(Role.ADMIN)
                .build();
        User user2 = User.builder()
                .id(2L)
                .username("sveta@gmail.com")
                .password("1543рв")
                .name("Sveta")
                .contactInfo("6654376")
                .address("Lobonka 62-1")
                .role(Role.ADMIN)
                .build();
        User user3 = User.builder()
                .id(3L)
                .username("petr@gmail.com")
                .password("95475")
                .name("Petr")
                .contactInfo("0167543")
                .address("Odincova 63-1")
                .role(Role.ADMIN)
                .build();
        List<User> users = List.of(user1, user2, user3);
        UserReadDto dto1 = new UserReadDto(1L, "vlad@gmail.com", "Vladik", "22334455", "Pushkina 31-2", Role.ADMIN);
        UserReadDto dto2 = new UserReadDto(2L, "sveta@gmail.com", "Sveta", "6654376", "Lobonka 62-1", Role.ADMIN);
        UserReadDto dto3 = new UserReadDto(3L, "petr@gmail.com", "Petr", "0167543", "Odincova 63-1", Role.ADMIN);
        when(userRepository.findAll()).thenReturn(users);
        when(userReadMapper.map(user1)).thenReturn(dto1);
        when(userReadMapper.map(user2)).thenReturn(dto2);
        when(userReadMapper.map(user3)).thenReturn(dto3);

        List<UserReadDto> actualResult = userServiceImpl.findAll();

        assertThat(actualResult)
                .containsExactlyInAnyOrder(dto1, dto2, dto3);
    }

    @Test
    void findByIdShouldReturnUserWhenExists() {
        User user = getUser(1L, "vlad@gmail.com", "Vladik");
        UserReadDto userReadDto = new UserReadDto(1L, "vlad@gmail.com", "Vladik", "22334455", "Pushkina 31-2", Role.ADMIN);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userReadMapper.map(user)).thenReturn(userReadDto);

        Optional<UserReadDto> actualResult = userServiceImpl.findById(user.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult).contains(userReadDto);
        verify(userRepository, times(1)).findById(user.getId());
        verify(userReadMapper, times(1)).map(user);
    }

    @Test
    void findByIdShouldReturnEmptyWhenUserDoesNotExists() {
        User user = getUser(1L, "vlad@gmail.com", "Vladik");
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Optional<UserReadDto> actualResult = userServiceImpl.findById(user.getId());

        assertThat(actualResult).isNotPresent();
        verify(userRepository, times(1)).findById(user.getId());
        verifyNoMoreInteractions(userReadMapper);
    }

    @Test
    void shouldSaveAndReturnUser() {
        UserCreateEditDto userCreateEditDto = new UserCreateEditDto("vlad@gmail.com", "123gg", "Vladik", "22334455", "Pushkina 31-2", Role.ADMIN);
        User user = getUser(1L, "vlad@gmail.com", "Vladik");
        UserReadDto userReadDto = new UserReadDto(1L, "vlad@gmail.com", "Vladik", "22334455", "Pushkina 31-2", Role.ADMIN);
        when(userCreateEditMapper.map(userCreateEditDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userReadMapper.map(user)).thenReturn(userReadDto);

        UserReadDto actualResult = userServiceImpl.save(userCreateEditDto);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getId()).isEqualTo(user.getId());
        assertThat(actualResult.getUsername()).isEqualTo(user.getUsername());
        assertThat(actualResult.getRole()).isEqualTo(user.getRole());
        verify(userCreateEditMapper, times(1)).map(userCreateEditDto);
        verify(userRepository, times(1)).save(user);
        verify(userReadMapper, times(1)).map(user);
    }

    @Test
    void shouldUpdateExistingUser() {
        UserCreateEditDto userCreateEditDto = new UserCreateEditDto("vlad@gmail.com", "123gg", "Vladik", "22334455", "Pushkina 31-2", Role.ADMIN);
        User existingUser = getUser(1L, "vlad@gmail.com", "Vladik");
        User updatedUser = User.builder()
                .id(1L)
                .username("masha@gmail.com")
                .password("123gg")
                .name("Maria")
                .contactInfo("77889900")
                .address("Pushkina 66-1")
                .role(Role.CUSTOMER)
                .build();
        UserReadDto expectedUserReadDto = new UserReadDto(1L, "masha@gmail.com", "Maria", "77889900", "Pushkina 66-1", Role.CUSTOMER);
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        when(userCreateEditMapper.map(userCreateEditDto, existingUser)).thenReturn(updatedUser);
        when(userRepository.saveAndFlush(updatedUser)).thenReturn(updatedUser);
        when(userReadMapper.map(updatedUser)).thenReturn(expectedUserReadDto);

        Optional<UserReadDto> actualResult = userServiceImpl.update(1L, userCreateEditDto);

        assertThat(actualResult).isPresent();
        assertThat(actualResult).contains(expectedUserReadDto);
        verify(userRepository, times(1)).findById(existingUser.getId());
        verify(userCreateEditMapper, times(1)).map(userCreateEditDto, existingUser);
        verify(userRepository, times(1)).saveAndFlush(updatedUser);
        verify(userReadMapper, times(1)).map(updatedUser);
    }

    @Test
    void deleteShouldReturnTrueWhenUserExists() {
        User user = getUser(1L, "vlad@gmail.com", "Vladik");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        boolean actualResult = userServiceImpl.delete(user.getId());

        assertThat(actualResult).isTrue();
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).delete(user);
        verify(userRepository, times(1)).flush();
    }

    @Test
    void deleteShouldReturnFalseWhenUserNotFound() {
        User user = getUser(1L, "vlad@gmail.com", "Vladik");
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

    private static User getUser(Long id, String username, String name) {
        return User.builder()
                .id(1L)
                .username("vlad@gmail.com")
                .password("123gg")
                .name("Vladik")
                .contactInfo("22334455")
                .address("Pushkina 31-2")
                .role(Role.ADMIN)
                .build();
    }
}
