package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto);

    UserDto getUser(Long id);

    List<UserDto> getAllUsers();

    void deleteUser(Long userId);
}
