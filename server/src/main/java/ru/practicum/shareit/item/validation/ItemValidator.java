package ru.practicum.shareit.item.validation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

@Component
public class ItemValidator {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemValidator(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public void checkItemExistence(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Предмет не найден");
        }
    }

    public void checkOwner(Long itemId, Long ownerId) {
        var item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Предмет не найден"));

        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Вы не являетесь владельцем этого предмета");
        }
    }

    public void checkUserExistence(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }
}
