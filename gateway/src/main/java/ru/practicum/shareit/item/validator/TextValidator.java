package ru.practicum.shareit.item.validator;

import org.springframework.stereotype.Component;

@Component
public class TextValidator {

    public boolean isTextValid(String text) {
        return text != null && !text.trim().isEmpty();
    }
}
