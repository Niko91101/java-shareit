package ru.practicum.shareit.user.validation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.model.User;

import java.util.Map;

@Component
public class UserValidator {

    public void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email не может быть пустым");
        }

        if (!email.contains("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email некорректный");
        }
    }

    public void checkEmailUnique(String email, Map<Long, User> users) {
        boolean emailExists = users.values().stream()
                .anyMatch(u -> u.getEmail().equals(email));

        if (emailExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Такой  Email уже используется");
        }
    }

    public void checkEmailUniqueForUpdate(String email, Long userId, Map<Long, User> users) {
        boolean emailExists = users.values().stream()
                .anyMatch(u -> u.getEmail().equals(email) && !u.getId().equals(userId));

        if (emailExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email используется другим пользователем");
        }
    }


}
