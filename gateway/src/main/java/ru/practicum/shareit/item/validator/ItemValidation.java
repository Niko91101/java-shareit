package ru.practicum.shareit.item.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ItemValidation {
    public void validateUserId(long userId) {
        if (userId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный идентификатор пользователя.");
        }
    }
}
