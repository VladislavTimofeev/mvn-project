package com.vlad.entity;

import com.vlad.TestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserIT extends TestBase {

    @Test
    void deleteUser() {
        User user = User.builder()
                .username("MickyMouse")
                .password("1234567")
                .role(Role.GUEST)
                .name("Viktoria")
                .contactInfo("vika@example.com")
                .address("Mazurova 4")
                .build();
        session.persist(user);
        session.remove(user);
        session.flush();
        session.clear();

        User actualResult = session.get(User.class, user.getId());

        assertNull(actualResult);

    }

    @Test
    void updateUser() {
        User user = User.builder()
                .username("MickyMouse")
                .password("1234567")
                .role(Role.GUEST)
                .name("Viktoria")
                .contactInfo("vika@example.com")
                .address("Mazurova 4")
                .build();
        session.persist(user);
        user.setName("ViktoriaPobeditelnica");
        session.merge(user);
        session.flush();
        session.clear();

        User actualResult = session.get(User.class, user.getId());

        assertEquals("ViktoriaPobeditelnica", actualResult.getName());

    }

    @Test
    void getUser() {
        User user = User.builder()
                .username("MickyMouse")
                .password("1234567")
                .role(Role.GUEST)
                .name("Viktoria")
                .contactInfo("vika@example.com")
                .address("Mazurova 4")
                .build();
        session.persist(user);
        session.flush();
        session.clear();

        User actualResult = session.get(User.class, user.getId());

        assertEquals(user, actualResult);
    }

    @Test
    void createUser() {
        User user = User.builder()
                .username("MickyMouse")
                .password("1234567")
                .role(Role.GUEST)
                .name("Viktoria")
                .contactInfo("vika@example.com")
                .address("Mazurova 4")
                .build();
        session.persist(user);
        session.flush();
        session.clear();

        User actualResult = session.get(User.class, user.getId());

        assertEquals(user, actualResult);
    }

}
