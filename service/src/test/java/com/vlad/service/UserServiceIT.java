package com.vlad.service;

import com.vlad.annotation.IT;
import com.vlad.dto.user.UserCreateEditDto;
import com.vlad.dto.user.UserReadDto;
import com.vlad.entity.Role;
import com.vlad.entity.User;
import com.vlad.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@RequiredArgsConstructor
class UserServiceIT {

    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final UserService userService;

    @Test
    void findAll() {
        User firstUser = getUser();
        User secondUser = getUser1();
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        entityManager.flush();
        entityManager.clear();

        List<UserReadDto> actualResult = userService.findAll();

        assertThat(actualResult).hasSize(2);
        assertThat(actualResult.get(0).getUsername()).isEqualTo(firstUser.getUsername());
        assertThat(actualResult.get(1).getUsername()).isEqualTo(secondUser.getUsername());
    }

    @Test
    void findById() {
        User firstUser = getUser();
        User secondUser = getUser1();
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        entityManager.flush();
        entityManager.clear();

        Optional<UserReadDto> maybeUser = userService.findById(secondUser.getId());

        assertThat(maybeUser).isPresent();
        maybeUser.ifPresent(user -> assertEquals("mike@gmail.com", user.getContactInfo()));
    }

    @Test
    void save() {
        UserCreateEditDto userCreateEditDto = new UserCreateEditDto(
                "robinhood",
                "robin12345",
                "Robin",
                "dreamforest@gmail.com",
                "Green forest, second stone",
                Role.GUEST
        );

        UserReadDto actualResult = userService.save(userCreateEditDto);

        assertThat(actualResult).isNotNull();
        assertEquals(userCreateEditDto.getUsername(), actualResult.getUsername());
        assertEquals(userCreateEditDto.getName(), actualResult.getName());
    }

    @Test
    void update() {
        User user = getUser();
        UserCreateEditDto userCreateEditDto = new UserCreateEditDto(
                "robinhood",
                "robin12345",
                "Robin",
                "dreamforest@gmail.com",
                "Green forest, second stone",
                Role.GUEST
        );
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        Optional<UserReadDto> actualResult = userService.update(user.getId(), userCreateEditDto);
        assertThat(actualResult).isPresent();

        assertEquals(userCreateEditDto.getUsername(), actualResult.get().getUsername());
        assertEquals(userCreateEditDto.getName(), actualResult.get().getName());
        assertEquals(userCreateEditDto.getContactInfo(), actualResult.get().getContactInfo());
    }

    @Test
    void delete() {
        User user = getUser();
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        boolean result = userService.delete(user.getId());

        assertTrue(result);
    }

    private static User getUser1() {
        User user = new User();
        user.setUsername("MikeWazowski");
        user.setPassword("mike123000");
        user.setRole(Role.GUEST);
        user.setName("Mikeeee");
        user.setContactInfo("mike@gmail.com");
        user.setAddress("Pushkina 4-56");
        return user;
    }

    private static User getUser() {
        User user = new User();
        user.setUsername("MickyMouse");
        user.setPassword("123MickyMouse123");
        user.setRole(Role.GUEST);
        user.setName("Martin");
        user.setContactInfo("martin@gmail.com");
        user.setAddress("Mazurova 4-56");
        return user;
    }
}
