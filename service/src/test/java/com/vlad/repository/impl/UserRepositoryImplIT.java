package com.vlad.repository.impl;

import com.vlad.BaseIT;
import com.vlad.entity.Role;
import com.vlad.entity.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


class UserRepositoryImplIT extends BaseIT {

    private UserRepositoryImpl userRepository;

    @Test
    void deleteUser(){
        userRepository = new UserRepositoryImpl(entityManager);
        User user = new User();
        user.setUsername("MickyMouse");
        user.setPassword("123MickyMouse123");
        user.setRole(Role.GUEST);
        user.setName("Martin");
        user.setContactInfo("martin@gmail.com");
        user.setAddress("Mazurova 4-56");
        userRepository.save(user);
        userRepository.delete(user.getId());
        entityManager.clear();

        Optional<User> actualResult = userRepository.findById(user.getId());

        assertFalse(actualResult.isPresent());
    }

    @Test
    void updateUser(){
        userRepository = new UserRepositoryImpl(entityManager);
        User user = new User();
        user.setUsername("MickyMouse");
        user.setPassword("123MickyMouse123");
        user.setRole(Role.GUEST);
        user.setName("Martin");
        user.setContactInfo("martin@gmail.com");
        user.setAddress("Mazurova 4-56");
        userRepository.save(user);
        user.setName("TIMON");
        userRepository.update(user);
        entityManager.flush();
        entityManager.clear();

        Optional<User> actualResult = userRepository.findById(user.getId());

        assertTrue(actualResult.isPresent());
        assertEquals("TIMON", actualResult.get().getName());
    }

    @Test
    void getAllUsers(){
        userRepository = new UserRepositoryImpl(entityManager);
        User user1 = new User();
        user1.setUsername("MickyMouse");
        user1.setPassword("123MickyMouse123");
        user1.setRole(Role.GUEST);
        user1.setName("Martin");
        user1.setContactInfo("martin@gmail.com");
        user1.setAddress("Mazurova 4-56");
        userRepository.save(user1);
        User user2 = new User();
        user2.setUsername("Sallywan");
        user2.setPassword("12345677854");
        user2.setRole(Role.GUEST);
        user2.setName("Sally");
        user2.setContactInfo("Sally@gmail.com");
        user2.setAddress("Kolasa 5-19");
        userRepository.save(user2);
        entityManager.flush();
        entityManager.clear();

        List<User> actualResult = userRepository.findAll();

        assertEquals(2,actualResult.size());
        assertThat(actualResult).containsExactlyInAnyOrder(user1, user2);
    }

    @Test
    void saveUser() {
        userRepository = new UserRepositoryImpl(entityManager);
        User user = new User();
        user.setUsername("MickyMouse");
        user.setPassword("123MickyMouse123");
        user.setRole(Role.GUEST);
        user.setName("Martin");
        user.setContactInfo("martin@gmail.com");
        user.setAddress("Mazurova 4-56");
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        Optional<User> actualResult = userRepository.findById(user.getId());

        assertTrue(actualResult.isPresent());
        assertEquals(user, actualResult.get());
    }

}
