package ru.practicum.shareit.booking.validation;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class BookingValidator {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public void checkItemAvailability(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Вещь не найдена"));

        if (!item.getAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Вещь недоступна для бронирования");
        }
    }

    public void checkOwnerIsNotBooker(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Вещь не найдена"));

        if (item.getOwner().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Владелец не может бронировать свою вещь");
        }
    }

    public void checkBookingDates(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || !end.isAfter(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверные даты бронирования");
        }
        if (start.isBefore(LocalDateTime.now().minusSeconds(1))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Дата начала бронирования не может быть в прошлом");
        }
    }
}
