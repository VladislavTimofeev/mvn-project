package com.vlad.http.controller;

import com.vlad.annotation.IT;
import com.vlad.dto.user.UserCreateEditDto;
import com.vlad.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IT
@RequiredArgsConstructor
@AutoConfigureMockMvc
class UserControllerIT {

    private final MockMvc mockMvc;
    private final UserService userService;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/users"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get("/users/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/users/*"))
                .andExpect(model().attributeExists("user/user"));
    }

    @Test
    void save() throws Exception {
        mockMvc.perform(post("/users")
                        .param(UserCreateEditDto.Fields.username, "test")
                        .param(UserCreateEditDto.Fields.password, "test1111")
                        .param(UserCreateEditDto.Fields.name, "TEST")
                        .param(UserCreateEditDto.Fields.contactInfo, "test@gmail.com")
                        .param(UserCreateEditDto.Fields.address, "test 1 house 2")
                        .param(UserCreateEditDto.Fields.role, "ADMIN")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/users/*")
                );

    }

    @Test
    void update() throws Exception {
        mockMvc.perform(post("/users/1")
                        .param(UserCreateEditDto.Fields.username, "updatedUsername")
                        .param(UserCreateEditDto.Fields.password, "updatedPassword1111")
                        .param(UserCreateEditDto.Fields.name, "Updated Name")
                        .param(UserCreateEditDto.Fields.contactInfo, "updated.email@gmail.com")
                        .param(UserCreateEditDto.Fields.address, "updated address 123")
                        .param(UserCreateEditDto.Fields.role, "USER")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/users/*")
                );
    }

    @Test
    void delete() throws Exception {
        when(userService.delete(1L)).thenReturn(true);
        mockMvc.perform(post("/users/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

}
