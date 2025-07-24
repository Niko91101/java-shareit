package ru.practicum.shareit.booking.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Component
public class BookingValidation {

    public void checkBookingDates(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || !end.isAfter(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверные даты бронирования");
        }
        if (start.isBefore(LocalDateTime.now().minusSeconds(1))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Дата начала бронирования не может быть в прошлом");
        }
    }
}
