package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validation.UserValidator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import({UserServiceImpl.class, UserValidator.class})
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userDto = new UserDto();
        userDto.setName("Стас");
        userDto.setEmail("stas@example.com");
    }

    @Test
    void addUser_ShouldSaveUser() {
        UserDto saved = userService.addUser(userDto);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Стас");
        assertThat(saved.getEmail()).isEqualTo("stas@example.com");

        User fromDb = userRepository.findById(saved.getId()).orElseThrow();
        assertThat(fromDb.getEmail()).isEqualTo("stas@example.com");
    }

    @Test
    void addUser_DuplicateEmail_ShouldThrow() {
        userService.addUser(userDto);
        UserDto duplicate = new UserDto();
        duplicate.setName("Другой");
        duplicate.setEmail("stas@example.com");

        assertThatThrownBy(() -> userService.addUser(duplicate))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Email уже используется");
    }

    @Test
    void updateUser_ShouldUpdateNameAndEmail() {
        UserDto saved = userService.addUser(userDto);
        Long id = saved.getId();

        UserDto update = new UserDto();
        update.setName("Новый Стас");
        update.setEmail("new@example.com");

        UserDto updated = userService.updateUser(id, update);

        assertThat(updated.getName()).isEqualTo("Новый Стас");
        assertThat(updated.getEmail()).isEqualTo("new@example.com");
    }

    @Test
    void updateUser_NonExisting_ShouldThrow() {
        assertThatThrownBy(() -> userService.updateUser(999L, userDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Пользователь не найден");
    }

    @Test
    void getUser_ShouldReturnCorrectUser() {
        UserDto saved = userService.addUser(userDto);
        UserDto found = userService.getUser(saved.getId());

        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getEmail()).isEqualTo("stas@example.com");
    }

    @Test
    void getAllUsers_ShouldReturnList() {
        userService.addUser(userDto);
        UserDto another = new UserDto();
        another.setName("Вася");
        another.setEmail("vasya@example.com");
        userService.addUser(another);

        List<UserDto> users = userService.getAllUsers();
        assertThat(users).hasSize(2);
    }

    @Test
    void deleteUser_ShouldRemoveUser() {
        UserDto saved = userService.addUser(userDto);
        userService.deleteUser(saved.getId());

        assertThat(userRepository.findById(saved.getId())).isEmpty();
    }
}
