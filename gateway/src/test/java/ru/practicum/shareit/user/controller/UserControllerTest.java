package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.validator.UserValidator;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserClient userClient;

    @MockBean
    private UserValidator userValidator;

    @Test
    void testCreateUser() throws Exception {
        Mockito.when(userClient.create(Mockito.any(UserDto.class)))
                .thenReturn(ResponseEntity.ok(Map.of("id", 1, "name", "Стас", "email", "stas@test.com")));

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"name\":\"Стас\",\"email\":\"stas@test.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Стас")))
                .andExpect(jsonPath("$.email", is("stas@test.com")));
    }

    @Test
    void testUpdateUser() throws Exception {
        Mockito.when(userClient.update(eq(1L), Mockito.any(UserDto.class)))
                .thenReturn(ResponseEntity.ok(Map.of("id", 1, "name", "Стасик", "email", "stas@test.com")));

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"name\":\"Стасик\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Стасик")));
    }

    @Test
    void testGetAllUsers() throws Exception {
        Mockito.when(userClient.getAll())
                .thenReturn(ResponseEntity.ok(List.of(
                        Map.of("id", 1, "name", "Стас", "email", "stas@test.com"),
                        Map.of("id", 2, "name", "Игорь", "email", "igor@test.com")
                )));

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Стас")))
                .andExpect(jsonPath("$[1].email", is("igor@test.com")));
    }

    @Test
    void testGetUserById() throws Exception {
        Mockito.when(userClient.getById(eq(1L)))
                .thenReturn(ResponseEntity.ok(Map.of("id", 1, "name", "Стас", "email", "stas@test.com")));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Стас")));
    }

    @Test
    void testDeleteUser() throws Exception {
        Mockito.when(userClient.delete(eq(1L)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isOk());
    }
}
