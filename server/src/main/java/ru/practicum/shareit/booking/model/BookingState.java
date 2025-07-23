package ru.practicum.shareit.booking.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static Optional<BookingState> from(String state) {
        try {
            return Optional.of(BookingState.valueOf(state.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown state: " + state);
        }
    }
}