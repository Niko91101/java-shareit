package ru.practicum.shareit.item.validation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemValidator {

    public static void validate(Item item) {
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Имя обязательно");
        }

        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Описание обязательно");
        }

        if (item.getAvailable() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Поле available обязательно");
        }
    }
}
