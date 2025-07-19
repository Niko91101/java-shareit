package ru.practicum.shareit.user.validation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.repository.UserRepository;

@Component
public class UserValidator {

    public void checkEmailUnique(String email, UserRepository userRepository) {
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Такой Email уже используется");
        }
    }

    public void checkEmailUniqueForUpdate(String email, Long userId, UserRepository userRepository) {
        if (userRepository.existsByEmailAndIdNot(email, userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email используется другим пользователем");
        }
    }
}
