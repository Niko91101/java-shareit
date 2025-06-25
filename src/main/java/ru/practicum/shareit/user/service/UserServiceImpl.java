package ru.practicum.shareit.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.validation.UserValidator;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private final UserValidator userValidator;

    public UserServiceImpl(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        userValidator.validateEmail(userDto.getEmail());
        userValidator.checkEmailUnique(userDto.getEmail(), users);

        User user = UserMapper.toUser(userDto);
        user.setId(idCounter.getAndIncrement());
        users.put(user.getId(), user);
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User existing = users.get(userId);

        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }

        if (userDto.getName() != null) {
            existing.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            userValidator.validateEmail(userDto.getEmail());
            userValidator.checkEmailUniqueForUpdate(userDto.getEmail(), userId, users);
            existing.setEmail(userDto.getEmail());
        }

        return UserMapper.toDto(existing);
    }

    @Override
    public UserDto getUser(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return UserMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return users.values().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
    }
}
