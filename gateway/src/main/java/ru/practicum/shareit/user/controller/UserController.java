package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.validator.UserValidator;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;
    private final UserValidator userValidator;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto userDto) {
        userValidator.checkEmailUnique(userDto.getEmail());
        log.info("Создаю пользователя: {}", userDto);
        return userClient.create(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable long userId, @RequestBody UserDto userDto) {
        if (userDto.getEmail() != null) {
            userValidator.checkEmailUniqueForUpdate(userDto.getEmail(), userId);
        }
        log.info("Обновляю пользователя с id={}", userId);
        return userClient.update(userId, userDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Получаю список всех пользователей");
        return userClient.getAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable long userId) {
        log.info("Получаю пользователя с id={}", userId);
        return userClient.getById(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable long userId) {
        log.info("Удаляю пользователя с id={}", userId);
        return userClient.delete(userId);
    }
}



