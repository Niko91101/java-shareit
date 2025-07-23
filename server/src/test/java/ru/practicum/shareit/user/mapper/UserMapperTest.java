package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    @Test
    void toUser_convertsDtoToEntity() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("Test User");
        dto.setEmail("test@example.com");

        User user = UserMapper.toUser(dto);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("Test User");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void toDto_convertsEntityToDto() {
        User user = new User();
        user.setId(2L);
        user.setName("Another");
        user.setEmail("another@example.com");

        UserDto dto = UserMapper.toDto(user);

        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getName()).isEqualTo("Another");
        assertThat(dto.getEmail()).isEqualTo("another@example.com");
    }
}
