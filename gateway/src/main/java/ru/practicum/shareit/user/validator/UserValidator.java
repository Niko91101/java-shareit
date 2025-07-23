package ru.practicum.shareit.user.validator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.client.UserClient;

@Component
public class UserValidator {

    private final UserClient userClient;

    public UserValidator(UserClient userClient) {
        this.userClient = userClient;
    }

    public void checkEmailUnique(String email) {
        ResponseEntity<Object> response = userClient.getAll();
        if (response.getBody().toString().contains(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Такой Email уже используется");
        }
    }

    public void checkEmailUniqueForUpdate(String email, Long userId) {
        ResponseEntity<Object> response = userClient.getAll();
        if (response.getBody().toString().contains(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email используется другим пользователем");
        }
    }
}


