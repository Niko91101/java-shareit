package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl service;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");
        user.setName("User");
    }

    @Test
    void get_throwsIfNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> service.getUser(1L));
    }

    @Test
    void getAll_returnsEmptyIfNoUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserDto> result = service.getAllUsers();

        assertThat(result).isEmpty();
    }

    @Test
    void getAll_returnsListOfUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@test.com");
        user1.setName("User1");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@test.com");
        user2.setName("User2");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserDto> result = service.getAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("User1");
        assertThat(result.get(1).getName()).isEqualTo("User2");
    }
}
