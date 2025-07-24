package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testCreateUser_Valid() throws Exception {
        UserDto response = new UserDto();
        response.setId(1L);
        response.setName("Стас");
        response.setEmail("stas@test.com");

        Mockito.when(userService.addUser(any(UserDto.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"name\":\"Стас\",\"email\":\"stas@test.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Стас")))
                .andExpect(jsonPath("$.email", is("stas@test.com")));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDto response = new UserDto();
        response.setId(1L);
        response.setName("Стасик");
        response.setEmail("stas@test.com");

        Mockito.when(userService.updateUser(eq(1L), any(UserDto.class)))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"name\":\"Стасик\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Стасик")));
    }

    @Test
    void testGetAllUsers() throws Exception {
        UserDto user1 = new UserDto();
        user1.setId(1L);
        user1.setName("Стас");
        user1.setEmail("stas@test.com");

        UserDto user2 = new UserDto();
        user2.setId(2L);
        user2.setName("Игорь");
        user2.setEmail("igor@test.com");

        Mockito.when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Стас")))
                .andExpect(jsonPath("$[1].email", is("igor@test.com")));
    }

    @Test
    void testGetUserById() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("Стас");
        user.setEmail("stas@test.com");

        Mockito.when(userService.getUser(eq(1L))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Стас")));
    }

    @Test
    void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(eq(1L));

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}
